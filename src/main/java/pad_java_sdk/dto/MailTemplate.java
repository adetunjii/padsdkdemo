package pad_java_sdk.dto;

import lombok.Data;

import java.util.Arrays;

@Data
public class MailTemplate {

    private String html;
    private String type;
    private String subject;
    private String[] recipients;
    private String provider;
    private String content;
    private String from;

    public String getRecipients(){

        return String.join("','", Arrays.asList(recipients));

    }

}
