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

/**
 * SuggestionController is a REST controller responsible for handling requests related to
 * getting suggestions for a user. It interacts with the SuggestionService to manage and retrieve data.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/suggestion")
public class SuggestionController {

    SuggestionService suggestionService;

    /**
     * Constructs a SuggestionController with the provided SuggestionService instance.
     *
     * @param suggestionService an instance of SuggestionService
     */
    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    /**
     * Retrieves suggested items for the given user.
     *
     * @param user the authenticated user
     * @return ResponseEntity containing a list of suggested items for the user
     */
    @GetMapping("/get")
    public ResponseEntity<List<Item>> getSuggestions(@AuthenticationPrincipal User user) {
        return suggestionService.getSuggestedItems(user.getId());
    }
}
