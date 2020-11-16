package com.example.MovieCatalogService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@EnableEurekaClient
@EnableCircuitBreaker
@EnableHystrixDashboard
public class MovieCatalogServiceApplication {
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
//
//	@Autowired
//	private RestTemplate restTemplate;

	@Autowired
	private CatalogService catalogService;

	@GetMapping
	public String home() {
		return "MovieCatalogService";
	}

	@GetMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		UserRating userRating = catalogService.getUserRating(userId);
		System.out.println(userRating);
		return userRating.getRating().stream().map(rating -> catalogService.getCatalogItem(rating)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserRating {
	private String userId;
	private List<Rating> rating;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CatalogItem {
	private String movieName;
	private String movieDesc;
	private int rating;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Rating {
	private String movieId;
	private int rating;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Movie {
	private String movieId;
	private String movieName;
	private String movieDesc;
}
