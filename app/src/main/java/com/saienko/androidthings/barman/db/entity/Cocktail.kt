package com.saienko.androidthings.barman.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 01:56
 */

@Entity(tableName = "cocktail")
data class Cocktail(@field:ColumnInfo(name = "cocktailName") var cocktailName: String) : Item() {

    @Ignore
    var cocktailElements: List<CocktailElement>? = null

}
