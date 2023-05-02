package com.tomtom;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class TagCountryService {

    private final PBFRepository pbfRepository;

    private static final int pageSize = 100000;

    public Set<String> getTagsFromCountry(String country) {
        final Set<String> tags = new HashSet<>();
        final long totalFeatures = pbfRepository.totalFeatures(country);
        final int totalPages = (int) (totalFeatures / pageSize + 1);
        log.info("Total features {} pages {} ", totalFeatures, totalPages);

        for (int pageNumber = 0; pageNumber < totalPages; pageNumber++) {
            final PageRequest page = PageRequest.of(pageNumber, pageSize);
            final List<PBFModel> pbfModels = pbfRepository.findByCountry(country, page);
            pbfModels.forEach(pbfModel -> {
                final HashMap<String, Object> currentTags = (HashMap<String, Object>) pbfModel.getTags();
                tags.addAll(currentTags.keySet());
            });
        }
        return tags;
    }
}
