package edu.ntnu.idatt2106.backend.model.user;

import lombok.*;

/**
 * The class representing a user request
 * This is used for login and creating of users
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String email;
    private Long phoneNumber;
    private String address;
    private String password;

    public UserRequest(String email, Long phoneNumber, String address) {
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public UserRequest(String email, String token){
        this.email = email;
        this.password = token;
    }
}
