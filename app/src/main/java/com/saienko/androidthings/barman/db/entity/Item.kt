package com.saienko.androidthings.barman.db.entity

import android.arch.persistence.room.PrimaryKey

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 3/25/18
 * Time: 15:55
 */

abstract class Item {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}