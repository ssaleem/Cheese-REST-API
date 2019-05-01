package com.cheeseapi.cheeseapi.models.data;

import com.cheeseapi.cheeseapi.models.Cheese;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CheeseDao extends CrudRepository<Cheese, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("update Cheese c set c.name = ?1, c.description = ?2, c.rating = ?3 where c.id = ?4")
    int setCheeseInfoById(String name, String description, int rating, int id);
}
