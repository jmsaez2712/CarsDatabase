package com.saezgarcia.carsdatabase.model.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.saezgarcia.carsdatabase.model.entity.Car;
import com.saezgarcia.carsdatabase.model.entity.CarType;
import com.saezgarcia.carsdatabase.model.entity.Type;
import com.saezgarcia.carsdatabase.model.room.CarDao;
import com.saezgarcia.carsdatabase.model.room.CarDatabase;

import java.util.List;

public class CarRepository {

    private static final String INIT = "init";
    private CarDao carDao;
    private long resultTypeInsert;
    private List<Long> resultTypesInsert;

    private long resultCarInsert;
    private int resultCarUpdate;
    private int resultCarDelete;
    private List<Long> resultCarsInsert;

    private int resultTypeUpdate;
    private int resultTypeDelete;

    private LiveData<List<Car>> liveCarList;
    private LiveData<List<CarType>> liveCarTypeList;
    private LiveData<List<Type>> liveTypeList;

    private LiveData<Car> liveCar;
    private LiveData<Type> liveType;
    private SharedPreferences preferences;

    private MutableLiveData<Long> liveInsertCar;
    private MutableLiveData<Integer> liveUpdateCar;
    private MutableLiveData<Integer> liveDeleteCar;
    private MutableLiveData<Integer> liveUpdateType;
    private MutableLiveData<Integer> liveDeleteType;
    private MutableLiveData<Long> liveInsertType;

    public CarRepository(Context context) {
        CarDatabase db = CarDatabase.getDatabase(context);
        carDao = db.getDao();
        liveInsertCar = new MutableLiveData<>();
        liveUpdateCar = new MutableLiveData<>();
        liveDeleteCar = new MutableLiveData<>();
        liveInsertType = new MutableLiveData<>();
        liveUpdateType = new MutableLiveData<>();
        liveDeleteType = new MutableLiveData<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!getInit()){
            preloadTypes();
            setInit();
        }
        
    }

    public MutableLiveData<Long> getLiveInsertType() {
        return liveInsertType;
    }

    public MutableLiveData<Integer> getLiveUpdateType() {
        return liveUpdateType;
    }

    public MutableLiveData<Integer> getLiveDeleteType() {
        return liveDeleteType;
    }

    public long insertType(Type type) {
        Runnable r  = () ->{
            resultTypeInsert = carDao.insertType(type);
            liveInsertType.postValue(resultTypeInsert);
        };
        new Thread(r).start();
        return this.resultTypeInsert;
    }

    public int updateType(Type type) {
        Runnable r = () ->{
            resultTypeUpdate = carDao.updateType(type);
            liveUpdateType.postValue(resultTypeUpdate);
        };
        new Thread(r).start();
        return resultTypeUpdate;
    }

    public int deleteType(Type type) {
        Runnable r = ()->{
            resultTypeDelete = carDao.deleteType(type);
            liveDeleteType.postValue(resultTypeDelete);
        };
        new Thread(r).start();;
        return resultTypeDelete;
    }

    public MutableLiveData<Long> getLiveInsertCar() {
        return liveInsertCar;
    }

    public MutableLiveData<Integer> getLiveUpdateCar() {
        return liveUpdateCar;
    }

    public MutableLiveData<Integer> getLiveDeleteCar(){return liveDeleteCar;}

    public long insertCar(Car car) {
        Runnable r = () ->{
            resultCarInsert = carDao.insertCar(car);
            liveInsertCar.postValue(resultCarInsert);
        };
        new Thread(r).start();
        return resultCarInsert;
    }



    public void insertCarType(Car car, Type type){
        Runnable r = () ->{
            car.idtype = (int) insertTypeGetId(type);
            if(car.idtype > 0){
                carDao.insertCar(car);
            }
        };
        new Thread(r).start();
    }


    public List<Long> insertCars(Car... cars) {
        Runnable r = () ->{
            resultCarsInsert = carDao.insertCars(cars);
        };
        new Thread(r).start();
        return resultCarsInsert;
    }


    public List<Long> insertTypes(Type... types) {
        Runnable r = () ->{
            resultTypesInsert = carDao.insertTypes(types);
        };
        new Thread(r).start();
        return resultTypesInsert;
    }

    @Delete
    public int deleteCar(Car car) {
        Runnable r = () ->{
            resultCarDelete = carDao.deleteCar(car);
            liveDeleteCar.postValue(resultCarDelete);
        };
        new Thread(r).start();
        return resultCarDelete;
    }

    public LiveData<Car> getCar(long id) {
        if(liveCar == null){
            liveCar = carDao.getCar(id);
        }
        return liveCar;
    }


    public LiveData<Type> getType(long id) {
        if(liveType == null){
            liveType = carDao.getType(id);
        }
        return liveType;
    }


    public LiveData<List<Car>> getCars() {
        if(liveCarList == null){
            liveCarList = carDao.getCars();
        }
        return liveCarList;
    }


    public LiveData<List<Type>> getTypes() {
        if(liveTypeList == null){
            liveTypeList = carDao.getTypes();
        }
        return liveTypeList;
    }


    public LiveData<List<CarType>> getAllCars() {
        if(liveCarTypeList == null){
            liveCarTypeList = carDao.getAllCars();
        }
        return liveCarTypeList;
    }


    public int updateCar(Car car) {
        Runnable r = () ->{
            resultCarUpdate = carDao.updateCar(car);
            try {
                liveUpdateCar.postValue(resultCarUpdate);
            }catch (Exception e){}
        };
        new Thread(r).start();
        return resultCarUpdate;
    }

    private long insertTypeGetId(Type type){
        List<Long> ids = carDao.insertTypes(type);
        if(ids.get(0) < 1){
            return carDao.getIdType(type.name);
        } else {
            return ids.get(0);
        }
    }



    private void preloadTypes(){
        String[] typeNames = new String[] {"Van","Sports Car", "Sedan", "Wagon", "Hatchback", "Convertible", "SUV", "Coupe", "Truck", "Pickup"};
        Type[] types = new Type[typeNames.length];
        Type type;
        int cont = 0;
        for (String s: typeNames) {
            type = new Type();
            type.name = s;
            types[cont] = type;
            cont++;
        }
        insertTypes(types);
    }

    public void setInit() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(INIT, true);
        editor.commit();
    }

    public boolean getInit() {
        return preferences.getBoolean(INIT, false);
    }

}
