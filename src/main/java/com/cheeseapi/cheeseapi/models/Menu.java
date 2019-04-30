package com.cheeseapi.cheeseapi.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 15, message = "Menu name should be between 3 and 15 characters")
    private String name;

    @ManyToMany
    private Set<Cheese> cheeses;

    public Menu() {
    }

    public Menu(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Cheese> getCheeses() {
        return cheeses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addItem(Cheese cheese){
        cheeses.add(cheese);
    }

    public void removeItem(Cheese cheese){
        this.cheeses.remove(cheese);
    }
}
