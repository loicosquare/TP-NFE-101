package tp.fstl.nfe101.pharmacies.db.consumer.service;

import org.springframework.stereotype.Service;

@Service
public interface PharmaciesStreamProcessor {
    /**
     * Process the pharmacies stream.
     */
    void processPharmaciesStream();
}
