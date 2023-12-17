package leonardo.labutilities.qualitylabpro.controller;
import leonardo.labutilities.qualitylabpro.main.entitys.DefaultValues;
import leonardo.labutilities.qualitylabpro.main.entitys.Lot;
import leonardo.labutilities.qualitylabpro.records.defaultValues.DefaultRegisterDTO;
import leonardo.labutilities.qualitylabpro.records.lot.ValueOfLotDTO;
import leonardo.labutilities.qualitylabpro.repositories.DefaultValuesRepository;
import leonardo.labutilities.qualitylabpro.repositories.LotRepository;

import leonardo.labutilities.qualitylabpro.services.DefaultValuesService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
@Transactional
@ActiveProfiles("test")
class DefaultValuesControllerTest {
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Autowired
    private JacksonTester<DefaultRegisterDTO> authDataJacksonTesterDefaultRegister;

    @Autowired
    private JacksonTester<DefaultValues> authDataJacksonTesterDefaultValues;

    @MockBean
    private DefaultValuesRepository defaultValuesRepository;

    @Autowired
    DefaultValuesController defaultValuesController;

    @Autowired
    DefaultValuesService defaultValuesService;

    @Autowired
    LotRepository lotRepository;




    @Test
    @DisplayName("It should return the http code 400 when the information is invalid")
    void registerTest1() throws Exception {
        var response = mvc
                .perform(post("/defaultsValues/register"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    @DisplayName("Should return http code 201 when information is valid")
    void registerTest2() throws Exception {

        lotRepository.save(new Lot(new ValueOfLotDTO("abc123")));
        var data = new DefaultRegisterDTO
                ("albumin",0.2,3.4, 5.4, 0.3, 1L, 1L);
        DefaultValues defaultValues = new DefaultValues(data);

        when(defaultValuesRepository.save(any())).thenReturn(defaultValues);

        var response = mvc
                .perform(post("/defaultsValues/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authDataJacksonTesterDefaultRegister.write(data).getJson()))
                .andReturn().getResponse();

        var jsonOfResponse = authDataJacksonTesterDefaultValues.write(defaultValues).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonOfResponse);
    }
}