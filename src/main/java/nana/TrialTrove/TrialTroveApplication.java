package nana.TrialTrove;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TrialTroveApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrialTroveApplication.class, args);
	}

}
