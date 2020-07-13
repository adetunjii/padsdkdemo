package pad_java_sdk;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import pad_java_sdk.dto.Transaction;
import pad_java_sdk.dto.TransactionResponse;

import javax.naming.ServiceUnavailableException;

public class BillingService {

    public static final String baseUrl = "https://staging.api.humbergames.com/billings/";

    public static TransactionResponse verifyTransaction(String reference) throws UnirestException, ServiceUnavailableException {

        String url = baseUrl + "wallet-transactions?reference=" + reference;

        HttpResponse<JsonNode> response = HttpRequestUtil.makeGetRequest(url, UserService.headers());

        if(response.getStatus() != 200){
            throw new ServiceUnavailableException("Could not fetch transaction");
        }

        return getTransactionResponse(response.getBody());
    }

    private static TransactionResponse getTransactionResponse(JsonNode body) {

        TransactionResponse transactionResponse = new TransactionResponse();

        JSONObject jsonObject = body.getObject();
        JSONObject embeddedObject = jsonObject.getJSONObject("_embedded");
        JSONObject pageObject = jsonObject.getJSONObject("page");

        JSONArray walletTransactions = embeddedObject.getJSONArray("walletTransactions");

        Transaction[] transactions = new Transaction[walletTransactions.length()];

        for(int i =0; i<walletTransactions.length(); i++){

            Transaction transaction = new Transaction();

            JSONObject transactionObject = walletTransactions.getJSONObject(i);

            transaction.setId(transactionObject.getString("id"));
            transaction.setCreatedAt(transactionObject.getString("createdAt"));
            transaction.setUpdatedAt(transactionObject.getString("updatedAt"));
            transaction.setReference(transactionObject.getString("reference"));
            transaction.setDebitAmount(transactionObject.getDouble("debitAmount"));
            transaction.setCreditAmount(transactionObject.getDouble("creditAmount"));
            transaction.setId(String.valueOf(transactionObject.get("narration")));

            transactions[i] = transaction;

        }

        transactionResponse.setTransactions(transactions);
        transactionResponse.setSize(pageObject.getInt("size"));
        transactionResponse.setTotalElements(pageObject.getInt("totalElements"));
        transactionResponse.setTotalPages(pageObject.getInt("totalPages"));
        transactionResponse.setNumber(pageObject.getInt("number"));

        return transactionResponse;
    }
}
