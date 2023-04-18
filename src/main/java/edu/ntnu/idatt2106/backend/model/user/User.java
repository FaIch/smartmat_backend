package edu.ntnu.idatt2106.backend.model.user;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;

/**
 * The user-class
 * This is an entity for storing a user in the database
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String email;

    private String nickname;
    private Long phoneNumber;
    private String address;
    private Role role;
    private byte[] password;
    private byte[] salt;

    public User(String email, String nickname, Long phoneNumber, String address, Role role) {
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    /**
     * The overwritten equals method
     * @param o the object being compared
     * @return true or false depending on if it is equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    /**
     * The overwritten hashcode method
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}