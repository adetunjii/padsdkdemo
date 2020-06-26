package pad_java_sdk;

import lombok.Data;

import java.util.Map;

@Data
public class User {

    private String id;
    private String userId;
    private String clientId;
    private String name;
    private String email;
    private String phoneNumber;
    private String roleName;
    private String createdAt;
    private String updatedAt;
    private Map<String, Object> meta;

    @Override
    public String toString(){

        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roleName=" + roleName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", meta=" + meta +
                '}';
    }

}
