package org.carlRos.idempotency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class IdempotencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdempotencyApplication.class, args);
	}

}
