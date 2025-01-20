package tp.fstl.nfe101.pharmacies.producer.model;

import lombok.Builder;
import lombok.Data;

/**
 * Classe représentant les données JSON simplifiées de la pharmacie.
 */
@Data
@Builder
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

/**
 * Tableau des correspondances entre les colonnes du fichier CSV et les champs JSON.
 *
 * | **Colonne CSV**          | **Champ JSON**        |
 * |--------------------------|-----------------------|
 * | nofinesset               | finessET             |
 * | nofinessej               | finessEJ             |
 * | rs                       | name                 |
 * | rslongue                 | longName             |
 * | complrs                  | nameComplement       |
 * | numvoie                  | streetNumber         |
 * | compvoie                 | streetComplement     |
 * | typvoie                  | streetType           |
 * | voie                     | streetName           |
 * | lieuditbp                | locationDetails      |
 * | departement              | department           |
 * | libdepartement           | departmentLabel      |
 * | cp                       | postalCode           |
 * | commune                  | city                 |
 * | telephone                | phone                |
 * | telecopie                | fax                  |
 * | dateouv                  | openingDate          |
 * | dateautor                | authorizationDate    |
 * | datemaj                  | lastUpdate           |
 * | wgs84                    | coordinates          |
 * | lat                      | latitude             |
 * | lng                      | longitude            |
 */
