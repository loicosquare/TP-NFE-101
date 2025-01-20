package tp.fstl.nfe101.pharmacies.db.consumer.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.fstl.nfe101.pharmacies.db.consumer.assembler.PharmacieAssembler;
import tp.fstl.nfe101.pharmacies.db.consumer.assembler.PharmacieSummaryAssembler;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.CustomItemsListModel;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.NewPharmacie;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.PharmacieDetails;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.PharmacieSummary;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.CityNotFoundException;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.PharmacieNotFoundException;
import tp.fstl.nfe101.pharmacies.db.consumer.model.City;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Pharmacie;
import tp.fstl.nfe101.pharmacies.db.consumer.repository.CityRepository;
import tp.fstl.nfe101.pharmacies.db.consumer.repository.PharmacieRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pharmacies")
public class PharmacieController {

    private final PharmacieRepository pharmacieRepository;
    private final PharmacieAssembler pharmacieAssembler;
    private final CityRepository cityRepository;
    private final PharmacieSummaryAssembler pharmacieSummaryAssembler;

    public PharmacieController(PharmacieRepository pharmacieRepository, PharmacieAssembler pharmacieAssembler,
                               CityRepository cityRepository, PharmacieSummaryAssembler pharmacieSummaryAssembler) {
        this.pharmacieRepository = pharmacieRepository;
        this.pharmacieAssembler = pharmacieAssembler;
        this.cityRepository = cityRepository;
        this.pharmacieSummaryAssembler = pharmacieSummaryAssembler;
    }

    @GetMapping
    public CustomItemsListModel<PharmacieSummary> all() {
        List<EntityModel<PharmacieSummary>> pharmacies = pharmacieRepository.findAll().stream()
                .map(pharmacieSummaryAssembler::toModel)
                .collect(Collectors.toList());

        CustomItemsListModel<PharmacieSummary> pharmaciesModel = new CustomItemsListModel<>(pharmacies);
        pharmaciesModel.add(linkTo(methodOn(PharmacieController.class).all()).withSelfRel());

        return pharmaciesModel;
    }

    @PostMapping
    public ResponseEntity<?> newPharmacie(@RequestBody NewPharmacie newPharmacie) {
        City city = cityRepository.findById(newPharmacie.cityId())
                .orElseThrow(() -> new CityNotFoundException("City not found with id : " + newPharmacie.cityId()));

        Pharmacie newPharmacieEntity = new Pharmacie();
        newPharmacieEntity.setName(newPharmacie.name());
        newPharmacieEntity.setCity(city);


        EntityModel<PharmacieDetails> entityModel = pharmacieAssembler.toModel(pharmacieRepository.save(newPharmacieEntity));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public EntityModel<PharmacieDetails> one(@PathVariable Integer id) {
        Pharmacie pharmacie = pharmacieRepository.findById(id)
                .orElseThrow(() -> new PharmacieNotFoundException("Pharmacie with id : " + id + " Not found"));

        return pharmacieAssembler.toModel(pharmacie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replacePharmacie(@RequestBody Pharmacie newPharmacie, @PathVariable Integer id) {
        Pharmacie updatedPharmacie = pharmacieRepository.findById(id)
                .map(pharmacie -> {
                    pharmacie.setName(newPharmacie.getName());
                    pharmacie.setCity(newPharmacie.getCity());

                    return pharmacieRepository.save(pharmacie);
                })
                .orElseGet(() -> {
                    newPharmacie.setPharmacieId(id);
                    return pharmacieRepository.save(newPharmacie);
                });

        EntityModel<PharmacieDetails> entityModel = pharmacieAssembler.toModel(updatedPharmacie);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePharmacie(@PathVariable Integer id) {
        pharmacieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
