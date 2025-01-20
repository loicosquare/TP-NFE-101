package tp.fstl.nfe101.pharmacies.db.consumer.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant les villes (city).
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id")
    private Departement departement;
}
