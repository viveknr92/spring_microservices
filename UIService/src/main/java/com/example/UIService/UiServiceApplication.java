package com.example.UIService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@Controller
@EnableOAuth2Sso
public class UiServiceApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/")
				.permitAll()
				.anyRequest()
				.authenticated();
		http.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				.invalidateHttpSession(true)
				.deleteCookies("KSESSION")
				.deleteCookies("JSESSION");
	}

	@RequestMapping(value = "/")
	public String loadPublicUI() {
		return "home";
	}

	@RequestMapping(value = "/secure")
	public String loadSecureUI() {
		return "secure";
	}

	@RequestMapping(value = "/getCatalogItems")
	public ResponseEntity<CatalogItem[]> getCatalogItems() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", getAccessToken());
		HttpEntity<CatalogItem> catalogItemHttpEntity = new HttpEntity<>(httpHeaders);
		try {
			ResponseEntity<CatalogItem[]> responseEntity = restTemplate
					.exchange("http://MovieCatalogService/1", HttpMethod.GET, catalogItemHttpEntity, CatalogItem[].class);
			return responseEntity;
		} catch (HttpStatusCodeException e) {
			ResponseEntity responseEntity = ResponseEntity
					.status(e.getStatusCode())
					.headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
			return responseEntity;
		}
	}

	@RequestMapping(value = "/getMovieInfo")
	public ResponseEntity<?> getMovieInfo() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", getAccessToken());
		httpHeaders.add("someheader", "vivek");
		HttpEntity<Movie> movieHttpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<Movie> responseEntity = restTemplate
				.exchange("http://MovieInfoService/2", HttpMethod.GET, movieHttpEntity, Movie.class);
		return responseEntity;
	}

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static String getAccessToken() {
		OAuth2AuthenticationDetails auth2AuthenticationDetails =
				(OAuth2AuthenticationDetails) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getDetails();
		return auth2AuthenticationDetails.getTokenType().concat(" ")
				.concat(auth2AuthenticationDetails.getTokenValue());
	}


	public static void main(String[] args) {
		SpringApplication.run(UiServiceApplication.class, args);
	}

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
class Movie {
	private String movieId;
	private String movieName;
	private String movieDesc;
}
