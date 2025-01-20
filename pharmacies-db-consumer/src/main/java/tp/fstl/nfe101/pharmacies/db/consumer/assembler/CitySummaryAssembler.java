package tp.fstl.nfe101.pharmacies.db.consumer.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.record.CitySummary;
import tp.fstl.nfe101.pharmacies.db.consumer.model.City;
import tp.fstl.nfe101.pharmacies.db.consumer.rest.CityController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CitySummaryAssembler implements RepresentationModelAssembler<City, EntityModel<CitySummary>> {

    @Override
    public EntityModel<CitySummary> toModel(City city) {
        CitySummary summary = new CitySummary(
            city.getCityId(),
            city.getName(),
            city.getPostalCode()
        );

        return EntityModel.of(summary,
            linkTo(methodOn(CityController.class).one(city.getCityId())).withSelfRel(),
            linkTo(methodOn(CityController.class).all()).withRel("cities")
        );
    }
}
