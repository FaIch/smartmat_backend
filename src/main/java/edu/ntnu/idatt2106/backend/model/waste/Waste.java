package edu.ntnu.idatt2106.backend.model.waste;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;


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
    @JsonIgnore
    private User user;

    private int weight;

    @Column(name = "entry_date")
    private LocalDate entryDate;


    public Waste(User user, int weight, LocalDate entryDate) {
        this.user = user;
        this.weight = weight;
        this.entryDate = entryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waste waste = (Waste) o;
        return weight == waste.weight && Objects.equals(id, waste.id) && Objects.equals(entryDate, waste.entryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weight, entryDate);
    }

    @Override
    public String toString() {
        return "Waste{" +
                "id=" + id +
                ", weight=" + weight +
                ", entryDate=" + entryDate +
                '}';
    }
}
