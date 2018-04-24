package com.saienko.androidthings.barman.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/12/17
 * Time: 11:45
 */

@Entity(tableName = "position")
data class Position(@field:ColumnInfo var componentId: Long,
                    @field:ColumnInfo var motorId: Long) : Item() {


    @Ignore
    var component: Component? = null
        set(component) {
            if (component != null) {
                componentId = component.id
                field = component
            }
        }

    @Ignore
    var motor: Motor? = null
        set(motor) {
            if (motor != null) {
                motorId = motor.id
                field = motor
            }
        }
}
