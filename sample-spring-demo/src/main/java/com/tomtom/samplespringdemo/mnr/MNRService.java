package com.tomtom.samplespringdemo.mnr;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class MNRService {
    private JdbcTemplate jdbcTemplate;

    public Set<String> getNames() {
        final Set<String> names = new HashSet<>();
        try {
            final CSVReader csvReader = new CSVReaderBuilder(new FileReader(Paths.get("FRA_UnmatchedName_In_Orbis.csv").toFile())).withSkipLines(1).build();
            final List<String[]> rows = csvReader.readAll();
            System.out.println("Rows : " + rows.size());
            for (String[] row : rows) {
                names.add(row[0]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return names;
    }

    public void executed() {
        final Set<String> names = getNames();
        final String eol = System.getProperty("line.separator");
        try (Writer writer = new FileWriter("somefile.csv")) {
            names.stream().forEach(name -> {
                try {
                    writer.append(name).append(';').append(getWktGeometry(name)).append(eol);
                    writer.flush();
                } catch (Exception ex) {
                    log.warn("Error in get wkt of name {} :: Error {}", name, ex.getMessage());
                }
            });
        } catch (IOException e) {
            log.warn("Error in {} ", e.getMessage());
        }
    }

    public String getWktGeometry(final String name) {
        final String sql = "select ST_astext(grl.geom) as road_xy from _2023_09_001_eur_fra_fra.mnr_netw_geo_link as grl inner join _2023_09_001_eur_fra_fra.mnr_netw_route_link as nrl on nrl.feat_id = grl.feat_id inner join _2023_09_001_eur_fra_fra.mnr_netw2nameset as n2n on nrl.feat_id = n2n.netw_id inner join _2023_09_001_eur_fra_fra.mnr_nameset2name as nn using(nameset_id) inner join _2023_09_001_eur_fra_fra.mnr_name on nn.name_id in ( mnr_name.name_id, mnr_name.original_name_id ) inner join _2023_09_001_eur_fra_fra.mnr_netw2admin_area as na on nrl.feat_id = na.netw_id where ( n2n.nt_standard is true or n2n.nt_alternate is true ) and not ( n2n.nt_tourist_national or n2n.nt_tourist_regional or n2n.nt_tourist_nature or n2n.nt_tourist_cultural ) and mnr_name.name = ? and na.feat_type = '1111' limit 1";
        return jdbcTemplate.queryForObject(sql, String.class, new Object[]{name});
    }
}
