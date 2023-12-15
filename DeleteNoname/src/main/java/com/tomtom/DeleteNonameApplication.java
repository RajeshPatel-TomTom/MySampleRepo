package com.tomtom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class DeleteNonameApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DeleteNonameApplication.class, args);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://10.137.9.245/sdp-sn-analysis-recovery/v1/roadelements")
                .defaultCookie("cookie-name", "cookie-value")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public void run(final String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Please provide the path of the file to be deleted");
            return;
        }

        final AtomicInteger counts = new AtomicInteger();
        final Path noNameFile = Path.of(args[0]);
        final int maxPageSize = args.length > 1 ? Integer.parseInt(args[1]) : 100000;
        if (!Files.exists(noNameFile)) {
            System.out.println("File does not exist");
            return;
        }
        try (Stream<String> lines = Files.lines(noNameFile)) {
            lines.skip(1).collect(Collectors.groupingBy(s -> counts.getAndIncrement() / maxPageSize)).forEach((index, nonameBaseMapIds) -> {
                System.out.println("processing " + index + " " + nonameBaseMapIds.size());
                final String body = "{\"schema\":\"full\",\"states\":[\"full\"],\"basemapIds\":[" + nonameBaseMapIds.stream().collect(Collectors.joining(",")) + "],\"orbisIds\":[],\"startPage\":0,\"endPage\":0,\"operation\":\"DELETE\"}";
                webClient().post().bodyValue(body).retrieve().bodyToMono(String.class).block();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }
}
