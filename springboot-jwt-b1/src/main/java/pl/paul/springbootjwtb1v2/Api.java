package pl.paul.springbootjwtb1v2;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
public class Api {

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    UserRepo userRepo;

    public Api(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/hello")
    public String test1() {
        return "hello!";
    }

    @PostMapping("/generator")
    public String login(@RequestBody User user) {
        if (userRepo.findByLogin(user.getLogin()) == null)
            return "Incorrect login";

        if (!passwordEncoder().matches(user.getPassword(), userRepo.findByLogin(user.getLogin()).getPassword()))
            return "Incorrect password";

        String token = "";
        try {
            Algorithm algorithm = Algorithm.HMAC512("Kasia123");
            token = JWT.create()
                    .withSubject(user.getLogin())
                    .withClaim("roles", "ROLE_" + user.getRole())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 30000))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            return ("god damn it");
        }
        return token;

    }
}
