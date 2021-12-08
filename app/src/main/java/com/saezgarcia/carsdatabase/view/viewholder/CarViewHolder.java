package com.saezgarcia.carsdatabase.view.viewholder;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.saezgarcia.carsdatabase.R;
import com.saezgarcia.carsdatabase.model.entity.Car;

public class CarViewHolder extends RecyclerView.ViewHolder {

    public TextView tvBrandModel, tvYear, tvHP, tvType, tvDate;
    public ImageView ivCar;
    private MaterialCardView card;
    public Car car;


    public CarViewHolder(@NonNull View itemView) {
        super(itemView);
        tvBrandModel = itemView.findViewById(R.id.tvBrandModel);
        tvYear = itemView.findViewById(R.id.tvYear);
        tvHP = itemView.findViewById(R.id.tvHp);
        tvType = itemView.findViewById(R.id.tvType);
        ivCar = itemView.findViewById(R.id.ivCar);
        card = itemView.findViewById(R.id.card);
        tvDate = itemView.findViewById(R.id.tvDate);

        itemView.setOnClickListener((View v)->{
            Bundle bundle = new Bundle();
            bundle.putParcelable("car", car);
            Navigation.findNavController(itemView).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
        });

    }

}
