package edu.ntnu.idatt2106.backend.model.WeekMenu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekMenuData {
    private int totalAmountOfItems;
    private int totalAmountOfMissingItems;
    private int totalAmountOfItemsToExpire;


}
