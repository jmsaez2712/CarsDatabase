package com.saezgarcia.carsdatabase.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.sql.Date;


@Entity(tableName = "cars",
        indices = {@Index(value = "model", unique = true)},
        foreignKeys = {@ForeignKey(entity = Type.class, parentColumns = "id", childColumns = "idtype", onDelete = ForeignKey.CASCADE)})

public class Car implements Parcelable {

    public Car() {
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "brand")
    public String brand;

    @ColumnInfo(name = "model")
    public String model;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "hp")
    public int hp;

    @ColumnInfo(name = "idtype")
    public int idtype;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "date")
    public String date;


    protected Car(Parcel in) {
        id = in.readInt();
        brand = in.readString();
        model = in.readString();
        year = in.readInt();
        hp = in.readInt();
        idtype = in.readInt();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(brand);
        dest.writeString(model);
        dest.writeInt(year);
        dest.writeInt(hp);
        dest.writeInt(idtype);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}
