package com.saienko.androidthings.barman.db.cocktail;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 14:08
 */

@Dao
public interface CocktailDao {
    @Query("SELECT * FROM cocktail")
    List<Cocktail> getAll();

    @Insert
    void insertAll(Cocktail... drinks);

    @Insert
    long insert(Cocktail drink);

    @Delete
    void delete(Cocktail drink);

    @Query("DELETE FROM cocktail WHERE cocktailId = :cocktailId")
    void delete(long cocktailId);

    @Query("DELETE FROM cocktail")
    void clear();

    @Update
    void update(Cocktail cocktail);

    @Query("SELECT * FROM cocktail WHERE cocktailGroupId = :cocktailGroupId")
    List<Cocktail> getByGroup(long cocktailGroupId);

    @Query("SELECT * FROM cocktail WHERE cocktailName LIKE :cocktailName")
    Cocktail getByName(String cocktailName);

//    @Query("DELETE  FROM cocktail WHERE cocktailId = :cocktailId")
//    void deleteById(long cocktailId);
}
