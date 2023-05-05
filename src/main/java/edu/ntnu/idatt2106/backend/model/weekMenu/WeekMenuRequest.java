package edu.ntnu.idatt2106.backend.model.weekMenu;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekMenuRequest {
    private List<Integer> intList;
    private String message;

    public List<Integer> getIntList() {
        return intList;
    }

    public String getMessage() {
        return message;
    }

}