package com.saienko.androidthings.barman.db.gpio;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 14:08
 */

@Dao
public interface GpioDao {
    @Query("SELECT * FROM gpio")
    List<Gpio> getAll();

    @Insert
    void insertAll(Gpio... gpios);

    @Insert
    void insert(Gpio gpio);

    @Delete
    void delete(Gpio gpio);

    @Query("DELETE FROM gpio")
    void clear();

    @Update
    void update(Gpio gpio);

    @Query("SELECT * FROM gpio WHERE gpioId NOT IN (:gpioIds)")
    List<Gpio> getAllFree(long[] gpioIds);

    @Query("SELECT * FROM gpio WHERE gpioId = :gpioId")
    Gpio get(long gpioId);
}
