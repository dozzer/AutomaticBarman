package com.saienko.androidthings.barman.db.cocktail;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 01:58
 */

@Entity(tableName = "component")
public class Component implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "component_name")
    private String name;

    public Component(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
                                      dest.writeLong(this.id);
                                      dest.writeString(this.name);
                                  }

    protected Component(Parcel in) {
                                      this.id = in.readLong();
                                      this.name = in.readString();
                                  }

    public static final Creator<Component> CREATOR = new Creator<Component>() {
        @Override
        public Component createFromParcel(Parcel source) {return new Component(source);}

        @Override
        public Component[] newArray(int size) {return new Component[size];}
    };
}
