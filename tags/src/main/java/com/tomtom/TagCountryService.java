package com.tomtom;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TagCountryService {

    private final PBFRepository pbfRepository;

    private final TagExecutor executor;

    private static final int pageSize = 100000;

    public Set<String> getTagsFromCountry(String country) {

        final Set<String> tags = ConcurrentHashMap.newKeySet();
        final List<CompletableFuture<Void>> allJobs = new ArrayList<>();
        final long totalFeatures = pbfRepository.totalFeatures(country);
        final int totalPages = (int) (totalFeatures / pageSize + 1);
        log.info("Total features {} pages {} ", totalFeatures, totalPages);

        for (int pageNumber = 0; pageNumber < totalPages; pageNumber++) {
            final PageRequest page = PageRequest.of(pageNumber, pageSize);
            allJobs.add(executor.submit(() -> {
                final List<PBFModel> pbfModels = pbfRepository.findByCountry(country, page);
                final List<String> keys = pbfModels.stream().map(pbfModel -> {
                    final HashMap<String, Object> currentTags = (HashMap<String, Object>) pbfModel.getTags();
                    return currentTags.keySet();
                }).flatMap(Collection::stream).collect(Collectors.toList());
                tags.addAll(keys);
                return null;
            }));
        }
        executor.waitForCompletion(allJobs);
        tags.remove("noname");
        return tags;
    }
}
