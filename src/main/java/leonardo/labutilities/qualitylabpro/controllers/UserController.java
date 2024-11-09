package leonardo.labutilities.qualitylabpro.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.domain.entities.User;
import leonardo.labutilities.qualitylabpro.domain.enums.UserRoles;
import leonardo.labutilities.qualitylabpro.records.auth.AuthDataRecord;
import leonardo.labutilities.qualitylabpro.records.auth.TokenJwtRecord;
import leonardo.labutilities.qualitylabpro.records.user.UserRecord;
import leonardo.labutilities.qualitylabpro.services.TokenService;
import leonardo.labutilities.qualitylabpro.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserController
            (UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

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
    public ResponseEntity<TokenJwtRecord> singIn(@RequestBody @Valid AuthDataRecord authDataRecord) {
            var authToken = new UsernamePasswordAuthenticationToken(authDataRecord.username(),
                    authDataRecord.password());
            var auth = authenticationManager.authenticate(authToken);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new TokenJwtRecord(token));
    }

    @Transactional
    @PatchMapping
    @RequestMapping(value = "/update/password", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updPassword(@Valid @RequestBody UserRecord userRecord, String newPass) {
        userService.updUser(userRecord.username(), userRecord.email(), userRecord.password(),
                newPass, UserRoles.USER);
        return ResponseEntity.noContent().build();
    }
}
