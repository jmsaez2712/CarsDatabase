package com.saezgarcia.carsdatabase.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Delete;
import androidx.room.Update;

import com.saezgarcia.carsdatabase.model.entity.Car;
import com.saezgarcia.carsdatabase.model.entity.CarType;
import com.saezgarcia.carsdatabase.model.entity.Type;
import com.saezgarcia.carsdatabase.model.repository.CarRepository;

import java.util.List;

public class CarViewModel extends AndroidViewModel {

    private CarRepository carRepository;

    public CarViewModel(@NonNull Application application) {
        super(application);
        carRepository = new CarRepository(application);
    }

    public long insertCar(Car car) {
        return carRepository.insertCar(car);
    }

    public long insertType(Type type) {
        return carRepository.insertType(type);
    }

    public List<Long> insertCars(Car... cars) {
        return carRepository.insertCars(cars);
    }

    public List<Long> insertTypes(Type... types) {
        return carRepository.insertTypes(types);
    }

    public LiveData<Car> getCar(long id) {
        return carRepository.getCar(id);
    }

    public LiveData<Type> getType(long id) {
        return carRepository.getType(id);
    }

    public LiveData<List<Car>> getCars() {
        return carRepository.getCars();
    }

    public LiveData<List<Type>> getTypes() {
        return carRepository.getTypes();
    }

    public LiveData<List<CarType>> getAllCars() {
        return carRepository.getAllCars();
    }

    public void insertCarType(Car car, Type type) {
        carRepository.insertCarType(car, type);
    }

    public int deleteCar(Car car) {
        return carRepository.deleteCar(car);
    }

    public MutableLiveData<Long> getLiveInsertCar() {
        return carRepository.getLiveInsertCar();
    }

    public int updateCar(Car car) {
        return carRepository.updateCar(car);
    }

    public MutableLiveData<Integer> getLiveUpdateCar() {
        return carRepository.getLiveUpdateCar();
    }

    public MutableLiveData<Integer> getLiveDeleteCar() {
        return carRepository.getLiveDeleteCar();
    }

    public MutableLiveData<Long> getLiveInsertType() {
        return carRepository.getLiveInsertType();
    }

    public MutableLiveData<Integer> getLiveUpdateType() {
        return carRepository.getLiveUpdateType();
    }

    public MutableLiveData<Integer> getLiveDeleteType() {
        return carRepository.getLiveDeleteType();
    }

    public int updateType(Type type) {
        return carRepository.updateType(type);
    }

    public int deleteType(Type type) {
        return carRepository.deleteType(type);
    }
}
