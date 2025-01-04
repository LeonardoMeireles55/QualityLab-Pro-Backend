package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.utils.components.RulesValidatorComponent;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class GenericAnalyticsRepositoryTest {

    @Autowired
    private GenericAnalyticsRepository repository;

    @Transient
    private RulesValidatorComponent rulesValidatorComponent;

    private LocalDateTime testDate;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    void setupTestData() {
        testDate = LocalDateTime.now();
        GenericAnalytics testAnalytics = new GenericAnalytics(
                1L,            // id
                testDate,        // date
                "LEVEL_LOT_1",   // levelLot
                "TEST_LOT_1",    // testLot
                "TEST_NAME",     // name
                "TEST_LEVEL",    // level
                1.0,            // value
                1.0,            // mean
                1.0,            // sd
                "UNIT_1",       // unitValue
                "RULE_1",       // rules
                "average", // description
                rulesValidatorComponent
        );

        repository.save(testAnalytics);
    }

    @Test
    @DisplayName("Should find analytics by name when exists")
    @Transactional
    void testExistsByName() {
        setupTestData();
        assertTrue(repository.existsByName("TEST_NAME"));
        assertFalse(repository.existsByName("NONEXISTENT"));
    }

    @Test
    @DisplayName("Should find all analytics by name with pagination")
    @Transactional
    void testFindAllByName() {
        setupTestData();
        PageRequest pageable = PageRequest.of(0, 10);
        List<GenericValuesRecord> results = repository.findAllByName("TEST_NAME",pageable);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).name()).isEqualTo("TEST_NAME");
    }

    @Test
    @DisplayName("Should verify existence by date, level and name")
    @Transactional
    void testExistsByDateAndLevelAndName() {
        setupTestData();

        boolean exists = repository.existsByDateAndLevelAndName(
                testDate,
                "TEST_LEVEL",
                "TEST_NAME"
        );

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should find all by name and level with pagination")
    @Transactional
    void testFindAllByNameAndLevel() {
        setupTestData();
        PageRequest pageable = PageRequest.of(0, 10);

        List<GenericValuesRecord> results = repository.findAllByNameAndLevel(
                pageable,
                "TEST_NAME",
                "TEST_LEVEL"
        );

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).name()).isEqualTo("TEST_NAME");
        assertThat(results.get(0).level()).isEqualTo("TEST_LEVEL");
    }

    @Test
    @DisplayName("Should find all by names in list and date range")
    @Transactional
    void testFindAllByNameInAndDateBetween() {
        setupTestData();
        List<String> names = Arrays.asList("TEST_NAME");

        List<GenericValuesRecord> results = repository.findAllByNameInAndDateBetween(
                names,
                testDate.minusDays(1),
                testDate.plusDays(1)
        );

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).name()).isEqualTo("TEST_NAME");
    }

    @Test
    @DisplayName("Should find all by names in list with pagination")
    @Transactional
    void testFindAllByNameIn() {
        setupTestData();
        List<String> names = Arrays.asList("TEST_NAME");
        PageRequest pageable = PageRequest.of(0, 10);

        List<GenericValuesRecord> results = repository.findAllByNameIn(names, pageable);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).name()).isEqualTo("TEST_NAME");
    }

    @Test
    @DisplayName("Should find all by name, level and date range with pagination")
    @Transactional
    void testFindAllByNameAndLevelAndDateBetween() {
        setupTestData();
        PageRequest pageable = PageRequest.of(0, 10);

        List<GenericValuesRecord> results = repository.findAllByNameAndLevelAndDateBetween(
                "TEST_NAME",
                "TEST_LEVEL",
                testDate.minusDays(1),
                testDate.plusDays(1),
                pageable
        );

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).name()).isEqualTo("TEST_NAME");
        assertThat(results.get(0).level()).isEqualTo("TEST_LEVEL");
    }

    @Test
    @DisplayName("Should find all by date range")
    @Transactional
    void testFindAllByDateBetween() {
        setupTestData();

        List<GenericValuesRecord> results = repository.findAllByDateBetween(
                testDate.minusDays(1),
                testDate.plusDays(1)
        );

        assertThat(results).isNotEmpty();
    }

    @Test
    @DisplayName("Should find all by name and date range grouped by level")
    @Transactional
    void testFindAllByNameAndDateBetweenGroupByLevel() {
        setupTestData();
        PageRequest pageable = PageRequest.of(0, 10);

        List<GenericValuesRecord> results = repository.findAllByNameAndDateBetweenGroupByLevel(
                "TEST_NAME",
                testDate.minusDays(1),
                testDate.plusDays(1),
                pageable
        );

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).name()).isEqualTo("TEST_NAME");
    }
}