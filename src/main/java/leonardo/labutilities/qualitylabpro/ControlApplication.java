package leonardo.labutilities.qualitylabpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication()
public class ControlApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ControlApplication.class, args);
    }
}
