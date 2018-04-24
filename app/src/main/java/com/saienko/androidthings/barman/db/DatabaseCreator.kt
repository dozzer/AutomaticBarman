package com.saienko.androidthings.barman.db

import android.arch.persistence.room.Room
import android.content.Context
import com.saienko.androidthings.barman.db.AppDatabase.Companion.DATABASE_NAME
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 1/13/18
 * Time: 21:15
 */

object DatabaseCreator {

    private lateinit var database: AppDatabase

    private val mInitializing = AtomicBoolean(true)

    fun createDb(context: Context) {
        if (mInitializing.compareAndSet(true, false).not()) {
            return
        }
        launch {
            database = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }

    fun getDb() = database

}