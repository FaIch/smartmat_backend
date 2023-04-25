package edu.ntnu.idatt2106.backend.model.waste;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WasteRequest {
    private int weight;
    private String entryDate;
}
