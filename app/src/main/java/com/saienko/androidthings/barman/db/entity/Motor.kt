package com.saienko.androidthings.barman.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NonNls

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 13:30
 */

@Parcelize
@Entity(tableName = "motor")
data class Motor(@field:ColumnInfo var motorSpeed: Int,
                 @field:ColumnInfo var gpioId: Long) : Item(), Parcelable {

    @Ignore
    var gpio: Gpio? = null
        set(gpio) {
            if (gpio != null) {
                field = gpio
                this.gpioId = gpio.id
            }
        }

    val motorName: String
        @NonNls
        get() = if (this.gpio == null) {
            "unknown"
        } else {
            this.gpio!!.gpioName + " " + this.gpio!!.gpioPin
        }
}
