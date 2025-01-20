package tp.fstl.nfe101.pharmacies.db.consumer.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.fstl.nfe101.pharmacies.db.consumer.model.City;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {

    /**
     * Find a city by its id
     * @param id
     * @return the city
     */
    @EntityGraph(attributePaths = {"departement"})
    Optional<City> findById(Integer id);

    /**
     * Find a city by its name and postal code
     * @param name
     * @param postalCode
     * @return the city
     */
    Optional<City> findByNameAndPostalCode(String name, String postalCode);
}
