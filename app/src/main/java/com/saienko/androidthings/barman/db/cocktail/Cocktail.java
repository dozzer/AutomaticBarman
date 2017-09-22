package com.saienko.androidthings.barman.db.cocktail;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroup;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 01:56
 */

@Entity(tableName = "cocktail")
public class Cocktail implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public long cocktailId;

    @ColumnInfo(name = "cocktailName")
    private String cocktailName;

    @ColumnInfo(name = "cocktailGroupId")
    private long cocktailGroupId;

    @Ignore List<CocktailElement> cocktailElements;
    @Ignore CocktailGroup         cocktailGroup;

    public Cocktail(String cocktailName, long cocktailGroupId) {
        this.cocktailName = cocktailName;
        this.cocktailGroupId = cocktailGroupId;
    }

    public long getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(long cocktailId) {
        this.cocktailId = cocktailId;
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public void setCocktailName(String cocktailName) {
        this.cocktailName = cocktailName;
    }

    public long getCocktailGroupId() {
        return cocktailGroupId;
    }

    public void setCocktailGroupId(long cocktailGroupId) {
        this.cocktailGroupId = cocktailGroupId;
    }

    public List<CocktailElement> getCocktailElements() {
        return cocktailElements;
    }

    public void setCocktailElements(
            List<CocktailElement> cocktailElements) {
        this.cocktailElements = cocktailElements;
    }

    @Nullable
    public CocktailGroup getCocktailGroup() {
        return cocktailGroup;
    }

    public void setCocktailGroup(CocktailGroup cocktailGroup) {
        if (cocktailGroup != null) {
            this.cocktailGroup = cocktailGroup;
            this.cocktailGroupId = cocktailGroup.getId();
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
                                      dest.writeLong(this.cocktailId);
                                      dest.writeString(this.cocktailName);
                                      dest.writeLong(this.cocktailGroupId);
                                      dest.writeTypedList(this.cocktailElements);
                                      dest.writeParcelable(this.cocktailGroup, flags);
                                  }

    protected Cocktail(Parcel in) {
                                      this.cocktailId = in.readLong();
                                      this.cocktailName = in.readString();
                                      this.cocktailGroupId = in.readLong();
                                      this.cocktailElements = in.createTypedArrayList(CocktailElement.CREATOR);
                                      this.cocktailGroup = in.readParcelable(CocktailGroup.class.getClassLoader());
                                  }

    public static final Creator<Cocktail> CREATOR = new Creator<Cocktail>() {
        @Override
        public Cocktail createFromParcel(Parcel source) {return new Cocktail(source);}

        @Override
        public Cocktail[] newArray(int size) {return new Cocktail[size];}
    };
}
