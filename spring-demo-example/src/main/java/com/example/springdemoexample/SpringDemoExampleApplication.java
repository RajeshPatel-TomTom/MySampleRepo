package com.example.springdemoexample;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wololo.jts2geojson.GeoJSONReader;

@SpringBootApplication
public class SpringDemoExampleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        String json = "{\"coordinates\":[[-106.3057812,42.8416911],[-106.3057786,42.8420011]],\"type\":\"LineString\"}";
        String json = "{\"type\":\"MultiLineString\",\"coordinates\":[[[6.740127191170894,51.16147554838831],[6.739462640584596,51.16166582577008],[6.739441941605359,51.16166908603615],[6.739418036506426,51.16166800329164],[6.739395765162144,51.16166334563972],[6.739376692653773,51.16165557504054],[6.739134458922979,51.16152586939542]]]}";
        GeoJSONReader reader = new GeoJSONReader();
        Geometry geometry = reader.read(json);
        System.out.println(geometry.getGeometryType());
        for (Coordinate coordinate : geometry.getCoordinates()) {
            System.out.println(coordinate);
        }
    }
}
