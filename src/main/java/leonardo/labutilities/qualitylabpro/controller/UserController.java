package leonardo.labutilities.qualitylabpro.controller;

import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.records.auth.AuthDataDTO;
import leonardo.labutilities.qualitylabpro.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Transactional
    @PostMapping
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<AuthDataDTO> signUp
            (@Valid @RequestBody AuthDataDTO authDataDTO, UriComponentsBuilder uriComponentsBuilder) {
       var user = userService.signUp(authDataDTO.username(), authDataDTO.password(),
               authDataDTO.email(), UserRoles.USER);
       var uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(authDataDTO);
    }
    @Transactional
    @PatchMapping
    @RequestMapping(value = "/update/password", method = RequestMethod.PATCH)
    public ResponseEntity<?> updPassword
            (@Valid @RequestBody AuthDataDTO authDataDTO, String newPass) {
                userService.updUser(authDataDTO.username(), authDataDTO.email(), authDataDTO.password(),
                        newPass, UserRoles.USER);
        return ResponseEntity.noContent().build();
    }
}
