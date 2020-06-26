package pad_java_sdk;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;

import java.util.Map;

public class HttpRequestUtil {

    public static HttpResponse<JsonNode> makePostRequest(String url, Map<String, Object> params, Map<String, String> headers)
            throws UnirestException {

        Gson gson = new Gson();

        HttpRequestWithBody request = Unirest.post(url);

        if(headers != null)
            request = request.headers(headers);

        RequestBodyEntity finalRequest = request.body(gson.toJson(params));

        return finalRequest.asJson();

    }

    public static HttpResponse<JsonNode> makePutRequest(String url, Map<String, Object> params, Map<String, String> headers)
            throws UnirestException {

        Gson gson = new Gson();

        HttpRequestWithBody request = Unirest.put(url);

        if(headers != null)
            request = request.headers(headers);

        RequestBodyEntity finalRequest = request.body(gson.toJson(params));

        return finalRequest.asJson();

    }

    public static HttpResponse<JsonNode> makeGetRequest(String url, Map<String, String> headers) throws UnirestException{

        GetRequest request = Unirest.get(url);

        if(headers != null)
            request = request.headers(headers);

        return request.asJson();

    }
}
