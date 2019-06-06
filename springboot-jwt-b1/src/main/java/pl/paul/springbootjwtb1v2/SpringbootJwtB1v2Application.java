package pl.paul.springbootjwtb1v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringbootJwtB1v2Application {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public SpringbootJwtB1v2Application(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJwtB1v2Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        User adminUser = new User("admin",passwordEncoder.encode("admin"),"ADMIN");
        User defaultUser = new User("user",passwordEncoder.encode("user"),"USER");

        userRepo.save(adminUser);
        userRepo.save(defaultUser);
    }

}
