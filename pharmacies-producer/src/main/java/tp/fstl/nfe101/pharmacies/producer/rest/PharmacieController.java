package tp.fstl.nfe101.pharmacies.producer.rest;

import tp.fstl.nfe101.pharmacies.producer.service.PharmacieManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pharmacies")
public class PharmacieController {
    
    private final PharmacieManager pharmacieManager;

    public PharmacieController(PharmacieManager pharmacieManager) {
        this.pharmacieManager = pharmacieManager;
    }

    @PostMapping
    public ResponseEntity<String> transfer(@RequestParam String file) {
        try {
            pharmacieManager.transfer(file);
            return ResponseEntity.ok("File imported successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }

    }
}
