package com.cheeseapi.cheeseapi.models;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "cheeses")
@ApiModel(description = "Cheese")
public class Cheese {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 15, message = "Cheese name must be between 3 and 15 characters")
    private String name;

    @NotNull
    @Size(min= 1, max = 30, message = "Description must not be empty")
    private String description;


    @NotNull
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private int rating;

//    @ManyToOne
//    private Category category;

    @ManyToMany(mappedBy = "cheeses")
    private List<Menu> menus;


    public Cheese() {

    }

    public Cheese(String name, String description, int rating) {
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @PreRemove
    private void removeCheeseFromMenus(){
        for(Menu menu: menus){
            menu.removeItem(this);
        }
    }
}
