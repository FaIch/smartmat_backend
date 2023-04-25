package edu.ntnu.idatt2106.backend.model.Response;

public class RecipeSortedByFridgeResponse {
    private Long id;

    private String name;

    private String description;

    private int numberOfItemsRecipe;

    private int numberOfItemsFridge;

    public RecipeSortedByFridgeResponse(Long id, String name, String description, int numberOfItemsRecipe, int numberOfItemsFridge) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfItemsRecipe = numberOfItemsRecipe;
        this.numberOfItemsFridge = numberOfItemsFridge;
    }

    public RecipeSortedByFridgeResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfItemsRecipe() {
        return numberOfItemsRecipe;
    }

    public void setNumberOfItemsRecipe(int numberOfItemsRecipe) {
        this.numberOfItemsRecipe = numberOfItemsRecipe;
    }

    public int getNumberOfItemsFridge() {
        return numberOfItemsFridge;
    }

    public void setNumberOfItemsFridge(int numberOfItemsFridge) {
        this.numberOfItemsFridge = numberOfItemsFridge;
    }
}
