package leonardo.labutilities.qualitylabpro.controllers;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStdDeviationRecord;
import leonardo.labutilities.qualitylabpro.services.analytics.BiochemistryAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static leonardo.labutilities.qualitylabpro.utils.AnalyticsHelperMocks.createSampleRecordList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class BiochemistryAnalyticsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BiochemistryAnalyticsService biochemistryAnalyticsService;

    @Autowired
    private JacksonTester<List<GenericValuesRecord>> jacksonGenericValuesRecord;

    @BeforeEach
    public void setup() {
        doNothing().when(biochemistryAnalyticsService).saveNewAnalyticsRecords(anyList());
    }

    @Test
    @DisplayName("It should return HTTP code 201 when analytics records are saved")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void analytics_post_return_201() throws Exception {
        List<GenericValuesRecord> records = createSampleRecordList();
        mockMvc.perform(post("/biochemistry-analytics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonGenericValuesRecord.write(records).getJson()))
                .andExpect(status().isCreated());
        verify(biochemistryAnalyticsService, times(1)).saveNewAnalyticsRecords(anyList());
    }

    @Test
    @DisplayName("It should return a list of all analytics with pagination")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllAnalytics_return_list() throws Exception {
        List<GenericValuesRecord> records = createSampleRecordList();
        when(biochemistryAnalyticsService.getAllByNameIn(anyList(), any()))
                .thenReturn(records);

        mockMvc.perform(get("/biochemistry-analytics")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    // Verify the content of the response if necessary
                    // Example: assert the list size
                });

        verify(biochemistryAnalyticsService, times(1)).getAllByNameIn(anyList(), any());
    }

    @Test
    @DisplayName("It should return analytics records by level and name")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAnalyticsByLevel_return_analytics() throws Exception {
        List<GenericValuesRecord> records = createSampleRecordList();
        when(biochemistryAnalyticsService.findAnalyticsByNameAndLevel(any(), any(), any()))
                .thenReturn(records);

        mockMvc.perform(get("/biochemistry-analytics/results/search/name/level")
                        .param("name", "Glucose")
                        .param("level", "Normal")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(biochemistryAnalyticsService, times(1)).findAnalyticsByNameAndLevel(any(), any(), any());
    }

    @Test
    @DisplayName("It should return analytics records for a date range")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAnalyticsByDateRange_return_analytics() throws Exception {
        List<GenericValuesRecord> records = createSampleRecordList();

        when(biochemistryAnalyticsService.getAllByNameInAndDateBetween(anyList(), any(), any()))
                .thenReturn(records);

        mockMvc.perform(get("/biochemistry-analytics/results/names/date-range")
                        .param("startDate", "2025-01-01 00:00:00")
                        .param("endDate", "2025-01-05 00:00:00"))
                .andExpect(status().isOk());

        verify(biochemistryAnalyticsService, times(1)).getAllByNameInAndDateBetween(anyList(), any(), any());
    }

    @Test
    @DisplayName("It should return mean and standard deviation for a date range")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getMeanAndStandardDeviation_return_result() throws Exception {
        MeanAndStdDeviationRecord result = new MeanAndStdDeviationRecord(10.5, 2.3);
        when(biochemistryAnalyticsService.calculateMeanAndStandardDeviation(any(), any(), any(), any()))
                .thenReturn(result);

        mockMvc.perform(get("/biochemistry-analytics/results/mean-standard-deviation")
                        .param("name", "Hemoglobin")
                        .param("level", "High")
                        .param("startDate", "2025-01-01 00:00:00")
                        .param("endDate", "2025-01-05 00:00:00"))
                .andExpect(status().isOk());

        verify(biochemistryAnalyticsService, times(1)).calculateMeanAndStandardDeviation(any(), any(), any(), any());
    }
}
