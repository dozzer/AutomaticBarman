package com.saienko.androidthings.barman.db.drinkGroup;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 14:08
 */

@Entity(tableName = "cocktail_group")
public class CocktailGroup implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "cocktailGroupName")
    private String drinkGroupName;

    public CocktailGroup(String drinkGroupName) {
        this.drinkGroupName = drinkGroupName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDrinkGroupName() {
        return drinkGroupName;
    }

    public void setDrinkGroupName(String drinkGroupName) {
        this.drinkGroupName = drinkGroupName;
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
                                      dest.writeLong(this.id);
                                      dest.writeString(this.drinkGroupName);
                                  }

    protected CocktailGroup(Parcel in) {
                                      this.id = in.readLong();
                                      this.drinkGroupName = in.readString();
                                  }

    public static final Parcelable.Creator<CocktailGroup> CREATOR = new Parcelable.Creator<CocktailGroup>() {
        @Override
        public CocktailGroup createFromParcel(Parcel source) {return new CocktailGroup(source);}

        @Override
        public CocktailGroup[] newArray(int size) {return new CocktailGroup[size];}
    };
}
