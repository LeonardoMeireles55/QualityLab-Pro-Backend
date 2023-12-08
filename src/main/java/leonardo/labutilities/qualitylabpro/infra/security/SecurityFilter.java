package leonardo.labutilities.qualitylabpro.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leonardo.labutilities.qualitylabpro.repositories.UserRepository;
import leonardo.labutilities.qualitylabpro.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService  tokenService;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            var tokenJWT = getToken(request);
            if (tokenJWT != null) {
                var subject = tokenService.getSubject(tokenJWT);
                var users = userRepository.findByUsername(subject);
                var authentication = new UsernamePasswordAuthenticationToken(users, null, users.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(exception.getLocalizedMessage());
        }
    }
    private String getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if(authHeader != null) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}