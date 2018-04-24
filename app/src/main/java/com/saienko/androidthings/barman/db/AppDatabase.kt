package com.saienko.androidthings.barman.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.saienko.androidthings.barman.db.dao.*
import com.saienko.androidthings.barman.db.entity.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 13:34
 */

@Database(
        entities = [(Motor::class), (Gpio::class), (Cocktail::class), (Component::class), (CocktailElement::class), (Position::class)],
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun motorDao(): MotorDao

    abstract fun gpioDao(): GpioDao

    abstract fun cocktailDao(): CocktailDao

    abstract fun componentDao(): ComponentDao

    abstract fun cocktailElementDao(): CocktailElementDao

    abstract fun positionDao(): PositionDao

    companion object {
        const val DATABASE_NAME = "alco"
    }

//    companion object {
//        private lateinit var context: Context
//        private val database: AppDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
//            Room.databaseBuilder(context, AppDatabase::class.java, "alco.db").build()
//        }
//
//        fun init(context: Context) {
//            this.context = context.applicationContext
//        }
//
//        fun getDb(): AppDatabase {
//            return database
//        }
//
//    }
}