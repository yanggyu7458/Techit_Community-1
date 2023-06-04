package likelion15.mutsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
public class MutsaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutsaApplication.class, args);
    }

}
