package pl.paul.springbootjwtauth2b1;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

public class JwtFilter extends BasicAuthenticationFilter {

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        RestTemplate restTemplate = new RestTemplate();

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);

        ResponseEntity<String> resp = restTemplate.exchange("http://localhost:8080/generator", HttpMethod.POST, entity, String.class);
        String header = resp.getBody();
        UsernamePasswordAuthenticationToken token = getUsernamePasswordAuthenticationToken(header);
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String header) {
        Algorithm algorithm = Algorithm.HMAC512("Kasia123");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(header);
        String username = jwt.getClaim("login").asString();
        String roles = jwt.getClaim("roles").asString();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roles);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(simpleGrantedAuthority));
        return usernamePasswordAuthenticationToken;
    }
}
