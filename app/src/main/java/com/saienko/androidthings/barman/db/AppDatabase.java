package com.saienko.androidthings.barman.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.saienko.androidthings.barman.db.cocktail.*;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroup;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroupDao;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.db.gpio.GpioDao;
import com.saienko.androidthings.barman.db.motor.Motor;
import com.saienko.androidthings.barman.db.motor.MotorDao;
import com.saienko.androidthings.barman.db.position.Position;
import com.saienko.androidthings.barman.db.position.PositionDao;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 13:34
 */

@Database(entities = {Motor.class, Gpio.class, Cocktail.class, CocktailGroup.class, Component.class,
                      com.saienko.androidthings.barman.db.cocktail.CocktailElement.class,
                      Position.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract MotorDao motorDao();

    public abstract GpioDao gpioDao();

    public abstract CocktailDao cocktailDao();

    public abstract CocktailGroupDao cocktailGroup();

    public abstract ComponentDao componentDao();

    public abstract CocktailElementDao cocktailElementDao();

    public abstract PositionDao positionDao();

    public static void init(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "db_v1.db").build();
        } else {
            throw new UnsupportedOperationException("Unknown action");
        }
    }

    public static AppDatabase getDb() {
        return instance;
    }


}