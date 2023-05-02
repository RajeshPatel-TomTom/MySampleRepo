package com.tomtom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PBFRepository extends MongoRepository<PBFModel, String> {

    Optional<PBFModel> findByBaseMapID(Long baseMapID);

    @Query("{'metadata.country':?0}")
    public List<PBFModel> findByCountry(String country, PageRequest page);

    @Query(value = "{'metadata.country':?0}", count = true)
    public long totalFeatures(String country);

    @Query(value = "{'baseMapID':{'$in':?0}}")
    List<PBFModel> findAllByBaseMapID(List<Long> baseMapsIDs);
}
