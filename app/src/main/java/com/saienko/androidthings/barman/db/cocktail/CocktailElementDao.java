package com.saienko.androidthings.barman.db.cocktail;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 02:08
 */

@Dao
public interface CocktailElementDao {

    @Query("SELECT * FROM cocktail_element")
    List<CocktailElement> getAll();

    @Insert
    void insertAll(CocktailElement... cocktailElements);

    @Insert
    long insert(CocktailElement cocktailElement);

    @Delete
    void delete(CocktailElement cocktailElement);

    @Query("DELETE FROM cocktail_element")
    void clear();

    @Update
    void update(CocktailElement cocktailElement);

    @Query("SELECT * FROM cocktail_element WHERE cocktailId = :cocktailId")
    List<CocktailElement> getByCocktailId(long cocktailId);

    @Query("DELETE FROM cocktail_element WHERE cocktailId = :cocktailId")
    void delete(long cocktailId);
}
