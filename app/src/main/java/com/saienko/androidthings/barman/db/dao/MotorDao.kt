package com.saienko.androidthings.barman.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.saienko.androidthings.barman.db.entity.Motor

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 13:32
 */

@Dao
interface MotorDao : BaseDao<Motor> {

    @get:Query("SELECT * FROM motor")
    val all: MutableList<Motor>

    @Query("DELETE FROM motor WHERE id = :id")
    fun delete(id: Long)

    @Query("DELETE FROM motor")
    fun clear()

    @Query("SELECT * FROM motor WHERE id = :id")
    fun get(id: Long): Motor
}
