package com.saienko.androidthings.barman.db.manager

import com.saienko.androidthings.barman.db.DatabaseCreator
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.db.entity.CocktailElement
import com.saienko.androidthings.barman.db.interfaces.ICocktailManager
import java.util.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/11/17
 * Time: 12:57
 */

class CocktailManager : ICocktailManager {

    override fun list(): MutableList<Cocktail> = DatabaseCreator.getDb().cocktailDao().all

    override fun insert(item: Cocktail): Long {
        val cocktailId = DatabaseCreator.getDb().cocktailDao().insert(item)
        if (item.cocktailElements != null) {
            save(cocktailId, item.cocktailElements)
        }
        return cocktailId
    }

    override suspend fun delete(id: Long) = delete(DatabaseCreator.getDb().cocktailDao().get(id))

    override suspend fun delete(item: Cocktail) {
        item.cocktailElements?.forEach {
            DatabaseCreator.getDb().cocktailElementDao().delete(it.id)
        }
        DatabaseCreator.getDb().cocktailDao().delete(item)
    }

    override suspend fun clear() {
        DatabaseCreator.getDb().cocktailDao().all.forEach {
            delete(it)
        }
    }

    override fun update(item: Cocktail) {
        throw UnsupportedOperationException(
                "Ignore this method. You should use update(item: Cocktail, items: ArrayList<CocktailElement>) ")
    }

    override fun update(item: Cocktail, items: ArrayList<CocktailElement>) {
        val cocktailElementManager = CocktailElementManager()
        cocktailElementManager.deleteByCocktailId(item.id)
        DatabaseCreator.getDb().cocktailDao().update(item)
        if (item.cocktailElements != null) {
            save(item.id, items)
        }
    }

    override fun get(itemId: Long): Cocktail {
        val cocktail = DatabaseCreator.getDb().cocktailDao().get(itemId)
        val cocktailElements = DatabaseCreator.getDb().cocktailElementDao().getByCocktailId(cocktail.id)
        cocktailElements.forEach {
            it.component = DatabaseCreator.getDb().componentDao().get(it.componentId)
            it.position = DatabaseCreator.getDb().positionDao().getByComponentId(it.componentId)
            it.position?.let {
                it.motor = DatabaseCreator.getDb().motorDao().get(it.motorId)
                it.motor?.let {
                    it.gpio = DatabaseCreator.getDb().gpioDao().get(it.id)
                }
            }
        }
        cocktail.cocktailElements = cocktailElements
        return cocktail
    }

    override fun getCocktailElement(cocktailId: Long): MutableList<CocktailElement> {
        val componentManager = ComponentManager()
        val list = DatabaseCreator.getDb().cocktailElementDao().getByCocktailList(cocktailId)
        list.forEach {
            val component = componentManager.get(it.componentId)
            it.component = component
        }
        return list
    }

    private fun save(cocktailId: Long, cocktailElements: List<CocktailElement>?) {
        if (cocktailElements != null) {
            for (element in cocktailElements) {
                element.cocktailId = cocktailId
                DatabaseCreator.getDb().cocktailElementDao().insert(element)
            }
        }
    }

//    override fun getCocktails(tags: MutableList<TagElement>): MutableList<Cocktail> {
//        if (!tags.isEmpty()) {
//            val list = list()
//            list.filter {
//                for (tag in tags) {
//                    it.cocktailElements!!
//                            .filter { TextUtils.equals(tag.tagName, it.component!!.componentName) }
//                            .forEach {
//                                return@filter true
//                            }
//                }
//                return@filter false
//            }
//            return list
//        }
//        return list()
//    }
}
