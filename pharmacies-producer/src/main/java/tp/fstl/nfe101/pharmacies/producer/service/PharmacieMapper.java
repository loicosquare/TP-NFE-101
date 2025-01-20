package tp.fstl.nfe101.pharmacies.producer.service;

import tp.fstl.nfe101.pharmacies.producer.model.PharmacieCsv;
import tp.fstl.nfe101.pharmacies.producer.model.PharmacieJson;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PharmacieMapper {
    public static PharmacieJson toJson(PharmacieCsv csv) {
        return PharmacieJson.builder()
            .finessET(csv.getNoFinessET())
            .finessEJ(csv.getNoFinessEJ())
            .name(csv.getRaisonSociale())
            .longName(csv.getRaisonSocialeLongue())
            .nameComplement(csv.getComplementRaisonSociale())
            .streetNumber(csv.getNumeroVoie())
            .streetComplement(csv.getComplementVoie())
            .streetType(csv.getTypeVoie())
            .streetName(csv.getVoie())
            .locationDetails(csv.getLieuDitBP())
            .department(csv.getDepartement())
            .departmentLabel(csv.getLibelleDepartement())
            .postalCode(csv.getCodePostal())
            .city(csv.getCommune())
            .phone(csv.getTelephone())
            .fax(csv.getTelecopie())
            .openingDate(csv.getDateOuverture())
            .authorizationDate(csv.getDateAutorisation())
            .lastUpdate(csv.getDateMiseAJour())
            .coordinates(csv.getCoordonneesWGS84())
            .latitude(csv.getLatitude())
            .longitude(csv.getLongitude())
        .build();
    }
}
