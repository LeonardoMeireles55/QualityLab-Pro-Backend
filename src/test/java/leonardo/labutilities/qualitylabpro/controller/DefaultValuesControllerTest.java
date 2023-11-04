package leonardo.labutilities.qualitylabpro.controller;
import leonardo.labutilities.qualitylabpro.analytics.DefaultValues;
import leonardo.labutilities.qualitylabpro.records.defaultvalues.DefaultRegister;
import leonardo.labutilities.qualitylabpro.repositories.DefaultValuesRepository;
import leonardo.labutilities.qualitylabpro.services.DefaultValuesService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
class DefaultValuesControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DefaultRegister> authDataJacksonTesterDefaultRegister;

    @Autowired
    private JacksonTester<DefaultValues> authDataJacksonTesterDefaultValues;

    @MockBean
    private DefaultValuesRepository defaultValuesRepository;

    @Autowired
    DefaultValuesController defaultValuesController;

    @Autowired
    DefaultValuesService defaultValuesService;


    @Test
    @DisplayName("It should return the http code 400 when the information is invalid")
    void registerTest1() throws Exception {
        var response = mvc
                .perform(post("/defaultsvalues/register"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    @DisplayName("Should return http code 201 when information is valid")
    void registerTest2() throws Exception {
        var data = new DefaultRegister("albumine",0.2,3.4, 5.4, 0.3);
        DefaultValues defaultValues = new DefaultValues(data);

        when(defaultValuesRepository.save(any())).thenReturn(defaultValues);

        var response = mvc
                .perform(post("/defaultsvalues/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authDataJacksonTesterDefaultRegister.write(data).getJson()))
                .andReturn().getResponse();

        var jsonOfResponse = authDataJacksonTesterDefaultValues.write(defaultValues).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonOfResponse);
    }
}