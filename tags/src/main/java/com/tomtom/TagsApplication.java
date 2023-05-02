package com.tomtom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Set;

@SpringBootApplication

public class TagsApplication {

	public static void main(String[] args) {
		final ConfigurableApplicationContext run = SpringApplication.run(TagsApplication.class, args);
		final TagCountryService tagCountryService = run.getBean(TagCountryService.class);
		final Set<String> tagsFromCountry = tagCountryService.getTagsFromCountry("GBR");
	}

}
