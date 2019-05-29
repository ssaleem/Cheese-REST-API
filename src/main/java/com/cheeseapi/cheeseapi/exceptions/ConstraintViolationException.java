package com.cheeseapi.cheeseapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ConstraintViolationException extends RuntimeException {

    ArrayList<Map<String, String>> errors = new ArrayList<>();

    public ConstraintViolationException(String categoryName) {
        super();
        Map<String, String> error = new HashMap<>();
        String field = "name";
        String defaultMessage = "Category (" + categoryName +") already exists, no duplicates allowed";
        error.put("field", field);
        error.put("defaultMessage", defaultMessage);

        this.errors.add(error);
    }

    public ArrayList<Map<String, String>> getErrors() {
        return errors;
    }
}
