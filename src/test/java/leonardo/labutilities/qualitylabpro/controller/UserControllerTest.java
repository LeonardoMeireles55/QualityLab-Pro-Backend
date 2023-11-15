package leonardo.labutilities.qualitylabpro.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import leonardo.labutilities.qualitylabpro.main.User;
import leonardo.labutilities.qualitylabpro.main.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.records.auth.AuthDataDTO;
import leonardo.labutilities.qualitylabpro.repositories.UserRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<AuthDataDTO> authDataJacksonTester;

    @MockBean
    private UserRepository repository;

    @Test
    @DisplayName("It should return the http code 400 when the information is invalid")
    @WithMockUser
    void register_scenario1() throws Exception {
        var response = mvc
                .perform(post("/user/signup"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    @DisplayName("Should return http code 201 when information is valid")
    @WithMockUser
    void register_scenario2() throws Exception {
        var authData = new AuthDataDTO("Pharmacist",
                "249195@", "leonardo@email.com", UserRoles.USER);

        when(repository.save(any())).thenReturn(new User(authData.login(), authData.password()));

        var response = mvc
                .perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authDataJacksonTester.write(authData).getJson()))
                .andReturn().getResponse();

        var jsonOfResponse = authDataJacksonTester.write(authData).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
         assertThat(response.getContentAsString()).isEqualTo(jsonOfResponse);
    }
}