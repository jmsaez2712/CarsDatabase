package com.saezgarcia.carsdatabase.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saezgarcia.carsdatabase.R;
import com.saezgarcia.carsdatabase.model.entity.CarType;
import com.saezgarcia.carsdatabase.view.adapter.CarAdapter;
import com.saezgarcia.carsdatabase.viewmodel.CarViewModel;

import java.util.List;


public class CarsListFragment extends Fragment {

    private MaterialCardView card;
    private ActionMode actionMode;
    private CarViewModel cmv;

    public CarsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cars_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvCar = view.findViewById(R.id.rv);
        rvCar.setLayoutManager(new LinearLayoutManager(getContext()));
        cmv = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
        CarAdapter carAdapter = new CarAdapter(getContext());

        card = view.findViewById(R.id.card);
        rvCar.setAdapter(carAdapter);

        LiveData<List<CarType>> listCarType = cmv.getAllCars();
        listCarType.observe(getViewLifecycleOwner(), cars -> {
            carAdapter.setCarList(cars);
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener((View v) -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment);
        });


        }



    /*@Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_delete);
        MenuItem item2 = menu.findItem(R.id.action_edit);
        item.setVisible(card.isChecked() && card != null);
        item2.setVisible(card.isChecked() && card != null);
    }*/


}