package leonardo.labutilities.qualitylabpro.repositories;

import static leonardo.labutilities.qualitylabpro.utils.AnalyticsHelperMocks.createSampleRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.Transient;
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

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class GenericAnalyticsRepositoryTest {

	@Autowired
	GenericAnalyticsRepository repository;

	@Transient
	RulesValidatorComponent rulesValidatorComponent = new RulesValidatorComponent();

	@Transient
	LocalDateTime testDate = LocalDateTime.of(2024, 12, 16, 7, 53);
	@BeforeEach
	void clearDatabase(@Autowired Flyway flyway) {
		flyway.clean();
		flyway.migrate();
	}
	void setupTestData() {

		GenericAnalytics analytics = new GenericAnalytics(createSampleRecord(), rulesValidatorComponent);

		repository.save(analytics);
	}

	@Test
	@DisplayName("Should find analytics by name when exists")
	void testExistsByName() {
		setupTestData();
		assertTrue(repository.existsByName("ALB2"));
		assertFalse(repository.existsByName("NONEXISTENT"));
	}

	@Test
	@DisplayName("Should find all analytics by name with pagination")
	void testFindAllByName() {
		setupTestData();
		PageRequest pageable = PageRequest.of(0, 10);
		List<GenericValuesRecord> results = repository.findAllByName("ALB2", pageable);

		assertThat(results).isNotEmpty();
		assertThat(results.getFirst().name()).isEqualTo("ALB2");
	}

	@Test
	@DisplayName("Should verify existence by date, level and name")
	void testExistsByDateAndLevelAndName() {
		setupTestData();
		boolean exists =
				repository.existsByDateAndLevelAndName(testDate, "PCCC1", "ALB2");

		assertTrue(exists);
	}

	@Test
	@DisplayName("Should find all by name and level with pagination")
	void testFindAllByNameAndLevel() {
		setupTestData();
		PageRequest pageable = PageRequest.of(0, 10);

		List<GenericValuesRecord> results =
				repository.findAllByNameAndLevel(pageable, "ALB2", "PCCC1");

		assertThat(results).isNotEmpty();
		assertThat(results.getFirst().name()).isEqualTo("ALB2");
		assertThat(results.getFirst().level()).isEqualTo("PCCC1");
	}

	@Test
	@DisplayName("Should find all by names in list and date range")
	void testFindAllByNameInAndDateBetween() {
		setupTestData();
		List<String> names = List.of("ALB2");

		List<GenericValuesRecord> results = repository.findAllByNameInAndDateBetween(names,
				testDate.minusDays(1), testDate.plusDays(1));

		assertThat(results).isNotEmpty();
		assertThat(results.getFirst().name()).isEqualTo("ALB2");
	}

	@Test
	@DisplayName("Should find all by names in list with pagination")
	void testFindAllByNameIn() {
		setupTestData();
		List<String> names = List.of("ALB2");
		PageRequest pageable = PageRequest.of(0, 10);

		List<GenericValuesRecord> results = repository.findAllByNameIn(names, pageable);

		assertThat(results).isNotEmpty();
		assertThat(results.getFirst().name()).isEqualTo("ALB2");
	}

	@Test
	@DisplayName("Should find all by name, level and date range with pagination")
	void testFindAllByNameAndLevelAndDateBetween() {
		setupTestData();
		PageRequest pageable = PageRequest.of(0, 10);

		List<GenericValuesRecord> results = repository.findAllByNameAndLevelAndDateBetween(
				"ALB2", "PCCC1", testDate.minusDays(1), testDate.plusDays(1), pageable);

		assertThat(results).isNotEmpty();
		assertThat(results.getFirst().name()).isEqualTo("ALB2");
		assertThat(results.getFirst().level()).isEqualTo("PCCC1");
	}

	@Test
	@DisplayName("Should find all by date range")
	void testFindAllByDateBetween() {
		setupTestData();
		List<GenericValuesRecord> results =
				repository.findAllByDateBetween(testDate.minusDays(1), testDate.plusDays(1));
		assertThat(results).isNotEmpty();
	}

	@Test
	@DisplayName("Should find all by name and date range grouped by level")
	void testFindAllByNameAndDateBetweenGroupByLevel() {
		setupTestData();
		PageRequest pageable = PageRequest.of(0, 10);

		List<GenericValuesRecord> results = repository.findAllByNameAndDateBetweenGroupByLevel(
				"ALB2", testDate.minusDays(1), testDate.plusDays(1), pageable);

		assertThat(results).isNotEmpty();
		assertThat(results.getFirst().name()).isEqualTo("ALB2");
	}
}
