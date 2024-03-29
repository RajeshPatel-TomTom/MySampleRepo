package com.tomtom;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("v1")
@Slf4j
@AllArgsConstructor
public class TagCountryController {

    private final TagCountryService tagCountryService;

    @PostMapping("tags/country/{country}")
    public Set<String> getTagsFromCountry(@PathVariable String country) {
        log.info("Got request for country {} ", country);
        return tagCountryService.getTagsFromCountry(country.toUpperCase());
    }
}
