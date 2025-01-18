package quotes.api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.stream.Stream;

@SpringBootApplication
public class QuotesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuotesServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(QuotesRepository repository) {
		return args -> {
			Stream.of("some text1", "some text2", "some text 3", "some text4", "quote five")
					.forEach(quote -> {
						repository.save(new Quotes(quote));
					});
		};
	}

}


@Controller
@RequestMapping("/quotes")
class QuotesController {
	private final QuotesRepository quotesRepository;


	public QuotesController(QuotesRepository quotesRepository) {
		this.quotesRepository = quotesRepository;
	}

	@GetMapping("/")
	@ResponseBody
	public Collection<Quotes> all() {
		return this.quotesRepository.findAll();
	}

	@GetMapping("/{id}")
	@ResponseBody
	public Quotes getOne(@PathVariable Integer id) {
		return this.quotesRepository.findById(id).get();
	}

}

@RestResource
interface QuotesRepository extends ListCrudRepository<Quotes, Integer> {
}


@Entity
@Table(name = "quotes")
class Quotes {

	@Id
	@GeneratedValue
	Integer id;

	String quote;

	public Quotes() {
	}

	public Quotes(String quote) {
		this.quote = quote;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}
}