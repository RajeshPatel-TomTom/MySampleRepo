package com.tomtom.samplespringdemo.mongodb.repo;

import com.tomtom.sn.commons.model.delta.PBFModel;
import com.tomtom.sn.dataaccess.repository.PBFRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtendsPbfRepo extends PBFRepository {
    @Query(value = "{'tags.name':?0}", collation = "{ locale: 'en', strength: 2 }")
    List<PBFModel> findAllBySearchQueryEqualIgnoreCase(final String query);

    @Query(value = "{'tags.name':?0}")
    List<PBFModel> findAllBySearchQuery(final String query);
}
