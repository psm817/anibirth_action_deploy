package com.cod.AniBirth;

import com.cod.AniBirth.animal.entity.Animal;
import com.cod.AniBirth.animal.service.AnimalService;
import com.cod.AniBirth.category.entity.Category;
import com.cod.AniBirth.category.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Component
public class ApiExplorer implements CommandLineRunner {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception{
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6300000/animalDaejeonService/animalDaejeonList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=no5Kx%2Bn4qn2kVYo3VLF%2BeQtRlsK9eQ%2FVDKn7dXtl%2BWWrs6j4Semu8NMnOUSdMGVcvp%2FdQx4IBt70JnXBHxwFSg%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*페이지당 게시물 수*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String xml = sb.toString();
        System.out.println("XML Response: " + xml);

        // XML을 JSON으로 변환
        String json = XmlToJsonConverter.convert(xml);
        System.out.println("JSON Response: " + json);

        // Save to database
        animalService.saveAnimals(json);
    }
    private void processJsonData(String json) {
        // JSON 데이터 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponse apiResponse;
        try {
            apiResponse = objectMapper.readValue(json, ApiResponse.class);
            List<Animal> animals = apiResponse.getMsgBody().getItems();

            for (Animal animal : animals) {
                // Classification 값으로 카테고리 설정
                setCategoryForAnimal(animal);

                // Animal 엔티티 저장
                animalService.saveAnimal(animal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setCategoryForAnimal(Animal animal) {
        // Classification 값을 사용하여 Category 엔티티를 찾거나 생성
        String classification = animal.getClassification();
        if (classification != null) {
            Category category = categoryRepository.findByName(classification);
            if (category == null) {
                // 필요한 경우 카테고리 생성 (옵션)
                category = new Category();
                category.setName(classification);
                category.setType("classification");
                categoryRepository.save(category);
            }
            animal.setCategory(category);
        }
    }
}
