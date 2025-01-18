package service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumeServiceApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}


	@Bean
	public CommandLineRunner commandLineRunner(RestTemplate restTemplate) {
		return args -> {
				var quotes  = restTemplate.getForObject("http://localhost:8080/quotes/", Quote[].class);
				for (Quote quote : quotes) {
					System.out.println(quote);
				}
		};
	}


}

	@JsonIgnoreProperties(ignoreUnknown = true)
	record Quotes(String type, Quote quote){}

	@JsonIgnoreProperties(ignoreUnknown = true)
	record Quote(Integer id, String quote) {

	}




