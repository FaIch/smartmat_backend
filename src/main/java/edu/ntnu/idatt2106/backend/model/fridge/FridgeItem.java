package edu.ntnu.idatt2106.backend.model.fridge;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ntnu.idatt2106.backend.model.item.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FridgeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    private int quantity;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public FridgeItem(int quantity, LocalDate expirationDate, Item itemById) {
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.item = itemById;
    }

    @Override
    public String toString(){
        return "";
    }
}
