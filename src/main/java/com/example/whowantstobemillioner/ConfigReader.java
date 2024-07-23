package com.example.whowantstobemillioner;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class ConfigReader {
    //    public static DBConfig readConfig(String filePath) throws IOException {
//        Gson gson = new Gson();
//        try (FileReader reader = new FileReader(filePath)) {
//            return gson.fromJson(reader, DBConfig.class);
//        }
//    }
    public static DBConfig readConfig(String fileName) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(ConfigReader.class.getClassLoader().getResourceAsStream(fileName)))) {
            return gson.fromJson(reader, DBConfig.class);
        }
    }
}

