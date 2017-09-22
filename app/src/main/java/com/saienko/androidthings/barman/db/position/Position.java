package com.saienko.androidthings.barman.db.position;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import com.saienko.androidthings.barman.db.cocktail.Component;
import com.saienko.androidthings.barman.db.motor.Motor;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/12/17
 * Time: 11:45
 */

@Entity
public class Position implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo
    private long componentId;

    @ColumnInfo
    private long motorId;

    @Ignore
    private Component component;

    @Ignore
    private Motor motor;

    public Position(long componentId, long motorId) {
        this.componentId = componentId;
        this.motorId = motorId;
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

    public long getMotorId() {
        return motorId;
    }

    public void setMotorId(long motorId) {
        this.motorId = motorId;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        if (component != null) {
            setComponentId(component.getId());
            this.component = component;
        }
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.componentId);
        dest.writeLong(this.motorId);
        dest.writeParcelable(this.component, flags);
        dest.writeParcelable(this.motor, flags);
    }

    protected Position(Parcel in) {
        this.id = in.readLong();
        this.componentId = in.readLong();
        this.motorId = in.readLong();
        this.component = in.readParcelable(Component.class.getClassLoader());
        this.motor = in.readParcelable(Motor.class.getClassLoader());
    }

    public static final Creator<Position> CREATOR = new Creator<Position>() {
        @Override
        public Position createFromParcel(Parcel source) {return new Position(source);}

        @Override
        public Position[] newArray(int size) {return new Position[size];}
    };
}
