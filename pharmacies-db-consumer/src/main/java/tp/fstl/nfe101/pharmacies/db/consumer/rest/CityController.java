package tp.fstl.nfe101.pharmacies.db.consumer.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.fstl.nfe101.pharmacies.db.consumer.assembler.CityAssembler;
import tp.fstl.nfe101.pharmacies.db.consumer.assembler.CitySummaryAssembler;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.CustomItemsListModel;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.CityDetails;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.CitySummary;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.NewCity;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.CityNotFoundException;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.DepartementNotFoundException;
import tp.fstl.nfe101.pharmacies.db.consumer.model.City;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Departement;
import tp.fstl.nfe101.pharmacies.db.consumer.repository.CityRepository;
import tp.fstl.nfe101.pharmacies.db.consumer.repository.DepartementRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityRepository cityRepository;
    private final CityAssembler cityAssembler;
    private final DepartementRepository departementRepository;
    private final CitySummaryAssembler citySummaryAssembler;

    public CityController(CityRepository cityRepository, CityAssembler cityAssembler, DepartementRepository departementRepository, CitySummaryAssembler citySummaryAssembler) {
        this.cityRepository = cityRepository;
        this.cityAssembler = cityAssembler;
        this.departementRepository = departementRepository;
        this.citySummaryAssembler = citySummaryAssembler;
    }

    @GetMapping
    public CustomItemsListModel<CitySummary> all() {
        List<EntityModel<CitySummary>> cities = cityRepository.findAll().stream()
                .map(citySummaryAssembler::toModel)
                .collect(Collectors.toList());

        CustomItemsListModel<CitySummary> citiesModel = new CustomItemsListModel<>(cities);
        citiesModel.add(linkTo(methodOn(CityController.class).all()).withSelfRel());

        return citiesModel;
    }

    @PostMapping
    public ResponseEntity<?> newCity(@RequestBody NewCity newCity) {
        Departement departement = departementRepository.findById(newCity.departementId())
                .orElseThrow(() -> new DepartementNotFoundException("Departement not found with id : " + newCity.departementId()));

        City newCityEntity = new City();
        newCityEntity.setName(newCity.name());
        newCityEntity.setPostalCode(newCity.postalCode());
        newCityEntity.setDepartement(departement);

        EntityModel<CityDetails> entityModel = cityAssembler.toModel(cityRepository.save(newCityEntity));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public EntityModel<CityDetails> one(@PathVariable Integer id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("City with id " + id + "Not found"));
        return cityAssembler.toModel(city);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceCity(@RequestBody City newCity, @PathVariable Integer id) {
        City updatedCity = cityRepository.findById(id)
                .map(city -> {
                    city.setName(newCity.getName());
                    city.setPostalCode(newCity.getPostalCode());
                    city.setDepartement(newCity.getDepartement());
                    return cityRepository.save(city);
                })
                .orElseGet(() -> {
                    newCity.setCityId(id);
                    return cityRepository.save(newCity);
                });

        EntityModel<CityDetails> entityModel = cityAssembler.toModel(updatedCity);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Integer id) {
        cityRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
