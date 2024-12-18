package leonardo.labutilities.qualitylabpro.infra.config.date;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore({ JacksonAutoConfiguration.class })
public class DateTimeAutoConfiguration {}
