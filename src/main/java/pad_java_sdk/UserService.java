package pad_java_sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.json.JSONObject;
import pad_java_sdk.exceptions.UnauthorisedAccessException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UserService {

    private static final String baseUrl = "http://staging.api.humbergames.com/users/v1";

    private static String clientId;
    private static String clientSecret;
    private static byte[] secret;
    private static String token;
    private static Logger logger = Logger.getLogger(String.valueOf(UserService.class));

    private static User user = new User();

    public static User login(Map<String, Object> credentials) throws UnirestException, NoSuchFieldException {

        if (!(credentials.containsKey("email") || credentials.containsKey("phoneNumber")) && credentials.containsKey("password")) {

            throw new NoSuchFieldException("(email | phoneNumber) and password required");
        }

        String url = baseUrl + "/auths/login";

        Map<String, String> headers = new HashMap<String, String>();

        headers.put("client-id", clientId);
        headers.put("client_secret", clientSecret);

        HttpResponse<JsonNode> response = HttpRequestUtil.makePostRequest(url, credentials, headers);

        if(response.getStatus() >= 401){
            throw new UnauthorisedAccessException("Invalid Credentials");
        }

        user = getUserEntity(response.getBody());

        return user;
    }

    public static User createUser(Map<String, Object> parameters) throws NoSuchFieldException, UnirestException{

        checkFields(parameters);

        String url = baseUrl + "/users";

        HttpResponse<JsonNode> response = HttpRequestUtil.makePostRequest(url, parameters, headers());

        return getUserEntity(response.getBody());

    }

    public static User updateUser(Map<String, Object> parameters) throws UnirestException{

        String url = baseUrl + "/users/" + user.getUserId();

        HttpResponse<JsonNode> response = HttpRequestUtil.makePostRequest(url, parameters, headers());

        user = getUserEntity(response.getBody());

        return user;
    }

    public static User fetchUserDetails(String userId) throws UnirestException{

        String url = baseUrl + "/users/" + userId;

        HttpResponse<JsonNode> response = HttpRequestUtil.makeGetRequest(url, headers());

        return getUserEntity(response.getBody());

    }

    private static void init() {
        clientId = System.getProperty("clientId");
        clientSecret = System.getProperty("clientSecret");
        secret = clientSecret.getBytes();
        token = System.getProperty("token");

    }

    private static User getUserEntity() throws MalformedJwtException, ExpiredJwtException, SignatureException {

        init();

        Jws<Claims> result = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret))
                .parseClaimsJws(token);

        Map<String, Object> userMap = result.getBody().get("auth", HashMap.class);

        user = getUserEntity(userMap);

        return user;
    }

    private static User getUserEntity(Map<String, Object> userMap){

        ObjectMapper objectMapper = new ObjectMapper();

        user.setId(String.valueOf(userMap.get("id")));
        user.setClientId(String.valueOf(userMap.get("clientId")));
        user.setUserId(String.valueOf(userMap.get("userId")));
        user.setPhoneNumber(String.valueOf(userMap.get("phoneNumber")));
        user.setEmail(String.valueOf(userMap.get("email")));
        user.setCreatedAt(String.valueOf(userMap.get("createdAt")));
        user.setUpdatedAt(String.valueOf(userMap.get("updatedAt")));

//        Map<String, Object> map = new Gson().fromJson(userMap.get("profile"), HashMap.class)
        Map<String, Object> map = objectMapper.convertValue(userMap.get("profile"), HashMap.class);

        user.setName(String.valueOf(map.get("name")));
        user.setCreatedAt(String.valueOf(map.get("createdAt")));
        user.setUpdatedAt(String.valueOf(map.get("updatedAt")));

        if(map.containsKey("meta")) {
            map = objectMapper.convertValue(map.get("meta"), HashMap.class);
            user.setMeta(map);
        }

        return user;
    }

    private static User getUserEntity(JsonNode jsonNode){

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jsonObject = jsonNode.getObject();
        jsonObject = jsonObject.getJSONObject("data");

        if(jsonObject.has("user")){
            jsonObject = jsonObject.getJSONObject("user");
        }

        User user = new User();

        user.setId(jsonObject.getString("id"));
        user.setClientId(jsonObject.getString("clientId"));
        user.setUserId(jsonObject.getString("userId"));

        if(jsonObject.has("email")){
            user.setEmail(jsonObject.getString("email"));
        }

        user.setPhoneNumber(jsonObject.getString("phoneNumber"));
        user.setCreatedAt(jsonObject.getString("createdAt"));
        user.setUpdatedAt(jsonObject.getString("updatedAt"));
        user.setRoleName(jsonObject.getJSONObject("role").getString("name"));

        jsonObject = jsonObject.getJSONObject("profile");

        user.setName(jsonObject.getString("name"));
        user.setCreatedAt(jsonObject.getString("createdAt"));
        user.setUpdatedAt(jsonObject.getString("updatedAt"));

        HashMap<String, Object> meta = new Gson().fromJson(jsonObject.getJSONObject("meta").toString(), HashMap.class);

        user.setMeta(meta);

        return user;
    }

    public static User getUser() {

        try {
            if(user == null){
                getUserEntity();
            }

            return user.getRoleName() == null ? fetchUserDetails(user.getUserId()) : user;
        }catch (UnirestException ex){
            throw new UnauthorisedAccessException("Could not fetch user details");
        }


    }

    public static boolean isUserValid() {
        try{

            getUserEntity();

            return true;
        }catch (Exception exc){

            logger.info("Error on validating user: " + exc.getMessage());

            exc.printStackTrace();

            return false;
        }
    }

    private static void checkFields(Map<String, Object> params) throws NoSuchFieldException{
        if (!(params.containsKey("email") || params.containsKey("phoneNumber"))) {

            throw new NoSuchFieldException("Email or phone number required");
        }
    }

    static Map<String, String> headers(){

        Map<String, String> headers = new HashMap<String, String>();

        headers.put("client-id", clientId);
        headers.put("client_secret", clientSecret);
        headers.put("Authorization", "Bearer " + token);

        return headers;

    }

}
