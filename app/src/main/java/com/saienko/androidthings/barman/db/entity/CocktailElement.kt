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

@Entity(tableName = "cocktail_element")
data class CocktailElement(@field:ColumnInfo(name = "cocktail_element_component_id") var componentId: Long,
                           @field:ColumnInfo(name = "cocktailId") var cocktailId: Long,
                           @field:ColumnInfo(name = "cocktail_element_volume") var volume: Int) : Item() {

    @Ignore
    var component: Component? = null
        set(component) {
            if (component != null) {
                field = component
                componentId = component.id
            }
        }

    @Ignore
    var position: Position? = null

}
