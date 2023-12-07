package leonardo.labutilities.qualitylabpro.controller;

import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.main.User;
import leonardo.labutilities.qualitylabpro.records.auth.AuthDataDTO;
import leonardo.labutilities.qualitylabpro.records.auth.TokenJwtDTO;
import leonardo.labutilities.qualitylabpro.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/login")
public class AuthController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    @PostMapping
    public ResponseEntity<?> singIn(@RequestBody @Valid AuthDataDTO authDataDTO) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(authDataDTO.login(), authDataDTO.password());
            var auth =  manager.authenticate(authToken);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new TokenJwtDTO(token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e + "User or Password is invalid");
        }

    }
}
