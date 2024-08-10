package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.domain.entities.User;
import leonardo.labutilities.qualitylabpro.domain.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.record.auth.AuthDataRecord;
import leonardo.labutilities.qualitylabpro.record.auth.TokenJwtRecord;
import leonardo.labutilities.qualitylabpro.record.user.UserRecord;
import leonardo.labutilities.qualitylabpro.services.TokenService;
import leonardo.labutilities.qualitylabpro.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    @PostMapping
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<UserRecord> signUp(@Valid @RequestBody UserRecord UserRecord,
            UriComponentsBuilder uriComponentsBuilder) {
        var user = userService.signUp(UserRecord.username(), UserRecord.password(),
                UserRecord.email(), UserRoles.USER);
        var uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(UserRecord);
    }

    @PostMapping
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public ResponseEntity<?> singIn(@RequestBody @Valid AuthDataRecord authDataRecord) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(authDataRecord.username(),
                    authDataRecord.password());
            var auth = authenticationManager.authenticate(authToken);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new TokenJwtRecord(token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e + "User or Password is invalid");
        }
    }

    @Transactional
    @PatchMapping
    @RequestMapping(value = "/update/password", method = RequestMethod.PATCH)
    public ResponseEntity<?> updPassword(@Valid @RequestBody UserRecord userRecord, String newPass) {
        userService.updUser(userRecord.username(), userRecord.email(), userRecord.password(),
                newPass, UserRoles.USER);
        return ResponseEntity.noContent().build();
    }
}
