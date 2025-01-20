package tp.fstl.nfe101.pharmacies.db.consumer.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.DepartementSummary;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Departement;
import tp.fstl.nfe101.pharmacies.db.consumer.rest.DepartementController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DepartementSummaryAssembler implements RepresentationModelAssembler<Departement, EntityModel<DepartementSummary>> {

    @Override
    public EntityModel<DepartementSummary> toModel(Departement departement) {
        DepartementSummary summary = new DepartementSummary(
            departement.getDepartementId(),
            departement.getCode(),
            departement.getName()
        );

        return EntityModel.of(summary,
            linkTo(methodOn(DepartementController.class).one(departement.getDepartementId())).withSelfRel(),
            linkTo(methodOn(DepartementController.class).all()).withRel("departements")
        );
    }
}
