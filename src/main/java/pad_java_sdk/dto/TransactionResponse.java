package pad_java_sdk.dto;

import lombok.Data;

@Data
public class TransactionResponse {

    private Transaction[] transactions;
    private int size;
    private int totalElements;
    private int totalPages;
    private int number;
}
