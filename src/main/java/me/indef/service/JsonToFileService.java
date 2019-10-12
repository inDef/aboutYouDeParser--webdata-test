package me.indef.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonToFileService {

    private static final String SEP = System.getProperty("file.separator");
    private static final File DIR = new File(System.getProperty("user.dir") + SEP + "files");

    public static void writeToFile(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();

        if (!DIR.exists()) {
            DIR.mkdir();
        }

        try {
            objectMapper.writeValue(new File(DIR + SEP + "products.json"), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
