package com.saienko.androidthings.barman.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.saienko.androidthings.barman.db.entity.Component

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 02:07
 */

@Dao
interface ComponentDao : BaseDao<Component> {

    @get:Query("SELECT * FROM component")
    val all: MutableList<Component>

    @Query("DELETE FROM component WHERE id = :componentId")
    fun delete(componentId: Long)

    @Query("DELETE FROM component")
    fun clear()

    @Query("SELECT * FROM component WHERE id = :componentId")
    fun get(componentId: Long): Component

    @Query("SELECT * FROM component WHERE id NOT IN (:componentIds)")
    fun getListFree(componentIds: List<Long>): MutableList<Component>

    @Query("SELECT * FROM component WHERE componentName = :componentName")
    fun getByName(componentName: String): Component
}
