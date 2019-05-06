package com.cheeseapi.cheeseapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 20, message = "Category name must be between 3 and 20 characters")
    private String name;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "category_id")
    List<Cheese> cheeses = new ArrayList<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Cheese> getCheeses() {
        return cheeses;
    }
}
