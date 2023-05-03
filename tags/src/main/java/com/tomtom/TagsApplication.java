package com.tomtom;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class TagsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TagsApplication.class, args);
    }


    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(
                new Info().title("Tags").description("REST-endpoints")
                        .contact((new Contact().email("orbis_search_sdp_alpha@groups.tomtom.com").name("Alpha")))
        );
    }

}
