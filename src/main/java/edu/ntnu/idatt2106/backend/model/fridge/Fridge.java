package edu.ntnu.idatt2106.backend.model.fridge;

import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<FridgeItem> fridgeItems = new ArrayList<>();

    public void addFridgeItem(FridgeItem fridgeItem) {
        fridgeItem.setFridge(this);
    }

    public void removeFridgeItem(FridgeItem fridgeItem) {
        fridgeItem.setFridge(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fridge fridge = (Fridge) o;
        return Objects.equals(id, fridge.id) && Objects.equals(user, fridge.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
