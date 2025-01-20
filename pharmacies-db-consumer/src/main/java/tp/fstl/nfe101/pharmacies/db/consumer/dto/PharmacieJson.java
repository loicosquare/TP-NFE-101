package tp.fstl.nfe101.pharmacies.db.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe représentant les données JSON simplifiées de la pharmacie => On utilisera cette classe comme DTP.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PharmacieJson {
    private String finessET;
    private String finessEJ;
    private String name;
    private String longName;
    private String nameComplement;
    private String streetNumber;
    private String streetComplement;
    private String streetType;
    private String streetName;
    private String locationDetails;
    private String department;
    private String departmentLabel;
    private String postalCode;
    private String city;
    private String phone;
    private String fax;
    private String openingDate;
    private String authorizationDate;
    private String lastUpdate;
    private String coordinates;
    private String latitude;
    private String longitude;
}
