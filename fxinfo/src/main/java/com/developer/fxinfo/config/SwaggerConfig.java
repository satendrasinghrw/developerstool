package com.developer.fxinfo.config;

import io.swagger.annotations.ApiOperation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@SuppressWarnings("deprecation")
	public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
		      "FX Spot Rate API", "", "0.1",
		      "urn:tos", "", 
		      "0.1", "");
	
	@Bean
	@ApiOperation(value = "FX Spot Rate")
	public Docket fxinfoAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(DEFAULT_API_INFO)
			.select()
			.apis(RequestHandlerSelectors.any())
           .paths(PathSelectors.regex("/fx.*"))
          .build();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public WebMvcConfigurer corsConfigurer() {
	   return new WebMvcConfigurerAdapter() {
	      @Override
	      public void addCorsMappings(CorsRegistry registry) {
	         registry.addMapping("/**").allowedOrigins("http://192.168.10.104:5502");

	      }
	   };
	}
	
	@Bean
	public CorsFilter corsFilter() {
	   UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	   CorsConfiguration config = new CorsConfiguration();
	   config.setAllowCredentials(true);
	   config.addAllowedOrigin("http://192.168.10.104:5502");
	   config.addAllowedHeader("*");
	   config.addAllowedMethod("GET");
	   config.addAllowedMethod("POST");
	   source.registerCorsConfiguration("/**", config);
	   return new CorsFilter(source);
	}
	
}
