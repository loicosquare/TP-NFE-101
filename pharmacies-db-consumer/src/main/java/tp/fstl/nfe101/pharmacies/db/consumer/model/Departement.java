package tp.fstl.nfe101.pharmacies.db.consumer.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant les départements.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departements")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "departement_id")
    private Integer departementId;

    @Column(name = "departement_code")
    private String code;

    @Column(name = "departement_name")
    private String name;
}
