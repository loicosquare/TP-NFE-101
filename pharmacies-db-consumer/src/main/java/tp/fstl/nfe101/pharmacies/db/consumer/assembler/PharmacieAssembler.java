package tp.fstl.nfe101.pharmacies.db.consumer.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.CitySummary;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.PharmacieDetails;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Pharmacie;
import tp.fstl.nfe101.pharmacies.db.consumer.rest.PharmacieController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PharmacieAssembler implements RepresentationModelAssembler<Pharmacie, EntityModel<PharmacieDetails>> {

    @Override
    public EntityModel<PharmacieDetails> toModel(Pharmacie pharmacie) {
        PharmacieDetails details = new PharmacieDetails(
                pharmacie.getPharmacieId(),
                pharmacie.getFinessET(),
                pharmacie.getFinessEJ(),
                pharmacie.getName(),
                pharmacie.getLongName(),
                pharmacie.getNameComplement(),
                pharmacie.getStreetNumber(),
                pharmacie.getStreetComplement(),
                pharmacie.getStreetType(),
                pharmacie.getStreetName(),
                pharmacie.getLocationDetails(),
                pharmacie.getPhone(),
                pharmacie.getFax(),
                pharmacie.getOpeningDate(),
                pharmacie.getAuthorizationDate(),
                pharmacie.getLastUpdate(),
                pharmacie.getLatitude(),
                pharmacie.getLongitude(),
                new CitySummary(
                        pharmacie.getCity().getCityId(),
                        pharmacie.getCity().getName(),
                        pharmacie.getCity().getPostalCode()
                )
        );

        return EntityModel.of(details,
                linkTo(methodOn(PharmacieController.class).one(pharmacie.getPharmacieId())).withSelfRel(),
                linkTo(methodOn(PharmacieController.class).all()).withRel("pharmacies")
        );
    }
}
