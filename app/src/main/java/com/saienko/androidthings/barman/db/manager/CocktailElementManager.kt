package com.saienko.androidthings.barman.db.manager

import com.saienko.androidthings.barman.db.DatabaseCreator
import com.saienko.androidthings.barman.db.entity.CocktailElement
import com.saienko.androidthings.barman.db.interfaces.ICocktailElementManager

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/18/17
 * Time: 22:21
 */

class CocktailElementManager : ICocktailElementManager {

    override fun list(): MutableList<CocktailElement> = DatabaseCreator.getDb().cocktailElementDao().list

    override fun insert(item: CocktailElement) = DatabaseCreator.getDb().cocktailElementDao().insert(item)

    override fun get(itemId: Long) = DatabaseCreator.getDb().cocktailElementDao().get(itemId)

    override suspend fun delete(id: Long) = DatabaseCreator.getDb().cocktailElementDao().delete(id)

    override suspend fun clear() = DatabaseCreator.getDb().cocktailElementDao().clear()

    override suspend fun delete(item: CocktailElement) = DatabaseCreator.getDb().cocktailElementDao().delete(item)

    override fun update(item: CocktailElement) = DatabaseCreator.getDb().cocktailElementDao().update(item)

    fun deleteByCocktailId(cocktailId: Long) = DatabaseCreator.getDb().cocktailElementDao().deleteByCocktailId(
            cocktailId)

}
