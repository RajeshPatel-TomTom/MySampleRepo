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
        return WebClient.builder().baseUrl("http://10.137.9.245/sdp-sn-analysis-recovery/v1/roadelements").defaultCookie("cookie-name", "cookie-value").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    @Override
    public void run(final String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Please provide the path of the file to be deleted");
            return;
        }

        final Path noNameFile = Path.of(args[0]);
        final int maxPageSize = args.length > 1 ? Integer.parseInt(args[1]) : 100000;
        if (!Files.exists(noNameFile)) {
            System.out.println("File does not exist");
            return;
        }
        try (Stream<String> lines = Files.lines(noNameFile).skip(1)) {
            final List<List<String>> lists = Lists.partition(lines.collect(Collectors.toSet()).stream().toList(), maxPageSize);
            System.out.println("Total number of pages to be deleted: " + lists.size());
            for (List<String> strings : lists) {
                final String body = "{\"schema\":\"full\",\"states\":[\"full\"],\"basemapIds\":[" + strings.stream().collect(Collectors.joining(",")) + "],\"orbisIds\":[],\"startPage\":0,\"endPage\":0,\"operation\":\"DELETE\"}";
                System.out.println(body);
                webClient().post().bodyValue(body).retrieve().bodyToMono(String.class).block();
                try {
                    Thread.sleep(100000);
                } catch (Exception e) {
                    System.out.println("Exception while sleeping " + e.getMessage());
                }
            }

        }

    }
}
