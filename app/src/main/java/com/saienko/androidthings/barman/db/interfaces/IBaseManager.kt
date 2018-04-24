package com.saienko.androidthings.barman.db.interfaces

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 1/16/18
 * Time: 00:37
 */

interface IBaseManager<T> {

    /*suspend*/ fun list(): MutableList<T>

    /*suspend*/ fun get(itemId: Long): T

    suspend fun clear()

    /*suspend*/ fun update(item: T)

    suspend fun delete(id: Long)

    suspend fun delete(item: T)

    /*TODO*//*suspend*/ fun insert(item: T): Long
}
