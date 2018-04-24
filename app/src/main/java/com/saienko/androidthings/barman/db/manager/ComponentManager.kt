package com.saienko.androidthings.barman.db.manager

import com.saienko.androidthings.barman.db.DatabaseCreator
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.db.interfaces.IComponentManager

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/11/17
 * Time: 01:04
 */

class ComponentManager : IComponentManager {

    override suspend fun delete(id: Long) = DatabaseCreator.getDb().componentDao().delete(id)


    override fun allFree(): MutableList<Component> {
        val positionManager = PositionManager()
        val filterList = positionManager.list().filter { it.componentId != -1L }
        val existComponentIds: MutableList<Long> = mutableListOf()
        filterList.forEach {
            existComponentIds.add(it.componentId)
        }
        return allFree(existComponentIds)
    }

    override fun insertAll(vararg components: Component) = DatabaseCreator.getDb().componentDao().insert(*components)

    fun insert(name: String, coefficient: Double) = DatabaseCreator.getDb().componentDao().insert(
            Component(name, coefficient))

    override fun list(): MutableList<Component> = DatabaseCreator.getDb().componentDao().all

    override fun insert(item: Component) = DatabaseCreator.getDb().componentDao().insert(item)

    override suspend fun delete(item: Component) = DatabaseCreator.getDb().componentDao().delete(item)

    override fun update(item: Component) = DatabaseCreator.getDb().componentDao().update(item)

    override fun getByName(componentName: String): Component {
        return DatabaseCreator.getDb().componentDao().getByName(componentName)
    }

    override suspend fun clear() = DatabaseCreator.getDb().componentDao().clear()

//    override fun getById(itemId: Long): Single<Component> {
//        return DatabaseCreator.getDb().componentDao().getById(itemId)
//    }

    override fun get(itemId: Long) = DatabaseCreator.getDb().componentDao().get(itemId)

//    fun allFree(existComponentIds: LongArray): MutableList<Component> {
//        return (if (existComponentIds.isEmpty()) {
//            DatabaseCreator.getDb().componentDao().list
//        } else {
//            val list = DatabaseCreator.getDb().componentDao().list
//            ODO("добвить existComponentIds")
//            return list()
//        })
//    }

//    override fun allFree(component: Component): MutableList<Component> {
//        val list = DatabaseCreator.getDb().componentDao().getListFree(longArrayOf(component.id).toList())
//        list.add(component)
//        return list
//    }

    fun allFree(existComponentIds: MutableList<Long>): MutableList<Component> {
        return (if (existComponentIds.isEmpty()) {
            DatabaseCreator.getDb().componentDao().all
        } else {
            return DatabaseCreator.getDb().componentDao().getListFree(existComponentIds)
        })
    }
}
