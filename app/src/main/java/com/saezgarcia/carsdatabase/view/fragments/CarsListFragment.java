package com.saezgarcia.carsdatabase.view.fragments;

import android.bluetooth.le.ScanSettings;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saezgarcia.carsdatabase.R;
import com.saezgarcia.carsdatabase.model.entity.CarType;
import com.saezgarcia.carsdatabase.view.activity.MainActivity;
import com.saezgarcia.carsdatabase.view.adapter.CarAdapter;
import com.saezgarcia.carsdatabase.viewmodel.CarViewModel;

import java.util.List;


public class CarsListFragment extends Fragment {

    private MaterialCardView card;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CarViewModel cmv;

    public CarsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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

        initRecycler(view);
        initComp(view);
        showTitleCollapse(view);
        toolbarListener();
    }

    private void initRecycler(View view){
        RecyclerView rvCar = view.findViewById(R.id.rv);
        rvCar.setLayoutManager(new LinearLayoutManager(getContext()));
        cmv = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
        CarAdapter carAdapter = new CarAdapter(getContext());
        rvCar.setAdapter(carAdapter);

        LiveData<List<CarType>> listCarType = cmv.getAllCars();
        listCarType.observe(getViewLifecycleOwner(), cars -> {
            carAdapter.setCarList(cars);
        });
    }

    private void initComp(View view){
        card = view.findViewById(R.id.card);

        toolbar = view.findViewById(R.id.toolbarFragment);
        toolbar.inflateMenu(R.menu.menu);

        fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener((View v) -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment);
        });
    }

    private void toolbarListener(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.types_opt:
                        NavHostFragment.findNavController(CarsListFragment.this).navigate(R.id.action_FirstFragment_to_typeFragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void showTitleCollapse(View view){
        collapsingToolbarLayout = view.findViewById(R.id.collapsingAppbar);
        appBarLayout = view.findViewById(R.id.appbarFragment);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            boolean isDown = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Car database");
                    isDown = true;
                } else if(isDown) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isDown = false;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).getSupportActionBar().show();
    }

}