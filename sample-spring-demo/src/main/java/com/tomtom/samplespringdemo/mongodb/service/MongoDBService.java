package com.tomtom.samplespringdemo.mongodb.service;

import com.tomtom.samplespringdemo.mongodb.repo.ExtendsPbfRepo;
import com.tomtom.sn.commons.model.delta.PBFModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MongoDBService {

    private final ExtendsPbfRepo extendsPbfRepo;

    public Map<String, Integer> runQuery(final String fileName, final boolean regexSearch, final int wordCount) {
        Map<String, Integer> result = new ConcurrentHashMap<>();
        try {
            final Path queryFile = ResourceUtils.getFile("classpath:" + fileName).toPath();
            Files.lines(queryFile).parallel().map(s -> exctractSearchQuery(s, wordCount)).forEach(s -> {
                final List<PBFModel> pbfModels = regexSearch ? extendsPbfRepo.findAllBySearchQueryEqualIgnoreCase(s) : extendsPbfRepo.findAllBySearchQuery(s);
                result.put(s, pbfModels.size());
            });
            final TreeMap<String, Integer> collect = result.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (aLong, aLong2) -> aLong2, TreeMap::new));
            System.out.println(collect);
            return collect;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    private String exctractSearchQuery(final String line, final int wordCount) {
        try {
            final String[] s = line.split(" ");
            return Arrays.stream(s).limit(wordCount).collect(Collectors.joining(" "));
        } catch (Throwable throwable) {
            return line;
        }
    }
}
