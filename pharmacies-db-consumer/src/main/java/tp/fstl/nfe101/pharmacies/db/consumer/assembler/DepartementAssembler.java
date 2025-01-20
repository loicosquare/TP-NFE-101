package tp.fstl.nfe101.pharmacies.db.consumer.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.DepartementDetails;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Departement;
import tp.fstl.nfe101.pharmacies.db.consumer.rest.DepartementController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DepartementAssembler implements RepresentationModelAssembler<Departement, EntityModel<DepartementDetails>> {

    @Override
    public EntityModel<DepartementDetails> toModel(Departement departement) {
        DepartementDetails details = new DepartementDetails(
            departement.getDepartementId(),
            departement.getCode(),
            departement.getName()
        );

        return EntityModel.of(details,
            linkTo(methodOn(DepartementController.class).one(departement.getDepartementId())).withSelfRel(),
            linkTo(methodOn(DepartementController.class).all()).withRel("departements")
        );
    }
}
