package leonardo.labutilities.qualitylabpro.controllers.users;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.dtos.authentication.LoginUserRecord;
import leonardo.labutilities.qualitylabpro.dtos.authentication.TokenJwtRecord;
import leonardo.labutilities.qualitylabpro.dtos.users.RecoverPasswordRecord;
import leonardo.labutilities.qualitylabpro.dtos.users.UpdatePasswordRecord;
import leonardo.labutilities.qualitylabpro.dtos.users.UsersRecord;
import leonardo.labutilities.qualitylabpro.entities.User;
import leonardo.labutilities.qualitylabpro.services.authentication.TokenService;
import leonardo.labutilities.qualitylabpro.services.users.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@SecurityRequirement(name = "bearer-key")
@RequestMapping("/users")
@RestController
public class UsersController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UsersController(UserService userService, AuthenticationManager authenticationManager,
                           TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Transactional
    @PatchMapping( "/password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRecord updatePasswordRecord) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            var user = (User) authentication.getPrincipal();
            userService
                    .updateUserPassword(user.getUsername(), user.getEmail(), updatePasswordRecord.oldPassword(),
                            updatePasswordRecord.newPassword());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @PostMapping("/password/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody UsersRecord usersRecord) {
        userService.recoverPassword(usersRecord.username(), usersRecord.email());
        return ResponseEntity.noContent().build();
    }
    @Transactional
    @PatchMapping("/password/recover")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody RecoverPasswordRecord recoverPasswordRecord) {
        userService
                .changePassword(recoverPasswordRecord.email(), recoverPasswordRecord.temporaryPassword(),
                        recoverPasswordRecord.newPassword());
        return ResponseEntity.noContent().build();
    }
    @Transactional
    @PostMapping("/sign-up")
    public ResponseEntity<UsersRecord> signUp(@Valid @RequestBody UsersRecord UsersRecord,
                                              UriComponentsBuilder uriComponentsBuilder) {
        var user = userService.signUp(UsersRecord.username(), UsersRecord.password(),
                UsersRecord.email());
        var uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(UsersRecord);
    }

    @PostMapping
    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public ResponseEntity<TokenJwtRecord> singIn(
            @RequestBody @Valid LoginUserRecord loginUserRecord) {
        var authToken = new UsernamePasswordAuthenticationToken(loginUserRecord.email(),
                loginUserRecord.password());
        var auth = authenticationManager.authenticate(authToken);
        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);

        return ResponseEntity.ok(new TokenJwtRecord(token));
    }


}
