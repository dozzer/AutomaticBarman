package com.saienko.androidthings.barman.db.cocktail;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import com.saienko.androidthings.barman.db.position.Position;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 01:56
 */

@Entity(tableName = "cocktail_element")//,
//        foreignKeys = @ForeignKey(entity = Cocktail.class,
//                parentColumns = "cocktailId",
//                childColumns = "cocktailId"
//        ))
public class CocktailElement implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo(name = "cocktail_element_component_id")
    public long componentId;

    @ColumnInfo(name = "cocktail_element_volume")
    public int volume;

    @ColumnInfo(name = "cocktailId")
    public long cocktailId;

    @Ignore
    Component component;

    @Ignore Position position;

    public CocktailElement(long componentId, int volume, long cocktailId) {
        this.componentId = componentId;
        this.volume = volume;
        this.cocktailId = cocktailId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public long getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(long cocktailId) {
        this.cocktailId = cocktailId;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.componentId);
        dest.writeInt(this.volume);
        dest.writeLong(this.cocktailId);
        dest.writeParcelable(this.component, flags);
        dest.writeParcelable(this.position, flags);
    }

    protected CocktailElement(Parcel in) {
        this.id = in.readLong();
        this.componentId = in.readLong();
        this.volume = in.readInt();
        this.cocktailId = in.readLong();
        this.component = in.readParcelable(Component.class.getClassLoader());
        this.position = in.readParcelable(Position.class.getClassLoader());
    }

    public static final Creator<CocktailElement> CREATOR = new Creator<CocktailElement>() {
        @Override
        public CocktailElement createFromParcel(Parcel source) {return new CocktailElement(source);}

        @Override
        public CocktailElement[] newArray(int size) {return new CocktailElement[size];}
    };
}
