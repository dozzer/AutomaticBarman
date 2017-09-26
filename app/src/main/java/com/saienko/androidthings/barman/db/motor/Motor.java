package com.saienko.androidthings.barman.db.motor;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import com.saienko.androidthings.barman.db.gpio.Gpio;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 13:30
 */

@Entity(tableName = "motor")
public class Motor implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long motorId;


    @ColumnInfo
    private int motorSpeed;

    @ColumnInfo
    private long gpioId;

    @Ignore
    private Gpio gpio;

    public Motor(int motorSpeed, long gpioId) {
        this.motorSpeed = motorSpeed;
        this.gpioId = gpioId;
    }

    public long getMotorId() {
        return motorId;
    }

    void setMotorId(long motorId) {
        this.motorId = motorId;
    }

    public String getMotorName() {
        return gpio.getGpioName() + " " + gpio.getGpioPin();
    }

    public int getMotorSpeed() {
        return motorSpeed;
    }

    public void setMotorSpeed(int motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    public Gpio getGpio() {
        return gpio;
    }

    public void setGpio(Gpio gpio) {
        this.gpio = gpio;
    }

    public long getGpioId() {
        return gpioId;
    }

    public void setGpioId(long gpioId) {
        this.gpioId = gpioId;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.motorId);
        dest.writeInt(this.motorSpeed);
        dest.writeLong(this.gpioId);
        dest.writeParcelable(this.gpio, flags);
    }

    protected Motor(Parcel in) {
        this.motorId = in.readLong();
        this.motorSpeed = in.readInt();
        this.gpioId = in.readLong();
        this.gpio = in.readParcelable(Gpio.class.getClassLoader());
    }

    public static final Creator<Motor> CREATOR = new Creator<Motor>() {
        @Override
        public Motor createFromParcel(Parcel source) {return new Motor(source);}

        @Override
        public Motor[] newArray(int size) {return new Motor[size];}
    };

    @Override
    public String toString() {
        return "Motor{" +
               "motorId=" + motorId +
               ", motorSpeed=" + motorSpeed +
               ", gpioId=" + gpioId +
               ", gpio=" + gpio +
               '}';
    }
}
