package com.saienko.androidthings.barman.db.manager

import com.saienko.androidthings.barman.db.DatabaseCreator
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.db.entity.Motor
import com.saienko.androidthings.barman.db.entity.Position
import com.saienko.androidthings.barman.db.interfaces.IPositionManager
import java.util.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/11/17
 * Time: 10:17
 */

class PositionManager : IPositionManager {
    override fun get(itemId: Long): Position {
        var position: Position? = DatabaseCreator.getDb().positionDao().get(itemId)
        if (position == null) {
            position = Position(-1, -1)
        }
        position.component = getComponent(position)
        position.motor = getMotor(position)
        return position
    }

    override fun list(): MutableList<Position> {
        val motorManager = MotorManager()
        val componentManager = ComponentManager()

        val motors = motorManager.list()
        val unknownMotors = ArrayList<Motor>()
        val positions = DatabaseCreator.getDb().positionDao().all
        positions.forEach {
            if (it.componentId > 0) {
                it.component = componentManager.get(it.componentId)
            }
            if (it.motorId > 0) {
                it.motor = motorManager.get(it.motorId)
            }
        }

        for (motor in motors) {
            var shouldBeAdded = true
            for (position in positions) {
                if (position.motorId == motor.id) {
                    shouldBeAdded = false
                    position.motor = motor
                    position.component = componentManager.get(position.componentId)
                    break
                }
            }
            if (shouldBeAdded) {
                unknownMotors.add(motor)
            }
        }
        for (unknownMotor in unknownMotors) {
            val position = Position(-1, unknownMotor.id)
            position.motor = unknownMotor
            positions.add(position)
        }

        return positions
    }


//    private fun fillPosition(): ObservableTransformer<Position, Position> {
//        return ObservableTransformer { upstream ->
//            upstream
//                    .flatMap { addComponent(it) }
//                    .flatMap { addMotor(it) }
//        }
//    }

    private fun getComponent(position: Position): Component? {
        if (position.componentId < 1) {
            return null
        }
        val componentManager = ComponentManager()
        return componentManager.get(position.componentId)
    }

    private fun getMotor(position: Position): Motor? {
        if (position.motorId < 1) {
            return null
        }
        val motorManager = MotorManager()
        return motorManager.get(position.motorId)
    }

//    override fun getById(itemId: Long): Single<Position> {
//        return DatabaseCreator.getDb().positionDao().getById(itemId)
//                .onErrorReturn { Position(-1, -1) }
//                .toObservable()
//                .compose(fillPosition())
//                .firstOrError()
//    }

    override fun insert(item: Position) = DatabaseCreator.getDb().positionDao().insert(item)

    override suspend fun clear() = DatabaseCreator.getDb().positionDao().clear()

    override suspend fun delete(id: Long) = DatabaseCreator.getDb().positionDao().delete(id)

    override suspend fun delete(item: Position) = DatabaseCreator.getDb().positionDao().delete(item)

    override fun getByMotorId(motorId: Long): Position {
        val motorManager = MotorManager()
        val componentManager = ComponentManager()
        val position = DatabaseCreator.getDb().positionDao().getByMotorId(motorId)
        if (position.componentId > 0) {
            position.component = componentManager.get(position.componentId)
        }
        if (position.motorId > 0) {
            position.motor = motorManager.get(position.motorId)
        }
        return position
    }

    override fun getByComponentId(componentId: Long): Position {
        val motorManager = MotorManager()
        val componentManager = ComponentManager()
        val position = DatabaseCreator.getDb().positionDao().getByComponentId(componentId)
        if (position.componentId > 0) {
            position.component = componentManager.get(position.componentId)
        }
        if (position.motorId > 0) {
            position.motor = motorManager.get(position.motorId)
        }
        return position
    }

    override fun deleteByMotorId(motorId: Long) = DatabaseCreator.getDb().positionDao().deleteByMotorId(motorId)

    override fun update(item: Position) {
        if (item.component != null && item.componentId == 0L) {
            val componentManager = ComponentManager()
            val component = item.component
            if (component != null) {
                val id = componentManager.insert(component)
                item.componentId = id
            }
        }
        return DatabaseCreator.getDb().positionDao().update(item)
    }
}
