package ru.yandex.practicum.models;

import java.util.List;

public class Order {
    private List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public Order withIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }
}
