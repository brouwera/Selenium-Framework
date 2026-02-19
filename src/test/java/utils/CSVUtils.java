package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {

    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // skip header row
                    continue;
                }

                data.add(line.split(","));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        }

        return data;
    }
}