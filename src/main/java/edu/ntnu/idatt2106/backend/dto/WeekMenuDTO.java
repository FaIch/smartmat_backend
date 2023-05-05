package edu.ntnu.idatt2106.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeekMenuDTO {
    private Long id;
    private List<WeekMenuRecipeDTO> weekMenuRecipes;
}

