package com.saezgarcia.carsdatabase.model.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.saezgarcia.carsdatabase.model.entity.Car;
import com.saezgarcia.carsdatabase.model.entity.CarType;
import com.saezgarcia.carsdatabase.model.entity.Type;

import java.util.List;

@Dao
public interface CarDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertCar(Car car);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertType(Type type);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertCars(Car ...cars);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertTypes(Type ...types);

    @Update
    int updateCar(Car car);

    @Delete
    int deleteCar(Car car);

    @Query("SELECT * FROM cars WHERE id = :id ")
    LiveData<Car> getCar(long id);

    @Query("SELECT * FROM cartype WHERE id = :id")
    LiveData<Type> getType(long id);

    @Query("SELECT * FROM cars ORDER BY brand ASC")
    LiveData<List<Car>> getCars();

    @Query("SELECT * FROM cartype ORDER BY id ASC")
    LiveData<List<Type>> getTypes();

    @Query("select c.*, ct.id type_id, ct.name type_name " +
            "from cars c join cartype ct on c.idtype = ct.id order by " +
            "name asc")
    LiveData<List<CarType>> getAllCars();

    @Query("select id from cartype where name = :name")
    long getIdType(String name);


}
