package edu.ntnu.idatt2106.backend.model.response;

public class ResponseWeekMenu {

    private int totNumbersOfItemsMenu;

    private int totNumbersOfItemMissingMenu;

    public ResponseWeekMenu( int numberOfItemsRecipe, int numberOfItemsFridge) {

        this.totNumbersOfItemsMenu = numberOfItemsRecipe;
        this.totNumbersOfItemMissingMenu = numberOfItemsFridge;
    }

    public ResponseWeekMenu() {
    }


    public int getTotNumbersOfItemsMenu() {
        return totNumbersOfItemsMenu;
    }


    public void setTotNumbersOfItemsMenu(int totNumbersOfItemsMenu) {
        this.totNumbersOfItemsMenu = totNumbersOfItemsMenu;
    }

    public int getTotNumbersOfItemMissingMenu() {
        return totNumbersOfItemMissingMenu;
    }

    public void setTotNumbersOfItemMissingMenu(int totNumbersOfItemMissingMenu) {
        this.totNumbersOfItemMissingMenu = totNumbersOfItemMissingMenu;
    }
}

