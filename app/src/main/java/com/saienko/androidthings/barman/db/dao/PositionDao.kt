package com.saienko.androidthings.barman.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.saienko.androidthings.barman.db.entity.Position

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/12/17
 * Time: 11:57
 */

@Dao
interface PositionDao : BaseDao<Position> {

    @get:Query("SELECT * FROM Position")
    val all: MutableList<Position>

    @Query("DELETE FROM Position WHERE id = :id")
    fun delete(id: Long?)

    @Query("DELETE FROM Position")
    fun clear()

    @Query("SELECT * FROM Position WHERE motorId = :motorId")
    fun getByMotorId(motorId: Long): Position

    @Query("DELETE FROM Position WHERE motorId = :motorId")
    fun deleteByMotorId(motorId: Long)

    @Query("SELECT * FROM Position WHERE componentId = :componentId")
    fun getByComponentId(componentId: Long): Position

    @Query("SELECT * FROM Position WHERE id = :positionId")
    fun get(positionId: Long): Position
}
