package com.saezgarcia.carsdatabase.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.drawable.DrawableUtils;
import com.saezgarcia.carsdatabase.R;
import com.saezgarcia.carsdatabase.model.entity.Car;
import com.saezgarcia.carsdatabase.model.entity.CarType;
import com.saezgarcia.carsdatabase.model.entity.Type;
import com.saezgarcia.carsdatabase.view.viewholder.CarViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class CarAdapter extends RecyclerView.Adapter<CarViewHolder> {

    private List<CarType> carList;
    private Context context;

    public CarAdapter(Context context){this.context = context;}

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarType carType = carList.get(position);
        Car car = carType.car;
        Type type = carType.type;
        holder.car = car;

        holder.tvBrandModel.setText(car.brand+", "+car.model);
        holder.tvYear.setText(context.getString(R.string.year_card) + " " +  String.valueOf(car.year));
        holder.tvHP.setText(context.getString(R.string.hp_card)  + " " +  String.valueOf(car.hp));
        holder.tvType.setText(context.getString(R.string.type_card)  + " " +  type.name);
        holder.tvDate.setText(context.getString(R.string.date_card)  + " " +  car.date);
        Glide.with(context).load(Uri.parse(car.url)).into(holder.ivCar);
        //Picasso.get().load(Uri.parse(car.url)).into(holder.ivCar);
    }

    @Override
    public int getItemCount() {
        if(carList == null){
            return 0;
        }
        return carList.size();
    }

    public void setCarList(List<CarType> carTypeList){
        this.carList = carTypeList;
        notifyDataSetChanged();
    }


}
