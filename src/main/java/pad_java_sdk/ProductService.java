package pad_java_sdk;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import pad_java_sdk.dto.Product;
import pad_java_sdk.exceptions.DuplicateEntryException;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;
import java.util.Map;

public class ProductService {

    private static final String baseUrl = "https://staging.api.humbergames.com/products/v1/";

    public static Product createProduct(String name, double amount, String description) throws UnirestException, DuplicateEntryException, ServiceUnavailableException {

        String url = baseUrl + "product";

        Map<String, Object> params = new HashMap<>();

        params.put("name", name);
        params.put("amount", amount);
        params.put("description", description);

        Map<String, String> headers = UserService.headers();

        headers.put("Content-Type", "application/x-www-form-urlencoded");

        HttpResponse<JsonNode> response = HttpRequestUtil.makePostRequest(url, params, headers);

        if(response.getStatus() == 409){
            throw new DuplicateEntryException("Name already exists");
        }

        if(response.getStatus() != 200){
            throw new ServiceUnavailableException("Could not create product");
        }

        return getProduct(response.getBody());
    }

    private static Product getProduct(JsonNode body) {

        JSONObject jsonObject = body.getObject().getJSONObject("data");

        Product product = new Product();

        product.setName(jsonObject.getString("name"));
        product.setDescription(jsonObject.getString("description"));
        product.setAmount(jsonObject.getDouble("amount"));
        product.setId(jsonObject.getString("id"));
        product.setUpdatedAt(jsonObject.getString("updated_at"));
        product.setCreatedAt(jsonObject.getString("created_at"));

        return product;
    }
}
