package com.tomtom.samplespringdemo;

import com.tomtom.samplespringdemo.mnr.MNRService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication(scanBasePackages = "com.tomtom")
@EnableFeignClients
@AllArgsConstructor
public class SampleSpringDemoApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(SampleSpringDemoApplication.class, args);
        final MNRService mnrService = context.getBean(MNRService.class);
        mnrService.executed();

//        final GssService gssService = run.getBean(GssService.class);
//        final Long aLong = gssService.maxRevisionId("USA");
//        System.out.println(aLong);

//        final MongoDBService extendsPbfRepo = run.getBean(MongoDBService.class);
//        extendsPbfRepo.runQuery("query_gbr.txt", true, 2);
//        final ExtendsPbfRepo extendsPbfRepo = run.getBean(ExtendsPbfRepo.class);
//        final ObjectMapper objectMapper = run.getBean(ObjectMapper.class);
//        final List<PBFModel> datas = extendsPbfRepo.findAllBySearchQueryEqualIgnoreCase("47th");
//        System.out.println(datas.size());
//        final long count = datas.stream().map(PBFModel::getTags).map(o -> objectMapper.convertValue(
//                o,
//                new TypeReference<Map<String, Object>>() {
//                }
//        )).filter(stringObjectMap -> stringObjectMap.getOrDefault("name", Strings.EMPTY).toString().equalsIgnoreCase("paseo rancho castilla")).count();
//        System.out.println(count);
        System.exit(0);
    }

}
