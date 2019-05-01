package com.cheeseapi.cheeseapi.controllers;

import com.cheeseapi.cheeseapi.exceptions.ResourceNotFoundException;
import com.cheeseapi.cheeseapi.models.Cheese;
import com.cheeseapi.cheeseapi.models.Menu;
import com.cheeseapi.cheeseapi.models.data.CheeseDao;
import com.cheeseapi.cheeseapi.models.data.MenuDao;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "menus")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    /**
     * Get http://localhost:8080/menus
     * Purpose: Get all menus
     * status: 200
     * body: json array of menus, [] if no items in menus
     */
    @ApiOperation("Returns list of all Menus in the system")
    @GetMapping(value = "")
    public ResponseEntity<Iterable<Menu>> getMenus() {

        return ResponseEntity.ok(menuDao.findAll());

    }

    /**
     * Post http://localhost:8080/menus
     * Purpose: Create new menu and add to menus collection
     * Success
     * status: 201
     * Location: http://localhost:8080/menus/{id_of_newly_created_cheese}
     * body: newly created cheese
     */

    @ApiOperation("Creates a new menu and adds it to Menus, returns newly created menu and its URL. 400 if invalid parameters")
    @PostMapping(value = "")
    public ResponseEntity<Menu> addMenu(@RequestBody @Valid Menu menu) {

        menuDao.save(menu);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port("8080")
                .path("/menus/" + menu.getId())
                .build();

        return ResponseEntity.created(uriComponents.toUri()).body(menu);

    }

    /**
     * Get http://localhost:8080/menus/{menuId}
     * Purpose: Get menu with specific id
     * Success:
     * status: 200
     * body: menu with id: menuId
     * Failure: (if ID not found or invalid)
     * status: 404
     */
    @ApiOperation("Returns a specific menu by its identifier. 404 if does not exist")
    @GetMapping(value = "{menuId}")
    public ResponseEntity<Menu> getMenuById(@PathVariable int menuId) {

        Menu menu = menuDao.findOne(menuId);

        if(menu != null) {
            return ResponseEntity.ok(menu);
        }

        throw new ResourceNotFoundException(menuId);

    }

    /**
     * Get http://localhost:8080/menus/{menuId}/cheeses
     * Purpose: Get existing menu's cheeses collection
     * Success:
     * status: 200
     * body: cheese collection from menu with id: menuId
     * Failure (if ID not found or invalid)
     * status: 404
     */
    @ApiOperation("Returns cheeses from a specific menu by its identifier. 404 if menu does not exist")
    @GetMapping(value = "{menuId}/cheeses")
    public ResponseEntity<Iterable<Cheese>> getCheesesFromMenu(@PathVariable int menuId) {

        Menu existingMenu = menuDao.findOne(menuId);

        if(existingMenu == null) {
            throw new ResourceNotFoundException(menuId);
        }

        return ResponseEntity.ok().body(existingMenu.getCheeses());
    }


    /**
     * Put http://localhost:8080/menus/{menuId}/cheeses
     * Purpose: Update existing menu's cheeses collection with specific cheese
     * Receives: cheeseId (through request body x-www-form-urlencoded format)
     * Success:
     * status: 200
     * body: updated menu with id: menuId
     * Failure (if ID not found or invalid)
     * status: 404
     */
//    @ApiOperation("Adds cheese to a menu with a specific identifier, returns updated menu. 404 if menu or cheese does not exist")
//    @PostMapping(value = "{menuId}/cheeses")
//    public ResponseEntity addCheesetoMenu(@PathVariable int menuId, @RequestParam int cheeseId) {
//
//        Menu existingMenu = menuDao.findOne(menuId);
//        Cheese cheese = cheeseDao.findOne(cheeseId);
//
//        if(existingMenu == null) {
//            throw new ResourceNotFoundException(menuId);
//        }
//
//        if(cheese == null) {
//            throw new ResourceNotFoundException(cheeseId);
//        }
//
//        existingMenu.addItem(cheese);
//        menuDao.save(existingMenu);
//        return ResponseEntity.ok(existingMenu);
//    }

    @ApiOperation("Adds cheese/s to a menu, returns updated menu. 404 if menu or cheese does not exist")
    @PostMapping(value = "{menuId}/cheeses")
    public ResponseEntity addCheesestoMenu(@PathVariable int menuId, @RequestBody int[] cheeseIds) {

        Menu existingMenu = menuDao.findOne(menuId);

        if(existingMenu == null) {
            throw new ResourceNotFoundException(menuId);
        }

        for(int id: cheeseIds){
            Cheese cheese = cheeseDao.findOne(id);
            if(cheese == null) {
                throw new ResourceNotFoundException(id);
            }
            existingMenu.addItem(cheese);
        }

        menuDao.save(existingMenu);
        return ResponseEntity.ok(existingMenu);
    }

    @ApiOperation("Deletes a cheese from the menu by its identifier. 404 if menu or cheese does not exist")
    @DeleteMapping(value = "{menuId}/cheeses/{cheeseId}")
    public ResponseEntity deleteCheeseFromMenu(@PathVariable int cheeseId, @PathVariable int menuId){

        Menu menu = menuDao.findOne(menuId);
        if(menu == null) {
            throw new ResourceNotFoundException(menuId);
        }

        Cheese cheese = cheeseDao.findOne(cheeseId);
        if(cheese == null) {
            throw new ResourceNotFoundException(cheeseId);
        }


        if(menu.getCheeses().contains(cheese)) {
            menu.removeItem(cheese);
        }

        menuDao.save(menu);

        return ResponseEntity.noContent().build();
    }

}
