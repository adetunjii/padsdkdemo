package pad_java_sdk.dto;

import lombok.Data;

@Data
public class Transaction {

    private String id;
    private String createdAt;
    private String updatedAt;
    private String reference;
    private double debitAmount;
    private double creditAmount;
    private String narration;
}
