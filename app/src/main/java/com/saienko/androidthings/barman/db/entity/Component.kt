package com.saienko.androidthings.barman.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import java.util.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 01:58
 */

@Entity(tableName = "component")
data class Component(
        @field:ColumnInfo var componentName: String,
        @field:ColumnInfo var coefficient: Double) : Item() {
    companion object {
        fun getRealComponents(): ArrayList<Component> {
            val list = ArrayList<Component>()

            list.add(Component("Sparkling water", 1.25))
            list.add(Component("Aperol", 1.0))
            list.add(Component("Sparkling wine", 1.5))

            list.add(Component("Whiskey", 1.0))
            list.add(Component("Cola", 1.5))
            list.add(Component("Lime juice", 1.0))
            list.add(Component("Beer", 2.0))

            return list
        }
    }
}
