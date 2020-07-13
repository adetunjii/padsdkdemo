package pad_java_sdk.dto;

import lombok.Data;

@Data
public class SmsTemplate {

    private String[] recipients;
    private String provider;
    private String message;
    private String sender;
}
