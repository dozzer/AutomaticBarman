package com.saienko.androidthings.barman.db.gpio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 14:08
 */

@Entity(tableName = "gpio")
public class Gpio implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long gpioId;

    @ColumnInfo(name = "gpioPin")
    private int gpioPin;

    @ColumnInfo(name = "gpioName")
    private String gpioName;

    public static Gpio[] getRealGpios() {
        ArrayList<Gpio> list = new ArrayList<>();
        list.add(new Gpio(15, "GPIO_10"));
        list.add(new Gpio(35, "GPIO_32"));
        list.add(new Gpio(29, "GPIO_33"));
        list.add(new Gpio(31, "GPIO_34"));
        list.add(new Gpio(13, "GPIO_35"));
        list.add(new Gpio(37, "GPIO_37"));
        list.add(new Gpio(36, "GPIO_39"));
        list.add(new Gpio(22, "GPIO_128"));
        list.add(new Gpio(18, "GPIO_172"));
        list.add(new Gpio(16, "GPIO_173"));
        list.add(new Gpio(40, "GPIO_174"));
        list.add(new Gpio(38, "GPIO_175"));
        Gpio[] out = new Gpio[list.size()];
        out = list.toArray(out);
        return out;
    }

    public Gpio(int gpioPin, String gpioName) {
        this.gpioPin = gpioPin;
        this.gpioName = gpioName;
    }

    public long getGpioId() {
        return gpioId;
    }

    public void setGpioId(long gpioId) {
        this.gpioId = gpioId;
    }

    public int getGpioPin() {
        return gpioPin;
    }

    public void setGpioPin(int gpioPin) {
        this.gpioPin = gpioPin;
    }

    public String getGpioName() {
        return gpioName;
    }

    public void setGpioName(String gpioName) {
        this.gpioName = gpioName;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
                                      dest.writeLong(this.gpioId);
                                      dest.writeInt(this.gpioPin);
                                      dest.writeString(this.gpioName);
                                  }

    protected Gpio(Parcel in) {
                                      this.gpioId = in.readLong();
                                      this.gpioPin = in.readInt();
                                      this.gpioName = in.readString();
                                  }

    public static final Creator<Gpio> CREATOR = new Creator<Gpio>() {
        @Override
        public Gpio createFromParcel(Parcel source) {return new Gpio(source);}

        @Override
        public Gpio[] newArray(int size) {return new Gpio[size];}
    };

    @Override
    public String toString() {
        return "Gpio{" +
               "gpioId=" + gpioId +
               ", gpioPin=" + gpioPin +
               ", gpioName='" + gpioName + '\'' +
               '}';
    }
}
