package edu.ntnu.idatt2106.backend.model.Response;

public class RecipeSortedByFridgeResponse {
    private Long id;

    private String name;

    private String estimated_time;

    private String description;

    private String image;

    private int numberOfItemsRecipe;

    private int numberOfItemsFridge;

    public RecipeSortedByFridgeResponse(Long id, String name, String estimatedTime, String description, String image, int numberOfItemsRecipe, int numberOfItemsFridge) {
        this.id = id;
        this.name = name;
        estimated_time = estimatedTime;
        this.description = description;
        this.image = image;
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

    public String getEstimated_time() {
        return estimated_time;
    }

    public void setEstimated_time(String estimated_time) {
        this.estimated_time = estimated_time;
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


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
