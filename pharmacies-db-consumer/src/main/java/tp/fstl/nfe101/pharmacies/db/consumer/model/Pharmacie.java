package tp.fstl.nfe101.pharmacies.db.consumer.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant les pharmacies.
 */
@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pharmacies")
public class Pharmacie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacie_id")
    private Integer pharmacieId;

    @Column(name = "finess_et")
    private String finessET;

    @Column(name = "finess_ej")
    private String finessEJ;

    @Column(name = "name")
    private String name;

    @Column(name = "long_name")
    private String longName;

    @Column(name = "name_complement")
    private String nameComplement;

    @Column(name = "street_number")
    private String streetNumber;

    @Column(name = "street_complement")
    private String streetComplement;

    @Column(name = "street_type")
    private String streetType;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "location_details")
    private String locationDetails;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "opening_date")
    private String openingDate;

    @Column(name = "authorization_date")
    private String authorizationDate;

    @Column(name = "last_update")
    private String lastUpdate;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;
}
