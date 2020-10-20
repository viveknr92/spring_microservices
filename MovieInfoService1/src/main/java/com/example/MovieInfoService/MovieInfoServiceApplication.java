package com.example.MovieInfoService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class MovieInfoServiceApplication {

	static int count = 0;
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping
	public String home() {
		return "MovieInfoService";
	}

	@Value("${services.url}")
    String movieDbUrl;

	@GetMapping("/{movieId}")
	public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
		System.out.println("8084 port - Movie Info Service" + count);
		count++;
		String apiKey = "24d0896bacfa5a3ada2ff0882c57f997";
		MovieSummary movieSummary = restTemplate.getForObject(movieDbUrl + movieId + "?api_key=" + apiKey, MovieSummary.class);
		return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieInfoServiceApplication.class, args);
	}

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Movie {
	private String movieId;
	private String movieName;
	private String movieDesc;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class MovieSummary {
	private String id;
	private String title;
	private String overview;
}