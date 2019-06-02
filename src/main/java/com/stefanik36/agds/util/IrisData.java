package com.stefanik36.agds.util;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import io.vavr.collection.List;

import java.io.FileReader;
import java.io.IOException;

public class IrisData {

    private List<List<Object>> data;

    public IrisData() {
        this.data = List.empty();
        try (CSVReader csvReader = new CSVReaderBuilder(
                new FileReader("src/main/resources/data/iris_data.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()
        ) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                data = data.append(List.of(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ? to 0 changed
     *
     * @return BreastCancerWisconsinData
     */
    public List<List<Object>> getData() {
        return data;
    }

}
