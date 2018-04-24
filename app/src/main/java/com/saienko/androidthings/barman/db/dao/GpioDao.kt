package com.saienko.androidthings.barman.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.saienko.androidthings.barman.db.entity.Gpio
import io.reactivex.Single

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 14:08
 */

@Dao
interface GpioDao : BaseDao<Gpio> {

    @get:Query("SELECT * FROM gpio")
    val all: MutableList<Gpio>

    @Query("DELETE FROM gpio WHERE id = :gpioId")
    fun delete(gpioId: Long)

    @Query("DELETE FROM gpio")
    fun clear()

    @Query("SELECT * FROM gpio WHERE id NOT IN (:gpioIds)")
    fun getAllFree(gpioIds: LongArray): Single<MutableList<Gpio>>

    @Query("SELECT * FROM gpio WHERE id = :id")
    fun get(id: Long): Gpio
}
