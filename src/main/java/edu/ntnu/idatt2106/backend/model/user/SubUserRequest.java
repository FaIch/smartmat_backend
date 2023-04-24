package edu.ntnu.idatt2106.backend.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubUserRequest {
    private final String userEmail;
    private final String nickname;
    private Role role;
    private int passcode;

    public SubUserRequest(String userEmail, String nickname){
        this.userEmail = userEmail;
        this.nickname = nickname;
    }
}
