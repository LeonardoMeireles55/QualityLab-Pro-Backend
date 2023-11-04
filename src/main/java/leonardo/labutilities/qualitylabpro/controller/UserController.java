package leonardo.labutilities.qualitylabpro.controller;

import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.records.auth.AuthData;
import leonardo.labutilities.qualitylabpro.repositories.UserRepository;
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
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<AuthData> signUp
            (@Valid @RequestBody AuthData authData, UriComponentsBuilder uriComponentsBuilder) {
       var user = userService.signUp(authData.login(), authData.password(), authData.roles());
       var uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(authData);
    }
}
