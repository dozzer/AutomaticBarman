package com.saienko.androidthings.barman.db.bottle;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.saienko.androidthings.barman.db.cocktail.Component;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 01:58
 */

@Entity(tableName = "bottle")
public class Bottle {

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo(name = "bottle_name")
    String name;

    @ColumnInfo(name = "bottle_volume")
    int volume;

    @ColumnInfo(name = "bottle_component")
    Component component;
}
