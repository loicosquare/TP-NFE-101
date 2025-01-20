package tp.fstl.nfe101.pharmacies.db.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Departement;

import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, Integer> {

    /**
     * Find a departement by its code
     * @param code => Qui correspond du deux premiers chiffres du code postal.
     * @return the departement
     */
    Optional<Departement> findByCode(String code);
}
