package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.domain.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.record.user.UserRecord;
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
    public ResponseEntity<UserRecord> signUp
            (@Valid @RequestBody UserRecord UserRecord, UriComponentsBuilder uriComponentsBuilder) {
       var user = userService.signUp(UserRecord.username(), UserRecord.password(),
               UserRecord.email(), UserRoles.USER);
       var uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(UserRecord);
    }
    @Transactional
    @PatchMapping
    @RequestMapping(value = "/update/password", method = RequestMethod.PATCH)
    public ResponseEntity<?> updPassword
            (@Valid @RequestBody UserRecord userRecord, String newPass) {
                userService.updUser(userRecord.username(), userRecord.email(), userRecord.password(),
                        newPass, UserRoles.USER);
        return ResponseEntity.noContent().build();
    }
}
