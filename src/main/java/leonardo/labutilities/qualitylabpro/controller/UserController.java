package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.records.UserDTO;
import leonardo.labutilities.qualitylabpro.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Transactional
    @PostMapping
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<leonardo.labutilities.qualitylabpro.records.UserDTO> signUp
            (@Valid @RequestBody leonardo.labutilities.qualitylabpro.records.UserDTO UserDTO, UriComponentsBuilder uriComponentsBuilder) {
       var user = userService.signUp(UserDTO.username(), UserDTO.password(),
               UserDTO.email(), UserRoles.USER);
       var uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(UserDTO);
    }
    @Transactional
    @PatchMapping
    @RequestMapping(value = "/update/password", method = RequestMethod.PATCH)
    public ResponseEntity<?> updPassword
            (@Valid @RequestBody UserDTO userDTO, String newPass) {
                userService.updUser(userDTO.username(), userDTO.email(), userDTO.password(),
                        newPass, UserRoles.USER);
        return ResponseEntity.noContent().build();
    }
}
