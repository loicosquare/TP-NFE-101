package tp.fstl.nfe101.pharmacies.db.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tp.fstl.nfe101.pharmacies.db.consumer.service.PharmacieConsumer;

@SpringBootApplication
public class PharmaciesDbConsumerApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(PharmaciesDbConsumerApplication.class);
	private final PharmacieConsumer pharmacieConsumer;

    public PharmaciesDbConsumerApplication(PharmacieConsumer pharmacieConsumer) {
        this.pharmacieConsumer = pharmacieConsumer;
    }

    public static void main(String[] args) {
		log.info("App started");
		SpringApplication.run(PharmaciesDbConsumerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("Starting consumer");
		pharmacieConsumer.consume();
	}
}
