package tp.fstl.nfe101.pharmacies.db.consumer.dto.record;

public record CityDetails(
    Integer cityId,
    String postalCode,
    String name,
    DepartementSummary departement
) {}
