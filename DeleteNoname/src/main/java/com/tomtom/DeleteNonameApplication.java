package com.tomtom;

import com.google.common.collect.Lists;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class DeleteNonameApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DeleteNonameApplication.class, args);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://10.137.9.245/sdp-sn-analysis-recovery/v1/roadelements").defaultCookie("cookie-name", "cookie-value").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    @Override
    public void run(final String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Please provide the path of the file to be deleted");
            return;
        }

        final Path noNameFile = Path.of(args[0]);
        final int maxPageSize = 100000;
        final Operation operation = args.length > 1 ? Operation.valueOf(args[1]) : null;
        if (operation == null) {
            System.out.println("Please provide the operation to be performed");
            return;
        }
        if (!Files.exists(noNameFile)) {
            System.out.println("File does not exist");
            return;
        }
        try (Stream<String> lines = Files.lines(noNameFile).skip(1)) {
            final List<List<String>> lists = Lists.partition(lines.collect(Collectors.toSet()).stream().toList(), maxPageSize);
            System.out.println("Total number of pages to be deleted: " + lists.size());
            int count = 0;
            for (List<String> strings : lists) {
                System.out.println("Deleting page: " + count++);
                final String body = "{\"schema\":\"full\",\"states\":[\"full\"],\"basemapIds\":[" + strings.stream().collect(Collectors.joining(",")) + "],\"orbisIds\":[],\"startPage\":0,\"endPage\":0,\"operation\":\"" + operation + "\"}";
                webClient().post().bodyValue(body).retrieve().bodyToMono(String.class).block();
                try {
                    Thread.sleep(500000);
                } catch (Exception e) {
                    System.out.println("Exception while sleeping " + e.getMessage());
                }
            }
            System.out.println("Done deleting");

        }


    }

    enum Operation {
        DELETE, REFRESH, UNNAMED
    }
}
