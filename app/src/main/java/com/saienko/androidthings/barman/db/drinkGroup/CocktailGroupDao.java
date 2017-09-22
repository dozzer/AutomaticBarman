package com.saienko.androidthings.barman.db.drinkGroup;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 14:08
 */

@Dao
public interface CocktailGroupDao {

    @Query("SELECT * FROM cocktail_group")
    List<CocktailGroup> getAll();

    @Insert
    void insertAll(CocktailGroup... groups);

    @Insert
    void insert(CocktailGroup group);

    @Delete
    void delete(CocktailGroup group);

    @Query("DELETE FROM cocktail_group")
    void clear();

    @Update
    void update(CocktailGroup group);

    @Query("SELECT * FROM cocktail_group WHERE id = :cocktailGroupId")
    CocktailGroup getById(long cocktailGroupId);
}
