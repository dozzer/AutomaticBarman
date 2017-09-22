package com.saienko.androidthings.barman.db.position;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/12/17
 * Time: 11:57
 */

@Dao
public interface PositionDao {

    @Query("SELECT * FROM Position")
    List<Position> getAll();

//    @Insert
//    void insertAll(Position... positions);

    @Insert
    void insert(Position position);

    @Delete
    void delete(Position position);

    @Query("DELETE FROM Position")
    void clear();

    @Update
    void update(Position position);

    @Query("SELECT * FROM Position WHERE motorId = :motorId")
    Position getByMotorId(long motorId);

    @Query("SELECT * FROM Position WHERE componentId = :componentId")
    Position getByComponentId(long componentId);

    @Query("DELETE FROM Position WHERE motorId = :motorId")
    void deleteByMotorId(long motorId);
}
