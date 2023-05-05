package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/suggestion")
public class SuggestionController {

    SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    /*
    * Get all suggested items for a user
     */
    @GetMapping("/get")
    public ResponseEntity<List<Item>> getSuggestions(@AuthenticationPrincipal User user) {
        return suggestionService.getSuggestedItems(user.getId());
    }
}
