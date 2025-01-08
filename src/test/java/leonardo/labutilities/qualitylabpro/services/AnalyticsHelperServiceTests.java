package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.dtos.analytics.AnalyticsRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GroupedValuesByLevel;
import leonardo.labutilities.qualitylabpro.entities.Analytics;
import leonardo.labutilities.qualitylabpro.repositories.AnalyticsRepository;
import leonardo.labutilities.qualitylabpro.services.analytics.AnalyticsHelperService;
import leonardo.labutilities.qualitylabpro.utils.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.utils.exception.CustomGlobalErrorHandling;
import leonardo.labutilities.qualitylabpro.utils.mappers.AnalyticsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static leonardo.labutilities.qualitylabpro.utils.AnalyticsHelperMocks.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsHelperServiceTests {

	@Mock
	private AnalyticsRepository analyticsRepository;

	@Mock
	private AnalyticsHelperService analyticsHelperService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		analyticsHelperService =
				new AnalyticsHelperService(analyticsRepository) {
					@Override
					public List<AnalyticsRecord> findAnalyticsByNameAndLevel
							(Pageable pageable, String name, String level) {
						return analyticsRepository.findAllByNameAndLevel(pageable, name,
								level).stream().map(AnalyticsMapper::toRecord).toList();
					}

					@Override
					public List<AnalyticsRecord> findAllAnalyticsByNameAndLevelAndDate(
							String name, String level, LocalDateTime dateStart,
							LocalDateTime dateEnd) {
						return analyticsRepository.findAllByNameAndLevelAndDateBetween(name,
								level, dateStart, dateEnd, PageRequest.of(0, 200))
								.stream().map(AnalyticsMapper::toRecord).toList();
					}
				};
	}

	@Test
	void shouldValidateRulesProcessedByRulesValidatorComponent() {
		// Arrange: create sample input records
		List<AnalyticsRecord> records = createSampleRecordList();
		RulesValidatorComponent rulesValidatorComponent = new RulesValidatorComponent();

		// Act: convert the records to AnalyticsRecord using the validation component
		List<leonardo.labutilities.qualitylabpro.entities.Analytics> analytics = records.stream()
				.map(values -> new leonardo.labutilities.qualitylabpro.entities.Analytics(values, rulesValidatorComponent)).toList();

		// Assert: validate the rules generated by the component
		assertEquals(records.stream().map(AnalyticsRecord::rules).toList(),
				analytics.stream().map(leonardo.labutilities.qualitylabpro.entities.Analytics::getRules).toList(),
				"The rules processed by the RulesValidatorComponent should match the input rules");
	}

	@Test
	void saveNewAnalyticsRecords_WithValidRecords_ShouldSaveSuccessfully() {
		List<AnalyticsRecord> records = createSampleRecordList();
		when(analyticsRepository.existsByDateAndLevelAndName(any(), any(), any()))
				.thenReturn(false);
		when(analyticsRepository.saveAll(any())).thenReturn(null);

		assertDoesNotThrow(() -> analyticsHelperService.saveNewAnalyticsRecords(records));
		verify(analyticsRepository, times(1)).saveAll(any());
	}

	@Test
	void saveNewAnalyticsRecords_WithDuplicateRecords_ShouldThrowException() {
		List<AnalyticsRecord> records = createSampleRecordList();
		when(analyticsRepository.existsByDateAndLevelAndName(any(), any(), any()))
				.thenReturn(true);

		assertThrows(CustomGlobalErrorHandling.DataIntegrityViolationException.class,
				() -> analyticsHelperService.saveNewAnalyticsRecords(records));
		verify(analyticsRepository, never()).saveAll(any());
	}

	@Test
	void findById_WithValidId_ShouldReturnRecord() {
		Long id = 1L;
		Analytics analytics = new Analytics();
		when(analyticsRepository.findById(id)).thenReturn(Optional.of(analytics));

		Analytics result = AnalyticsMapper.toEntity(analyticsHelperService.findById(id));

		assertNotNull(result);
		assertEquals(analytics, result);
	}

	@Test
	void findById_WithInvalidId_ShouldThrowException() {
		Long id = 999L;
		when(analyticsRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(CustomGlobalErrorHandling.ResourceNotFoundException.class,
				() -> analyticsHelperService.findById(id));
	}

	@Test
	void findAnalyticsByNameAndLevel_WithFilters_ShouldReturnFilteredRecords() {
		String name = "Glucose";
		String level = "Normal";
		Pageable pageable = PageRequest.of(0, 10);
		List<Analytics> expectedRecords = createSampleRecordList().stream()
				.filter(r -> r.name().equals(name) && r.level().equals(level)).toList()
				.stream().map(AnalyticsMapper::toEntity).toList();

		when(analyticsRepository.findAllByNameAndLevel(pageable, name, level))
				.thenReturn(expectedRecords);

		List<AnalyticsRecord> result =
				analyticsHelperService.findAnalyticsByNameAndLevel(pageable, name, level);

		assertEquals(expectedRecords.size(), result.size());
		verify(analyticsRepository).findAllByNameAndLevel(pageable, name, level);
	}

	@Test
	void findAllAnalyticsByNameAndLevelAndDate_WithDateRange_ShouldReturnFilteredRecords() {
		String name = "Glucose";
		String level = "Normal";
		LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);
		List<Analytics> expectedRecords = createDateRangeRecords().stream().map(AnalyticsMapper::toEntity).toList();

		when(analyticsRepository.findAllByNameAndLevelAndDateBetween(eq(name), eq(level),
				eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(expectedRecords);

		List<AnalyticsRecord> result = analyticsHelperService
				.findAllAnalyticsByNameAndLevelAndDate(name, level, startDate, endDate);

		assertNotNull(result);
		assertEquals(expectedRecords.size(), result.size());
	}

	@Test
	void deleteAnalyticsById_WithValidId_ShouldDelete() {
		Long id = 1L;
		when(analyticsRepository.existsById(id)).thenReturn(true);
		doNothing().when(analyticsRepository).deleteById(id);

		assertDoesNotThrow(() -> analyticsHelperService.deleteAnalyticsById(id));

		verify(analyticsRepository).deleteById(id);
	}

	@Test
	void deleteAnalyticsById_WithInvalidId_ShouldThrowException() {
		Long id = 999L;
		when(analyticsRepository.existsById(id)).thenReturn(false);

		assertThrows(CustomGlobalErrorHandling.ResourceNotFoundException.class,
				() -> analyticsHelperService.deleteAnalyticsById(id));
		verify(analyticsRepository, never()).deleteById(id);
	}

	@Test
	void ensureNameExists_WithValidName_ShouldNotThrowException() {
		String name = "Glucose";
		when(analyticsRepository.existsByName(name.toUpperCase())).thenReturn(true);

		assertDoesNotThrow(() -> analyticsHelperService.ensureNameExists(name));
	}

	@Test
	void ensureNameExists_WithInvalidName_ShouldThrowException() {
		String name = "NonExistentTest";
		when(analyticsRepository.existsByName(name.toUpperCase())).thenReturn(false);

		assertThrows(CustomGlobalErrorHandling.ResourceNotFoundException.class,
				() -> analyticsHelperService.ensureNameExists(name));
	}

	@Test
	void ensureNameNotExists_WithInvalidName_ShouldThrowException() {
		String name = "Glucose";
		when(analyticsRepository.existsByName(name.toUpperCase())).thenReturn(false);

		CustomGlobalErrorHandling.ResourceNotFoundException exception =
				assertThrows(CustomGlobalErrorHandling.ResourceNotFoundException.class,
						() -> analyticsHelperService.ensureNameExists(name));

		assertEquals("AnalyticsRecord by name not found", exception.getMessage());
	}

	@Test
	void isAnalyticsNonExistent_WithNonExistentRecord_ShouldReturnTrue() {
		AnalyticsRecord record = createSampleRecord();
		when(analyticsRepository.existsByDateAndLevelAndName(record.date(), record.level(),
				record.name())).thenReturn(false);

		boolean result = analyticsHelperService.isAnalyticsNonExistent(record);

		assertTrue(result);
	}

	@Test
	void isAnalyticsNonExistent_WithExistentRecord_ShouldReturnFalse() {
		AnalyticsRecord record = createSampleRecord();
		when(analyticsRepository.existsByDateAndLevelAndName(record.date(), record.level(),
				record.name())).thenReturn(true);

		boolean result = analyticsHelperService.isAnalyticsNonExistent(record);

		assertFalse(result);
	}

	@Test
	void findGroupedAnalyticsByLevel_WithValidInputs_ShouldReturnGroupedRecords() {
		String name = "Glucose";
		LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 1, 2, 0, 0);
		List<Analytics> records = createSampleRecordList().stream().map(AnalyticsMapper::toEntity).toList();

		when(analyticsRepository.findAllByNameAndDateBetweenGroupByLevel(eq(name),
				eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(records);

		List<GroupedValuesByLevel> result =
				analyticsHelperService.findGroupedAnalyticsByLevel(name, startDate, endDate);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		verify(analyticsRepository).findAllByNameAndDateBetweenGroupByLevel(eq(name),
				eq(startDate), eq(endDate), any(Pageable.class));
	}
}