package com.saezgarcia.carsdatabase.view.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.saezgarcia.carsdatabase.R;
import com.saezgarcia.carsdatabase.model.entity.Car;
import com.saezgarcia.carsdatabase.model.entity.Type;
import com.saezgarcia.carsdatabase.viewmodel.CarViewModel;

import java.util.Calendar;
import java.util.List;


public class AddCarFragment extends Fragment {

    private final Calendar c = Calendar.getInstance();

    private int day = c.get(Calendar.DAY_OF_MONTH);
    private int month = c.get(Calendar.MONTH);
    private int year = c.get(Calendar.YEAR);

    private ActivityResultLauncher<String> mGetContent;

    private Spinner spType;
    private TextInputLayout tlBrand, tlModel, tlYear, tlHp, tlDate;
    private EditText etBrand, etModel, etYear, etHP, etDate;
    private ImageView ivAddCar;
    private Button btAddEdit, btCancel, btDelete;
    private Uri selectedUri;
    private String defaultPhoto = "https://illustoon.com/photo/1742.png";
    private static boolean firstType = true;

    private Bundle bundle;

    private Car car;

    private int cont = 0;

    private CarViewModel cvm;

    public AddCarFragment() {
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
        return inflater.inflate(R.layout.fragment_add_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = getArguments();
        initComp(view);
        loadSpinner();
        try{
            car = bundle.getParcelable("car");
            loadEdit();
        }catch (Exception e){
            loadAdd(view);
        }

    }

    private void loadSpinner() {
        cvm.getTypes().observe(getViewLifecycleOwner(), types -> {
            if(firstType){
                Type type = new Type();
                type.id = 0;
                type.name = getString(R.string.default_select);
                types.add(0, type);
                firstType = false;
            }
            ArrayAdapter<Type> adapter =
                    new ArrayAdapter<Type>(this.getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, types);
            this.spType.setAdapter(adapter);
            /*FOR EACH COMPARANDO CATEGORIA ENTRE COCHE Y LA DEL LIST DEL LIVE DATA, PARA PONERLO EN EL SPINNER SI EXISTE*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try{
                    types.forEach((t)->{
                        if(t.id == car.idtype){
                            int pos = types.indexOf(t);
                            spType.setSelection(pos);
                        }
                    });
                } catch (Exception e){}

            }

        });
    }

    private void loadEdit() {

        btAddEdit.setText("Edit");
        btDelete.setVisibility(View.VISIBLE);

        etBrand.setText(car.brand);
        etModel.setText(car.model);
        etYear.setText(String.valueOf(car.year));
        etHP.setText(String.valueOf(car.hp));
        etDate.setText(car.date);

        Log.v("XXX", ""+car.idtype);

        Glide.with(this).load(Uri.parse(car.url)).into(ivAddCar);

        etDate.setOnFocusChangeListener((v, hasFocus)->{
            if(hasFocus){
                obtainDate();
            }
        });

        btCancel.setOnClickListener((View v)->{
            NavHostFragment.findNavController(this).popBackStack();
        });

        btAddEdit.setOnClickListener((View v)->{
            try {
                Car c = carToUpdate();
                if (isCarValid(c)) {
                    cvm.updateCar(c);
                    wasUpdated();
                } else {
                    showEmpty();
                    Toast.makeText(getContext(), "Something was wrong", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                showEmpty();
            }
        });

        btDelete.setOnClickListener((View v)->{
           alert();
        });

        pickImageEdit();
        getTextChange();
        //showEmpty();
    }


    private void loadAdd(View view){

        btDelete.setVisibility(View.GONE);

        Glide.with(this).load(R.drawable.add_24).into(ivAddCar);

        etDate.setOnFocusChangeListener((v, hasFocus)->{
            if(hasFocus){
                obtainDate();
            }
        });

        btCancel = view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener((View v)->{
            NavHostFragment.findNavController(this).popBackStack();
        });

        btAddEdit = view.findViewById(R.id.btAdd);
        btAddEdit.setOnClickListener((View v)->{
            try {
                Car c = carToInsert();
                if(isCarValid(c)){
                    cvm.insertCar(c);
                    wasInserted();
                } else {
                    showEmpty();
                    Toast.makeText(getContext(), "Error inserting", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                showEmpty();
            }


        });

        pickImageAdd();
        getTextChange();
    }

    private Car carToInsert(){
        String brand = etBrand.getText().toString();
        String model = etModel.getText().toString();
        String year = etYear.getText().toString();
        String hp = etHP.getText().toString();
        String date = etDate.getText().toString();

        Type type = (Type) spType.getSelectedItem();
        Car c = new Car();
        c.brand = brand;
        c.model = model;
        c.idtype = type.id;
        c.year = Integer.parseInt(year);
        c.hp = Integer.parseInt(hp);
        try {
            c.url = selectedUri.toString();
        }catch (Exception e){
            c.url = defaultPhoto;
        }
        c.date = date;
        Log.v("xyzv", c.url);
        return c;
    }

    private Car carToUpdate(){
        String brand = etBrand.getText().toString();
        String model = etModel.getText().toString();
        String year = etYear.getText().toString();
        String hp = etHP.getText().toString();
        String date = etDate.getText().toString();
        Type type = (Type) spType.getSelectedItem();
        Car c = new Car();
        c.brand = brand;
        c.model = model;
        c.idtype = type.id;
        c.year = Integer.parseInt(year);
        c.hp = Integer.parseInt(hp);
        try {
            c.url = selectedUri.toString();
        }catch (Exception e){
            c.url = car.url;
        }
        c.date = date;
        c.id = this.car.id;
        Log.v("xyzv", c.url);
        return c;
    }

    private boolean isCarValid(Car car){
        return !(car.brand.isEmpty() || car.model.isEmpty() || car.year <= 0 || car.hp <=0 || car.idtype <= 0);
    }

    private void obtainDate(){
        DatePickerDialog date = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String formattedMonth = (i1 < 10) ? String.valueOf("0"+i1) : String.valueOf(i1);
                String formattedDay = (i2 < 10) ? String.valueOf("0"+i2) : String.valueOf(i2);
                etDate.setText(i+"/"+formattedMonth+"/"+formattedDay);
            }
        },year, month, day);
        date.show();
    }

    private void initComp(View view){
        spType = view.findViewById(R.id.spinner);


        tlBrand = view.findViewById(R.id.tlBrand);
        tlModel = view.findViewById(R.id.tlModel);
        tlYear = view.findViewById(R.id.tlYear);
        tlHp = view.findViewById(R.id.tlHp);
        tlDate = view.findViewById(R.id.tlDate);

        etBrand = view.findViewById(R.id.etBrand);
        etModel = view.findViewById(R.id.etModel);
        etYear = view.findViewById(R.id.etYear);
        etHP = view.findViewById(R.id.etHp);
        etDate = view.findViewById(R.id.etDate);

        btCancel = view.findViewById(R.id.btCancel);
        btAddEdit = view.findViewById(R.id.btAdd);
        btDelete = view.findViewById(R.id.btDelete);

        ivAddCar = view.findViewById(R.id.ivAddCar);

        cvm = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
        loadSpinner();
    }


    private void pickImageEdit(){
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        //Log.d("XXX", uri.toString());
                        //binding.imageView.setImageURI(uri);
                        if(uri != null){
                            Glide.with(getActivity())
                                    .load(uri)
                                    .into(ivAddCar);
                            selectedUri = uri;
                        } else {
                            Glide.with(getActivity())
                                    .load(Uri.parse(car.url))
                                    .into(ivAddCar);
                            selectedUri = uri;
                        }

                    }
                });

        ivAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");

            }
        });
    }

    private void pickImageAdd(){
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        //Log.d("XXX", uri.toString());
                        //binding.imageView.setImageURI(uri);
                        if(uri != null){
                            Glide.with(getActivity())
                                    .load(uri)
                                    .into(ivAddCar);
                            selectedUri = uri;
                        } else {
                            Glide.with(getActivity())
                                    .load(R.drawable.add_24)
                                    .into(ivAddCar);
                        }

                    }
                });

        ivAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });
    }

    private void wasInserted(){
        cvm.getLiveInsertCar().observe(getViewLifecycleOwner(), car -> {
            Log.v("XXX", ""+car);
            if(car.longValue() > 0){
                Toast.makeText(getContext(), "Car inserted", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                Toast.makeText(getContext(), "Error inserting car", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void wasUpdated(){
        cvm.getLiveUpdateCar().observe(getViewLifecycleOwner(), car -> {
            Log.v("XXX", ""+car);
            if(car > 0){
                Toast.makeText(getContext(), "Car updated", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                Toast.makeText(getContext(), "Error updating car", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void wasDeleted(){
        cvm.getLiveDeleteCar().observe(getViewLifecycleOwner(), car ->{
            if(car > 0){
                Toast.makeText(getContext(), "Car deleted", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                Toast.makeText(getContext(), "Error deleting car", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTextChange(){
        etBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() <= 0){
                    tlBrand.setError(getString(R.string.error_field));
                    tlBrand.setErrorEnabled(true);
                } else {
                    tlBrand.setError("");
                    tlBrand.setErrorEnabled(false);
                }
            }
        });

        etModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() <= 0){
                    tlModel.setError(getString(R.string.error_field));
                    tlModel.setErrorEnabled(true);
                } else {
                    tlModel.setError("");
                    tlModel.setErrorEnabled(false);
                }
            }
        });

        etYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() <= 0){
                    tlYear.setError(getString(R.string.error_field));
                    tlYear.setErrorEnabled(true);
                } else {
                    tlYear.setError("");
                    tlYear.setErrorEnabled(false);
                }
            }
        });

        etHP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() <= 0){
                    tlHp.setError(getString(R.string.error_field));
                    tlHp.setErrorEnabled(true);
                } else {
                    tlHp.setError("");
                    tlHp.setErrorEnabled(false);
                }
            }
        });

        etDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() <= 0){
                    tlDate.setError(getString(R.string.error_field));
                    tlDate.setErrorEnabled(true);
                } else {
                    tlDate.setError("");
                    tlDate.setErrorEnabled(false);
                }
            }
        });
    }



    private void showEmpty(){
        if(etBrand.getText().toString().isEmpty()){
            tlBrand.setError(getString(R.string.empty_fields));
            tlBrand.setErrorEnabled(true);
        } else {
            tlBrand.setError("");
            tlBrand.setErrorEnabled(false);
        }

        if(etModel.getText().toString().isEmpty()){
            tlModel.setError(getString(R.string.empty_fields));
            tlModel.setErrorEnabled(true);
        } else {
            tlModel.setError("");
            tlModel.setErrorEnabled(false);
        }

        if(etYear.getText().toString().isEmpty()){
            tlYear.setError(getString(R.string.empty_fields));
            tlYear.setErrorEnabled(true);
        } else {
            tlYear.setError("");
            tlYear.setErrorEnabled(false);
        }

        if(etHP.getText().toString().isEmpty()){
            tlHp.setError(getString(R.string.empty_fields));
            tlHp.setErrorEnabled(true);
        } else {
            tlHp.setError("");
            tlHp.setErrorEnabled(false);
        }

        if(etDate.getText().toString().isEmpty()){
            tlDate.setError(getString(R.string.empty_fields));
            tlDate.setErrorEnabled(true);
        } else {
            tlDate.setError("");
            tlDate.setErrorEnabled(false);
        }
    }

    private void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Car")
                .setMessage("Are you totally sure? This action can not be rejected")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cvm.deleteCar(car);
                        wasDeleted();
                        Toast.makeText(getContext(), "The car was deleted", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(AddCarFragment.this).popBackStack();
                        firstType = true;
                    }
                });
        builder.create().show();
    }
}