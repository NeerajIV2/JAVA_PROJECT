package com.recipe.model;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private double quantity;
    private String unit;
    private String notes;

    public Ingredient() {
    }

    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Ingredient(String name, double quantity, String unit, String notes) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.notes = notes;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        String result = quantity + " " + unit + " " + name;
        if (notes != null && !notes.isEmpty()) {
            result += " (" + notes + ")";
        }
        return result;
    }
}

