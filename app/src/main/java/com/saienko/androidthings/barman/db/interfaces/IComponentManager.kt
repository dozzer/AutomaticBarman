package com.saienko.androidthings.barman.db.interfaces

import com.saienko.androidthings.barman.db.entity.Component

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/11/17
 * Time: 01:00
 */

interface IComponentManager : IBaseManager<Component> {

    fun allFree(): MutableList<Component>

    fun insertAll(vararg components: Component)

    fun getByName(componentName: String): Component

}
