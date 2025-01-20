package tp.fstl.nfe101.pharmacies.db.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.PharmacieJson;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.InvalidPostalCodeException;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.CityProcessingException;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.PharmacieSavingException;
import tp.fstl.nfe101.pharmacies.db.consumer.model.City;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Departement;
import tp.fstl.nfe101.pharmacies.db.consumer.model.Pharmacie;
import tp.fstl.nfe101.pharmacies.db.consumer.model.City;
import tp.fstl.nfe101.pharmacies.db.consumer.repository.DepartementRepository;
import tp.fstl.nfe101.pharmacies.db.consumer.repository.PharmacieRepository;
import tp.fstl.nfe101.pharmacies.db.consumer.repository.CityRepository;

import java.util.Optional;

@Service
public class PharmacieService {
    
    private static final Logger log = LoggerFactory.getLogger(PharmacieService.class);

    private final PharmacieRepository pharmacieRepository;
    private final CityRepository cityRepository;
    private final DepartementRepository departementRepository;

    public PharmacieService(PharmacieRepository pharmacieRepository,
        CityRepository cityRepository,
        DepartementRepository departementRepository) {
        this.pharmacieRepository = pharmacieRepository;
        this.cityRepository = cityRepository;
        this.departementRepository = departementRepository;
    }

    public void save(PharmacieJson json) {
        City city;
        try {
            city = getOrSaveCity(json);
        } catch (Exception e) {
            log.error("Error getting/saving city from pharmacie object. City name: {}, postal code: {}", json.getCity(), json.getPostalCode(), e);
            throw new CityProcessingException("Error getting or saving city for city name: " + json.getCity() + ", postal code: " + json.getPostalCode(), e);
        }
        try {
            savePharmacie(json, city);
        } catch (Exception e) {
            log.error("Error saving pharmacie object in DB {}", json, e);
            throw new PharmacieSavingException("Error saving pharmacie object in DB. Pharmacie details: " + json, e);
        }
    }

    private void savePharmacie(PharmacieJson json, City city) {
        // Recherche d'une pharmacie existante par son nom, ou création d'une nouvelle instance
        Pharmacie pharmacie = pharmacieRepository.findByName(json.getName())
                .orElseGet(Pharmacie::new);

        // Mise à jour des propriétés de la pharmacie
        pharmacie.setCity(city);
        pharmacie.setName(json.getName());
        pharmacie.setLongName(json.getLongName());
        pharmacie.setNameComplement(json.getNameComplement());
        pharmacie.setStreetNumber(json.getStreetNumber());
        pharmacie.setStreetComplement(json.getStreetComplement());
        pharmacie.setStreetType(json.getStreetType());
        pharmacie.setStreetName(json.getStreetName());
        pharmacie.setLocationDetails(json.getLocationDetails());
        pharmacie.setPhone(json.getPhone());
        pharmacie.setFax(json.getFax());
        pharmacie.setOpeningDate(json.getOpeningDate());
        pharmacie.setAuthorizationDate(json.getAuthorizationDate());
        pharmacie.setLastUpdate(json.getLastUpdate());
        pharmacie.setLatitude(json.getLatitude());
        pharmacie.setLongitude(json.getLongitude());
        pharmacie.setFinessET(json.getFinessET());
        pharmacie.setFinessEJ(json.getFinessEJ());

        try {
            // Sauvegarde dans la base de données
            pharmacieRepository.save(pharmacie);
            log.info("Pharmacie successfully saved: {}", pharmacie);
        } catch (Exception e) {
            log.error("Error while saving pharmacie to the database. Pharmacie: {}", pharmacie, e);
            throw new PharmacieSavingException("Error while saving pharmacie to the database. Details: " + pharmacie, e);
        }
    }

    private City getOrSaveCity(PharmacieJson json) {
        // Récupération ou sauvegarde de la city
        Departement departement = getOrSaveDepartement(json.getPostalCode(), json.getDepartmentLabel());

        // Recherche de la city par son nom et son code postal, ou création d'une nouvelle instance
        return cityRepository.findByNameAndPostalCode(json.getCity(), json.getPostalCode())
            .orElseGet(() -> {
                City newCity = City.builder()
                    .name(json.getCity())
                    .postalCode(json.getPostalCode())
                    .departement(departement)
                    .build();
                return cityRepository.save(newCity);
            }
        );
    }

    private Departement getOrSaveDepartement(String postalCode, String departmentName) {
        String departmentCode;

        // Vérification de la validité du code postal
        if (postalCode == null || postalCode.length() < 2 || postalCode.isBlank()) {
            log.debug("Le code postal est null ou vide, la valeur par défaut sera utilisée.");
            //throw new InvalidPostalCodeException("Postal code is invalid: " + postalCode);
            postalCode = "00000"; //Je mets ce code par défaut pour éviter l'erreur et continuer les autres enregistrements.
        }

        // Extraction des deux premiers chiffres du code postal
        departmentCode = postalCode.substring(0, 2);

        // Rechercher le département dans la base de données
        return departementRepository.findByCode(departmentCode)
            .orElseGet(() -> {
                // Création et sauvegarde d'un nouveau département si non trouvé
                Departement newDepartement = Departement.builder()
                    .code(departmentCode)
                    .name(departmentName)
                    .build();
                return departementRepository.save(newDepartement);
            }
        );
    }
}
