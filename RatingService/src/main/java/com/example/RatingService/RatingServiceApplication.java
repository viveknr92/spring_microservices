package com.example.RatingService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
public class RatingServiceApplication {

	@GetMapping
	public String home() {
		return "RatingService";
	}

	@GetMapping("/{userId}")
	public UserRating getUserRatings(@PathVariable("userId") String userId) {
		UserRating userRating = new UserRating();
		userRating.setRating(Arrays.asList(
				new Rating("505", 4),
				new Rating("506", 3)
		));
		userRating.setUserId(userId);
		return userRating;
	}

	public static void main(String[] args) {
		SpringApplication.run(RatingServiceApplication.class, args);
	}

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
class UserRating {
	private String userId;
	private List<Rating> rating;
}

