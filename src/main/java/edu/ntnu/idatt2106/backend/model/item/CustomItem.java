package edu.ntnu.idatt2106.backend.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private double weight;

    private LocalDate date;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    public CustomItem(String name, double weight, LocalDate date, User user) {
        this.name = name;
        this.weight = weight;
        this.date = date;
        this.user = user;
    }
}
