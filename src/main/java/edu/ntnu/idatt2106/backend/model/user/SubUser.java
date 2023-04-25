package edu.ntnu.idatt2106.backend.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JoinColumn(name = "mainUser_id")
    @JsonBackReference
    private User mainUser;

    private String nickname;

    private Role role;

    @Column(length = 4)
    private int passcode;

    public SubUser(String nickname, Role role) {
        this.nickname = nickname;
        this.role = role;
    }

    public SubUser(String nickname, Role role, int passcode) {
        this.nickname = nickname;
        this.role = role;
        this.passcode = passcode;
    }

}

