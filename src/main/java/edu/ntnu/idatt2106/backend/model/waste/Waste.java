package edu.ntnu.idatt2106.backend.model.waste;

import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Waste {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int weight;

    @Column(name = "entry_date")
    private LocalDate entryDate;


    public Waste(User user, int weight, LocalDate entryDate) {
        this.user = user;
        this.weight = weight;
        this.entryDate = entryDate;
    }

}
