package tp.fstl.nfe101.pharmacies.db.consumer.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.fstl.nfe101.pharmacies.db.consumer.assembler.DepartementAssembler;
import tp.fstl.nfe101.pharmacies.db.consumer.assembler.DepartementSummaryAssembler;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.CustomItemsListModel;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.DepartementDetails;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.DepartementSummary;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.NewDepartement;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.DepartementNotFoundException;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Departement;
import tp.fstl.nfe101.pharmacies.db.consumer.repository.DepartementRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departements")
public class DepartementController {

    private final DepartementRepository departementRepository;
    private final DepartementAssembler departementAssembler;
    private final DepartementSummaryAssembler departementSummaryAssembler;

    public DepartementController(DepartementRepository departementRepository, DepartementAssembler departementAssembler, DepartementSummaryAssembler departementSummaryAssembler) {
        this.departementRepository = departementRepository;
        this.departementAssembler = departementAssembler;
        this.departementSummaryAssembler = departementSummaryAssembler;
    }

    @GetMapping
    public CustomItemsListModel<DepartementSummary> all() {
        List<EntityModel<DepartementSummary>> departements = departementRepository.findAll().stream()
                .map(departementSummaryAssembler::toModel)
                .collect(Collectors.toList());

        CustomItemsListModel<DepartementSummary> departementsModel = new CustomItemsListModel<>(departements);
        departementsModel.add(linkTo(methodOn(DepartementController.class).all()).withSelfRel());

        return departementsModel;
    }

    @PostMapping
    public ResponseEntity<?> newDepartement(@RequestBody NewDepartement newDepartement) {
        Departement newDepartementEntity = new Departement();
        newDepartementEntity.setCode(newDepartement.code());
        newDepartementEntity.setName(newDepartement.name());

        EntityModel<DepartementDetails> entityModel = departementAssembler.toModel(departementRepository.save(newDepartementEntity));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public EntityModel<DepartementDetails> one(@PathVariable Integer id) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new DepartementNotFoundException("Departement not found with id" + id));

        return departementAssembler.toModel(departement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceDepartement(@RequestBody Departement newDepartement, @PathVariable Integer id) {
        Departement updatedDepartement = departementRepository.findById(id)
                .map(departement -> {
                    departement.setCode(newDepartement.getCode());
                    departement.setName(newDepartement.getName());
                    return departementRepository.save(departement);
                })
                .orElseGet(() -> {
                    newDepartement.setDepartementId(id);
                    return departementRepository.save(newDepartement);
                });

        EntityModel<DepartementDetails> entityModel = departementAssembler.toModel(updatedDepartement);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartement(@PathVariable Integer id) {
        departementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
