package com.cheeseapi.cheeseapi.controllers;

import com.cheeseapi.cheeseapi.exceptions.ConstraintViolationException;
import com.cheeseapi.cheeseapi.models.Category;
import com.cheeseapi.cheeseapi.models.data.CategoryDao;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("categories")
public class CategoryResource {

    @Autowired
    private CategoryDao categoryDao;

    @ApiOperation(value = "Returns list of all Categories in the system",
            produces = "application/json",
            response = Category.class,
            responseContainer = "List")
    @GetMapping("")
    @CrossOrigin
    public ResponseEntity getCategories(){

        return ResponseEntity.ok().body(categoryDao.findAll());
    }

    @ApiOperation(value = "Creates a new category and adds it to Categories, returns newly created category and its URL. 400 if invalid parameters or category already exists",
                response = Category.class)
    @PostMapping("")
    @CrossOrigin
    public ResponseEntity addCategory(@RequestBody @Valid Category category) {
        try{
            categoryDao.save(category);
        }
        catch(org.springframework.dao.DataIntegrityViolationException e){
            throw new ConstraintViolationException(category.getName());
        }

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("cheesemvc-api.herokuapp.com")
                .path("/categories/" + category.getId())
                .build();

        return ResponseEntity.created(uriComponents.toUri()).body(category);
    }

//    handles unique column constraint violation for category's name column-prevents adding duplicate category
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleValidationExceptions(ConstraintViolationException ex) {

        Map response = new HashMap<>();

        response.put("timestamp", new Date());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("errors", ex.getErrors());

        return ResponseEntity.badRequest().body(response);
    }

//    @ApiOperation("Returns a specific category by its identifier. 404 if does not exist")
//    @GetMapping("{categoryId}")
//    public ResponseEntity<?> getCategoryById(@PathVariable int categoryId) {
//
//        Category category = categoryDao.findOne((categoryId));
//
//        if(category != null) {
//            return ResponseEntity.ok().body(category);
//        }
//
//        throw new ResourceNotFoundException(categoryId);
//
//    }
//
//    @ApiOperation("Deletes a cheese from the system by its identifier. 404 if does not exist, 400 if category in use")
//    @DeleteMapping("{categoryId}")
//    public ResponseEntity deleteCategoryById(@PathVariable int categoryId) {
//
//        if(!categoryDao.exists(categoryId)){
//            throw new ResourceNotFoundException(categoryId);
//        }
//
//        if(categoryDao.findOne(categoryId).getCheeses().size() != 0) {
//            //  fulfilling the request would cause an invalid state
//            ResponseEntity.badRequest().body("");
//        }
//
//        categoryDao.delete(categoryId);
//
//        return ResponseEntity.noContent().build();
//    }


}
