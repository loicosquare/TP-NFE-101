package tp.fstl.nfe101.pharmacies.db.consumer.dto.record;

public record PharmacieDetails(
    Integer pharmacieId,
    String finessET,
    String finessEJ,
    String name,
    String longName,
    String nameComplement,
    String streetNumber,
    String streetComplement,
    String streetType,
    String streetName,
    String locationDetails,
    String phone,
    String fax,
    String openingDate,
    String authorizationDate,
    String lastUpdate,
    String latitude,
    String longitude,
    CitySummary city
) {}
