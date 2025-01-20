package tp.fstl.nfe101.pharmacies.db.consumer.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.CityDetails;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.DepartementSummary;
import tp.fstl.nfe101.pharmacies.db.consumer.model.City;
import tp.fstl.nfe101.pharmacies.db.consumer.rest.CityController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CityAssembler implements RepresentationModelAssembler<City, EntityModel<CityDetails>> {

    @Override
    public EntityModel<CityDetails> toModel(City city) {
        DepartementSummary departementSummary = new DepartementSummary(
            city.getDepartement().getDepartementId(),
            city.getDepartement().getCode(),
            city.getDepartement().getName()
        );

        CityDetails details = new CityDetails(
            city.getCityId(),
            city.getPostalCode(),
            city.getName(),
            departementSummary
        );

        return EntityModel.of(details,
            linkTo(methodOn(CityController.class).one(city.getCityId())).withSelfRel(),
            linkTo(methodOn(CityController.class).all()).withRel("cities")
        );
    }
}
