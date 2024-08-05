package com.cod.AniBirth;

import com.cod.AniBirth.animal.entity.Animal;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@JacksonXmlRootElement(localName = "ApiResponse")
public class ApiResponse {
    @JsonProperty("comMsgHeader")
    private ComMsgHeader comMsgHeader;

    @JsonProperty("msgHeader")
    private MsgHeader msgHeader;

    @JsonProperty("MsgBody")
    private MsgBody msgBody;

    @Getter
    @Setter
    public static class ComMsgHeader {
        @JsonProperty("returnCode")
        private String returnCode;

        @JsonProperty("returnMessage")
        private String returnMessage;

        @JsonProperty("successYN")
        private String successYN;

    }

    @Getter
    @Setter
    public static class MsgHeader {
        @JsonProperty("numOfRows")
        private String numOfRows;

        @JsonProperty("pageNo")
        private String pageNo;

        @JsonProperty("totalCount")
        private String totalCount;

        @JsonProperty("totalPage")
        private String totalPage;

    }

    @Getter
    @Setter
    public static class MsgBody {
        @JsonProperty("items")
        private List<Animal> items;

    }
}
