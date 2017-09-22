package com.saienko.androidthings.barman.db.motor;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 13:32
 */

@Dao
public interface MotorDao {
    @Query("SELECT * FROM motor")
    List<Motor> getAll();

//    @Query("SELECT * FROM motor WHERE uid IN (:motorIds)")
//    List<Motor> loadAllByIds(int[] motorIds);

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND "
//           + "last_name LIKE :last LIMIT 1")
//    Motor findByName(String first, String last);

    @Insert
    void insertAll(Motor... motors);

    @Insert
    void insert(Motor motor);

    @Delete
    void delete(Motor motor);

    @Query("DELETE FROM motor")
    void clear();

    @Update
    void update(Motor motor);

//    @Query("SELECT * FROM motor WHERE motorName LIKE :motorName")
//    Motor getMotorByName(String motorName);

    @Query("SELECT * FROM motor WHERE motorId = :motorId")
    Motor getById(long motorId);
}
