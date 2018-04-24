package com.saienko.androidthings.barman.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 14:08
 */

@Parcelize
@Entity(tableName = "gpio")
data class Gpio(
        @field:ColumnInfo(name = "gpioPin") val gpioPin: Long,
        @field:ColumnInfo(name = "gpioName") val gpioName: String) : Item(), Parcelable {


    /**
     * public static Gpio[] getFullRealGpios() {
     * ArrayList<Gpio> list = new ArrayList<>();
     * list.add(new Gpio(15, "GPIO_10"));
     * list.add(new Gpio(35, "GPIO_32"));
     * list.add(new Gpio(29, "GPIO_33"));
     * list.add(new Gpio(31, "GPIO_34"));
     * list.add(new Gpio(13, "GPIO_35"));
     * list.add(new Gpio(37, "GPIO_37"));
     * list.add(new Gpio(36, "GPIO_39"));
     * list.add(new Gpio(22, "GPIO_128"));
     * list.add(new Gpio(18, "GPIO_172"));
     * list.add(new Gpio(16, "GPIO_173"));
     * list.add(new Gpio(40, "GPIO_174"));
     * list.add(new Gpio(38, "GPIO_175"));
     * Gpio[] out = new Gpio[list.size()];
     * out = list.toArray(out);
     * return out;
     * }
    </Gpio> */


    companion object {
        fun getRealGpios(): ArrayList<Gpio> {
            val list = ArrayList<Gpio>()

//            list.add(Gpio(31, "GPIO2_IO02"))
//            list.add(Gpio(35, "GPIO2_IO00"))
//            list.add(Gpio(36, "GPIO2_IO07"))

//            list.add(Gpio(37, "GPIO2_IO05"))
            list.add(Gpio(16, "GPIO6_IO13"))
            list.add(Gpio(18, "GPIO6_IO12"))
            list.add(Gpio(38, "GPIO6_IO15"))
            list.add(Gpio(40, "GPIO6_IO14"))

            return list
        }
    }
}

