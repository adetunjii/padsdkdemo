package pad_java_sdk.dto;

import lombok.Data;

@Data
public class Product {

    private String name;
    private String description;
    private double amount;
    private String id;
    private String createdAt;
    private String updatedAt;
}
