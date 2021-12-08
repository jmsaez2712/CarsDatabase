package com.saezgarcia.carsdatabase.model.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.saezgarcia.carsdatabase.model.entity.Car;
import com.saezgarcia.carsdatabase.model.entity.Type;

@Database(entities = {Car.class, Type.class}, version = 1, exportSchema = false)
public abstract class CarDatabase extends RoomDatabase {

    public abstract CarDao getDao();

    private static volatile CarDatabase INSTANCE;

    /**/
    public static CarDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CarDatabase.class, "cars").build();
        }

        return INSTANCE;
    }
}
