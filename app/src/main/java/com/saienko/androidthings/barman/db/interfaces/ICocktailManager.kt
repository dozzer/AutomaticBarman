package com.saienko.androidthings.barman.db.interfaces

import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.db.entity.CocktailElement
import java.util.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/11/17
 * Time: 12:56
 */

interface ICocktailManager : IBaseManager<Cocktail> {

    fun getCocktailElement(cocktailId: Long): MutableList<CocktailElement>

    fun update(item: Cocktail, items: ArrayList<CocktailElement>)
}
