package edu.ntnu.idatt2106.backend.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubUserRequest {
    private String userEmail;
    private String nickname;
    private Role role;
    private int passcode;

    public SubUserRequest(String userEmail, String nickname){
        this.userEmail = userEmail;
        this.nickname = nickname;
    }
}
