package tp.fstl.nfe101.pharmacies.db.consumer.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Pharmacie;

import java.util.List;
import java.util.Optional;

public interface PharmacieRepository extends JpaRepository<Pharmacie, Integer> {

    /**
     * Find a pharmacy by its id
     * @param id
     * @return the pharmacy
     */
    @EntityGraph(attributePaths = {"city"})
    Optional<Pharmacie> findById(Integer id);

    /**
     * Find a pharmacy by its name
     * @param name
     * @return the pharmacy
     */
    Optional<Pharmacie> findByName(String name);
}
