package leonardo.labutilities.qualitylabpro.controllers;

import leonardo.labutilities.qualitylabpro.configs.TestSecurityConfig;
import leonardo.labutilities.qualitylabpro.controllers.analytics.CoagulationAnalyticsController;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStdDeviationRecord;
import leonardo.labutilities.qualitylabpro.repositories.UserRepository;
import leonardo.labutilities.qualitylabpro.services.analytics.CoagulationAnalyticsService;
import leonardo.labutilities.qualitylabpro.services.authentication.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static leonardo.labutilities.qualitylabpro.utils.AnalyticsHelperMocks.createSampleRecordList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoagulationAnalyticsController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CoagulationAnalyticsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TokenService tokenService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private CoagulationAnalyticsService coagulationAnalyticsService;

	@Autowired
	private JacksonTester<List<GenericValuesRecord>> jacksonGenericValuesRecord;

	@BeforeEach
	public void setup() {
		doNothing().when(coagulationAnalyticsService).saveNewAnalyticsRecords(anyList());
	}

	@Test
	@DisplayName("It should return HTTP code 201 when analytics records are saved")
	void analytics_post_return_201() throws Exception {
		List<GenericValuesRecord> records = createSampleRecordList();
		mockMvc.perform(post("/coagulation-analytics").contentType(MediaType.APPLICATION_JSON)
				.content(jacksonGenericValuesRecord.write(records).getJson()))
				.andExpect(status().isCreated());
		verify(coagulationAnalyticsService, times(1)).saveNewAnalyticsRecords(anyList());
	}

	@Test
	@DisplayName("It should return a list of all analytics with pagination")
	void getAllAnalytics_return_list() throws Exception {
		List<GenericValuesRecord> records = createSampleRecordList();
		when(coagulationAnalyticsService.getAllByNameIn(anyList(), any())).thenReturn(records);

		mockMvc.perform(get("/coagulation-analytics").param("page", "0").param("size", "10"))
				.andExpect(status().isOk()).andExpect(result -> {
					// Verify the content of the response if necessary
					// Example: assert the list size
				});

		verify(coagulationAnalyticsService, times(1)).getAllByNameIn(anyList(), any());
	}

	@Test
	@DisplayName("It should return analytics records by level and name")
	void getAnalyticsByLevel_return_analytics() throws Exception {
		List<GenericValuesRecord> records = createSampleRecordList();
		when(coagulationAnalyticsService.findAnalyticsByNameAndLevel(any(), any(), any()))
				.thenReturn(records);

		mockMvc.perform(get("/coagulation-analytics/name-and-level").param("name", "Glucose")
				.param("level", "Normal").param("page", "0").param("size", "10"))
				.andExpect(status().isOk());

		verify(coagulationAnalyticsService, times(1)).findAnalyticsByNameAndLevel(any(), any(),
				any());
	}

	@Test
	@DisplayName("It should return analytics records for a date range")
	void getAnalyticsByDateRange_return_analytics() throws Exception {
		List<GenericValuesRecord> records = createSampleRecordList();

		when(coagulationAnalyticsService.getAllByNameInAndDateBetween(anyList(), any(), any()))
				.thenReturn(records);

		mockMvc.perform(get("/coagulation-analytics/date-range")
				.param("startDate", "2025-01-01 00:00:00").param("endDate", "2025-01-05 00:00:00"))
				.andExpect(status().isOk());

		verify(coagulationAnalyticsService, times(1)).getAllByNameInAndDateBetween(anyList(), any(),
				any());
	}

	@Test
	@DisplayName("It should return mean and standard deviation for a date range")
	void getMeanAndStandardDeviation_return_result() throws Exception {
		MeanAndStdDeviationRecord result = new MeanAndStdDeviationRecord(10.5, 2.3);
		when(coagulationAnalyticsService.calculateMeanAndStandardDeviation(any(), any(), any(),
				any())).thenReturn(result);

		mockMvc.perform(get("/coagulation-analytics/mean-standard-deviation")
				.param("name", "Hemoglobin").param("level", "High")
				.param("startDate", "2025-01-01 00:00:00").param("endDate", "2025-01-05 00:00:00"))
				.andExpect(status().isOk());

		verify(coagulationAnalyticsService, times(1)).calculateMeanAndStandardDeviation(any(),
				any(), any(), any());
	}
}
