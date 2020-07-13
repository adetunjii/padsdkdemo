package pad_java_sdk;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;
import pad_java_sdk.dto.CustomMailTemplate;
import pad_java_sdk.dto.MailTemplate;
import pad_java_sdk.dto.SmsTemplate;

import java.util.HashMap;
import java.util.Map;

public class NotificationService {

    private static final String baseUrl = "https://staging.api.humbergames.com/notifications/v1/";

    public static boolean sendEmail(MailTemplate mailTemplate){

        try {
            String url = baseUrl + "email";

            Map<String, Object> params = new HashMap<>();

            params.put("html", mailTemplate.getHtml());
            params.put("type", mailTemplate.getType());
            params.put("subject", mailTemplate.getSubject());
            params.put("recipients", mailTemplate.getRecipients());
            params.put("provider", mailTemplate.getProvider());
            params.put("content", mailTemplate.getContent());
            params.put("from", mailTemplate.getFrom());

            Map<String, String> headers = UserService.headers();
            headers.put("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse<JsonNode> response = HttpRequestUtil.makePostRequest(url, params, headers);

            if(response.getStatus() != 201){
                return false;
            }

            JSONObject jsonObject = response.getBody().getObject();

            return jsonObject.has("data") && jsonObject.getString("data").equalsIgnoreCase("Email Sent");

        }catch (Exception exc){
            return false;
        }
    }

    public static boolean sendCustomMail(CustomMailTemplate customMailTemplate){

        try {

            String url = baseUrl + "email";

            Map<String, Object> params = new HashMap<>();

            params.put("provider", customMailTemplate.getProvider());
            params.put("from", customMailTemplate.getFrom());
            params.put("subject", customMailTemplate.getSubject());
            params.put("recipients", customMailTemplate.getRecipients());

            Map<String, Object> header = new HashMap<>();

            header.put("title", customMailTemplate.getTitle());
            header.put("bgColor", customMailTemplate.getBgColor());
            header.put("appName", customMailTemplate.getAppName());
            header.put("appURL", customMailTemplate.getAppUrl());
            header.put("appLogo", customMailTemplate.getAppLogo());


            Map<String, Object> body = new HashMap<>();

            body.put("content", customMailTemplate.getBodyContent());
            body.put("greeting", customMailTemplate.getGreeting());
            body.put("introLines", customMailTemplate.getIntroLines());
            body.put("outroLines", customMailTemplate.getOutroLines());

            Map<String, Object> button = new HashMap<>();

            button.put("level", customMailTemplate.getButtonLevel());
            button.put("actionUrl", customMailTemplate.getActionUrl());
            button.put("actionText", customMailTemplate.getActionText());

            params.put("header", header);
            params.put("content", customMailTemplate.getContent());
            params.put("body", body);
            params.put("button", button);
            params.put("attachments", customMailTemplate.getAttachments());


            HttpResponse<JsonNode> response = HttpRequestUtil.makePostRequest(url, params, UserService.headers());

            if(response.getStatus() != 201){
                return false;
            }

            JSONObject jsonObject = response.getBody().getObject();

            return jsonObject.has("data") && jsonObject.getString("data").equalsIgnoreCase("Email Sent");
        }catch (Exception exc){
            return false;
        }
    }

    public static boolean sendSms(SmsTemplate smsTemplate){

        try {
            String url = baseUrl + "sms";

            Map<String, Object> params = new HashMap<>();

            params.put("recipients", smsTemplate.getRecipients());
            params.put("provider", smsTemplate.getProvider());
            params.put("message", smsTemplate.getMessage());
            params.put("sender", smsTemplate.getSender());

            HttpResponse<JsonNode> response = HttpRequestUtil.makePostRequest(url, params, UserService.headers());

            if (response.getStatus() != 201) {
                return false;
            }

            JSONObject jsonObject = response.getBody().getObject();

            return jsonObject.has("data") && jsonObject.getString("data").equalsIgnoreCase("SMS Sent");
        }catch (Exception exc){
            return false;
        }
    }

}
