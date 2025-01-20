package tp.fstl.nfe101.pharmacies.producer.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe représentant les données CSV de la pharmacie.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacieCsv {
    @CsvBindByName(column = "nofinesset")
    private String noFinessET;

    @CsvBindByName(column = "nofinessej")
    private String noFinessEJ;

    @CsvBindByName(column = "rs")
    private String raisonSociale;

    @CsvBindByName(column = "rslongue")
    private String raisonSocialeLongue;

    @CsvBindByName(column = "complrs")
    private String complementRaisonSociale;

    @CsvBindByName(column = "numvoie")
    private String numeroVoie;

    @CsvBindByName(column = "compvoie")
    private String complementVoie;

    @CsvBindByName(column = "typvoie")
    private String typeVoie;

    @CsvBindByName(column = "voie")
    private String voie;

    @CsvBindByName(column = "lieuditbp")
    private String lieuDitBP;

    @CsvBindByName(column = "departement")
    private String departement;

    @CsvBindByName(column = "libdepartement")
    private String libelleDepartement;

    @CsvBindByName(column = "cp")
    private String codePostal;

    @CsvBindByName(column = "commune")
    private String commune;

    @CsvBindByName(column = "telephone")
    private String telephone;

    @CsvBindByName(column = "telecopie")
    private String telecopie;

    @CsvBindByName(column = "dateouv")
    private String dateOuverture;

    @CsvBindByName(column = "dateautor")
    private String dateAutorisation;

    @CsvBindByName(column = "datemaj")
    private String dateMiseAJour;

    @CsvBindByName(column = "wgs84")
    private String coordonneesWGS84;

    @CsvBindByName(column = "lat")
    private String latitude;

    @CsvBindByName(column = "lng")
    private String longitude;
}
