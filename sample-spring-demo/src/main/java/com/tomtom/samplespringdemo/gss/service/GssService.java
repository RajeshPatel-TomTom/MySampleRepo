package com.tomtom.samplespringdemo.gss.service;

import com.tomtom.orbis.Elements;
import com.tomtom.orbis.Id;
import com.tomtom.orbis.Tagging;
import com.tomtom.orbis.gss.model.ElementsRequestParams;
import com.tomtom.sn.commons.gss.GssApi;
import com.tomtom.sn.dataaccess.repository.PBFRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class GssService {
    private final GssApi gssApi;
    private final PBFRepository pbfRepository;

    public Long maxRevisionId(final String country) {
        long max = 0;
        int page = 0;
        while (true) {
            final PageRequest pageRequest = PageRequest.of(page++, 10000, Sort.by("updatedTimeStamp"));
            final List<Id> ids = pbfRepository.findByCountry(country, pageRequest).stream().map(s -> Id.newBuilder().setLayerId(2).setHigh(2).setLow(s.getBaseMapID()).build()).collect(Collectors.toList());
            if (ids.isEmpty()) break;
            final ElementsRequestParams requestParams = ElementsRequestParams.newBuilder().setLayerId(92324).addAllIds(ids).build();
            final Elements elements = gssApi.getElements(requestParams);
            final Long aLong = elements.getTaggingsList().stream().map(Tagging::getRevisionId).max(Long::compareTo).orElse(0l);
            max = Math.max(aLong, max);
        }
        return max;
    }
}
