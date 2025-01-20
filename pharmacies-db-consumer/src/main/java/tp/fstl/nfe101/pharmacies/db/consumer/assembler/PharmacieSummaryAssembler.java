package tp.fstl.nfe101.pharmacies.db.consumer.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.PharmacieSummary;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Pharmacie;
import tp.fstl.nfe101.pharmacies.db.consumer.rest.PharmacieController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PharmacieSummaryAssembler implements RepresentationModelAssembler<Pharmacie, EntityModel<PharmacieSummary>> {

    @Override
    public EntityModel<PharmacieSummary> toModel(Pharmacie pharmacie) {
        String cityName = null;
        if (pharmacie.getCity() != null) {
            cityName = pharmacie.getCity().getName();
        }

        PharmacieSummary summary = new PharmacieSummary(
                pharmacie.getPharmacieId(),
                pharmacie.getName(),
                cityName
        );

        return EntityModel.of(summary,
                linkTo(methodOn(PharmacieController.class).one(pharmacie.getPharmacieId())).withSelfRel(),
                linkTo(methodOn(PharmacieController.class).all()).withRel("pharmacies")
        );
    }
}
