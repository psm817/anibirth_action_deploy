package com.cod.AniBirth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


public class XmlToJsonConverter {
    public static String convert(String xml) {
        try {
            // XML을 JSON으로 변환하기 위한 XmlMapper 생성
            XmlMapper xmlMapper = new XmlMapper();
            // XML 문자열을 JsonNode로 변환
            JsonNode node = xmlMapper.readTree(xml.getBytes());

            // JsonNode를 JSON 문자열로 변환하기 위한 ObjectMapper 생성
            ObjectMapper jsonMapper = new ObjectMapper();
            // JsonNode를 JSON 문자열로 변환
            return jsonMapper.writeValueAsString(node);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
