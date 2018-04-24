package com.saienko.androidthings.barman.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.saienko.androidthings.barman.db.entity.Cocktail

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 14:08
 */

@Dao
interface CocktailDao : BaseDao<Cocktail> {
    @get:Query("SELECT * FROM cocktail")
    val all: MutableList<Cocktail>

    @Insert
    fun insertAll(vararg cocktails: Cocktail)

    @Query("DELETE FROM cocktail WHERE id = :cocktailId")
    fun delete(cocktailId: Long)

    @Query("DELETE FROM cocktail")
    fun clear()

    @Query("SELECT * FROM cocktail WHERE id LIKE :cocktailId")
    fun get(cocktailId: Long): Cocktail

}
