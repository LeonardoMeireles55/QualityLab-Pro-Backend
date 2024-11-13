package leonardo.labutilities.qualitylabpro.components;

import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsValidationComponent {
    private final GenericAnalyticsRepository genericAnalyticsRepository;


}
