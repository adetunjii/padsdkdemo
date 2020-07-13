package pad_java_sdk.dto;

import lombok.Data;

@Data
public class CustomMailTemplate {

    private String provider;
    private String from;
    private String subject;
    private String[] recipients;
    private String title;
    private String bgColor;
    private String appName;
    private String appUrl;
    private String appLogo;
    private String content;
    private String bodyContent;
    private String greeting;
    private String[] introLines;
    private String[] outroLines;
    private String buttonLevel;
    private String actionUrl;
    private String actionText;
    private String[] attachments;
}
