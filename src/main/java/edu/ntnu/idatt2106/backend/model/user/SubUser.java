package edu.ntnu.idatt2106.backend.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mainUser_email")
    private User mainUser;

    private String nickname;

    private Role role;

    public SubUser(String nickname, Role role) {
        this.nickname = nickname;
        this.role = role;
    }

}

