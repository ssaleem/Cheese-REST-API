package com.cheeseapi.cheeseapi.controllers;

import com.cheeseapi.cheeseapi.exceptions.ResourceNotFoundException;
import com.cheeseapi.cheeseapi.models.Cheese;
import com.cheeseapi.cheeseapi.models.data.CheeseDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "cheeses")
@Api(value = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Cheeses")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    /**
     * Get http://localhost:8080/cheeses
     * Purpose: Get all cheeses
     * status: 200
     * body: json array of cheeses, [] if no items in cheeses
     */
    @ApiOperation(value = "Returns list of all Cheeses in the system",
            produces = "application/json",
            response = Cheese.class,
            responseContainer = "List")
    @GetMapping(value = "")
    public ResponseEntity<Iterable<Cheese>> getCheeses() {

        return ResponseEntity.ok().body(cheeseDao.findAll());

    }

    /**
     * Post http://localhost:8080/cheeses
     * Purpose: Create new cheese and add to cheeses collection
     * Success
     * status: 201
     * Location: http://localhost:8080/cheeses/{id_of_newly_created_cheese}
     * body: newly created cheese
     */
    @ApiOperation("Creates a new cheese and adds it to Cheeses list, returns newly created cheese and its URL")
    @PostMapping(value = "")
    public ResponseEntity<?> addCheese(@RequestBody @Valid Cheese cheese) {

        cheeseDao.save(cheese);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port("8080")
                .path("/cheeses/" + cheese.getId())
                .build();

        return ResponseEntity.created(uriComponents.toUri()).body(cheese);
    }

    /**
     * Get http://localhost:8080/cheeses/{cheeseId}
     * Purpose: Get cheese with specific id
     * Success:
     * status: 200
     * body: cheese with id: cheeseId
     * Failure (if ID not found or invalid)
     * status: 404
     */
    @ApiOperation("Returns a specific cheese by its identifier. 404 if does not exist")
    @GetMapping(value = "{cheeseId}")
    public ResponseEntity getCheeseById(@PathVariable int cheeseId){
        Cheese cheese = cheeseDao.findOne(cheeseId);

        if( cheese != null){
            return ResponseEntity.ok().body(cheese);
        }

        throw new ResourceNotFoundException(cheeseId);
//        return ResponseEntity.notFound().build();
    }



    /**
     * Put http://localhost:8080/cheeses/{cheeseId}
     * Purpose: Update existing cheese with specific id
     * Why use Put? : Whole cheese resource is updated
     * Success:
     * status: 200
     * body: updated cheese with id: cheeseId
     * Failure (if ID not found or invalid)
     * status: 404
     */
    @ApiOperation("Updates and returns a specific cheese by its identifier. 404 if does not exist, 400 if invalid parameters")
    @PutMapping(value = "{cheeseId}")
    public ResponseEntity<Cheese> updateCheeseById(
            @PathVariable int cheeseId,
            @RequestBody @Valid Cheese cheese) {
        // If the request could not be correctly parsed (including the request entity/body) the appropriate response is 400 Bad Request
        // @Valid automatically sends that code with the message

        if(cheeseDao.exists(cheeseId)){
            cheeseDao.setCheeseInfoById(cheese.getName(), cheese.getDescription(), cheese.getRating(), cheeseId);
            return ResponseEntity.ok().body(cheeseDao.findOne(cheeseId));
        }

        throw new ResourceNotFoundException(cheeseId);
    }


    /*
    * Put http://localhost:8080/cheeses
    * Spring boot handles it automatically
    * {
    * "timestamp": 1556040276437,
    * "status": 405,
    * "error": "Method Not Allowed",
    * "exception": "org.springframework.web.HttpRequestMethodNotSupportedException",
    * "message": "Request method 'PUT' not supported",
    * "path": "/cheeses"
    *  }
    */


    /**
     * Delete http://localhost:8080/cheeses/{cheeseId}
     * Purpose: Delete existing cheese with specific id
     * Success:
     * status: 204
     * body: empty
     * Failure (if ID not found or invalid)
     * status: 404
     */
    @ApiOperation("Deletes a cheese from the system by its identifier. 404 if does not exist")
    @DeleteMapping(value = "{cheeseId}")
    public ResponseEntity deleteCheeseById(@PathVariable int cheeseId){

        if(!cheeseDao.exists(cheeseId)){
            throw new ResourceNotFoundException(cheeseId);
        }

        cheeseDao.delete(cheeseId);

        return ResponseEntity.noContent().build();
    }

}