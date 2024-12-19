package leonardo.labutilities.qualitylabpro.service.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import leonardo.labutilities.qualitylabpro.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    @Value("${api.security.issuer}")
    private static String ISSUER;

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getEmail())
                .withExpiresAt(dateExp())
                .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Generate token ERROR", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm).withIssuer(ISSUER).build().verify(tokenJWT).getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Invalid token " + exception);
        }
    }

    private Instant dateExp() {
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }
}
