package com.saienko.androidthings.barman.db.cocktail;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 02:07
 */

@Dao
public interface ComponentDao {
    @Query("SELECT * FROM component")
    List<Component> getAll();

    @Insert
    void insertAll(Component... components);

    @Insert
    void insert(Component component);

    @Delete
    void delete(Component component);

    @Query("DELETE FROM component")
    void clear();

    @Update
    void update(Component component);

    @Query("SELECT * FROM component WHERE id = :cocktailId")
    Component getById(long cocktailId);

    @Query("SELECT * FROM component WHERE id NOT IN (:componentIds)")
    List<Component> getAllFree(long[] componentIds);
}
