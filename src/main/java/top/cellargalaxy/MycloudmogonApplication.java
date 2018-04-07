package top.cellargalaxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MycloudmogonApplication {

	public static void main(String[] args) {
		SpringApplication.run(MycloudmogonApplication.class, args);
	}
}
