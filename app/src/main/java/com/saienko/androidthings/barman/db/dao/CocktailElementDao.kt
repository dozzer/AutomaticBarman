package com.saienko.androidthings.barman.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.saienko.androidthings.barman.db.entity.CocktailElement

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 02:08
 */

@Dao
interface CocktailElementDao : BaseDao<CocktailElement> {

    @get:Query("SELECT * FROM cocktail_element")
    val list: MutableList<CocktailElement>

    @Query("DELETE FROM cocktail_element")
    fun clear()

    @Query("SELECT * FROM cocktail_element WHERE cocktailId = :cocktailId")
    fun getByCocktailList(cocktailId: Long): MutableList<CocktailElement>

    @Query("SELECT * FROM cocktail_element WHERE cocktailId = :cocktailId")
    fun getByCocktailId(cocktailId: Long): MutableList<CocktailElement>

    @Query("DELETE FROM cocktail_element WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM cocktail_element WHERE id = :id")
    fun getById(id: Long): CocktailElement

    @Query("SELECT * FROM cocktail_element WHERE id LIKE :id")
    fun get(id: Long): CocktailElement

    @Query("DELETE FROM cocktail_element WHERE cocktailId = :cocktailId")
    fun deleteByCocktailId(cocktailId: Long)
}
