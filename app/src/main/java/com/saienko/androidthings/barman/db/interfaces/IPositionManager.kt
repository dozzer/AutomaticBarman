package com.saienko.androidthings.barman.db.interfaces

import com.saienko.androidthings.barman.db.entity.Position

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/11/17
 * Time: 10:15
 */

interface IPositionManager : IBaseManager<Position> {

    fun getByMotorId(motorId: Long): Position

    fun getByComponentId(componentId: Long): Position

    fun deleteByMotorId(motorId: Long)

}
