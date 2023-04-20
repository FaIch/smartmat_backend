package edu.ntnu.idatt2106.backend.model.fridge;

import edu.ntnu.idatt2106.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FridgeItem> fridgeItems = new ArrayList<>();

    public void addFridgeItem(FridgeItem fridgeItem) {
        fridgeItems.add(fridgeItem);
        fridgeItem.setFridge(this);
    }

    public void removeFridgeItem(FridgeItem fridgeItem) {
        fridgeItems.remove(fridgeItem);
        fridgeItem.setFridge(null);
    }
}
