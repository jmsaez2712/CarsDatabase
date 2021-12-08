package com.saezgarcia.carsdatabase.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.saezgarcia.carsdatabase.R;
import com.saezgarcia.carsdatabase.model.entity.Type;
import com.saezgarcia.carsdatabase.viewmodel.CarViewModel;


public class TypeFragment extends Fragment {

    private CarViewModel cvm;
    private Spinner spType;

    private EditText etNameType;
    private TextInputLayout tlNameType;
    private Button btEdit, btDelete, btCancel;
    private RadioButton cbAdd, cbEdit;
    private RadioGroup rbGroup;
    private static boolean firstType = true;
    private ArrayAdapter<Type> adapter;
    private TextView tvSupport;

    public TypeFragment() {
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
        return inflater.inflate(R.layout.fragment_type, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstType = true;
        initialize(view);

        if(cbAdd.isChecked()){
            loadSpinner();
            loadAdd();
        }

        cbAdd.setOnCheckedChangeListener((compoundButton, b) -> {
            if(cbAdd.isChecked()){
                loadSpinner();
                loadAdd();
            }
        });



        cbEdit.setOnCheckedChangeListener((compoundButton, b) -> {
            if(cbEdit.isChecked()){
                loadSpinner();
                loadEdit();
            }
        });


    }

    private void getViewModel(){
        cvm = new ViewModelProvider(requireActivity()).get(CarViewModel.class);
    }

    public Type createType(){
        Type type = new Type();
        type.name = etNameType.getText().toString();
        return type;
    }

    public Type updateType(){
        Type type = (Type) spType.getSelectedItem();
        type.name = etNameType.getText().toString();
        return type;
    }

    public Type deleteType(){
       Type type = (Type) spType.getSelectedItem();
       return type;
    }

    private void initialize(View view){
        spType = view.findViewById(R.id.spinner2);

        btDelete = view.findViewById(R.id.btDeleteType);
        btEdit = view.findViewById(R.id.btEditType);
        btCancel = view.findViewById(R.id.btCancelType);

        tlNameType = view.findViewById(R.id.tlTypeName);
        etNameType = view.findViewById(R.id.etTypeName);

        cbAdd = view.findViewById(R.id.rbAdd);
        cbEdit = view.findViewById(R.id.rbEdit);
        rbGroup = view.findViewById(R.id.rbGroup);

        tvSupport = view.findViewById(R.id.tvSupport);

        getViewModel();
    }

    private void loadSpinner(){

        cvm.getTypes().observe(getViewLifecycleOwner(), types -> {
                if(firstType){
                    Type type = new Type();
                    type.id = 0;
                    type.name = getString(R.string.default_select);
                    types.add(0, type);
                    firstType = false;
                }

                 adapter =
                        new ArrayAdapter<Type>(this.getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, types);
                this.spType.setAdapter(adapter);
        });
    }

    private void loadAdd(){

        spType.setEnabled(false);

        btDelete.setVisibility(View.GONE);

        btEdit.setText("Add");
        tlNameType.setHint(R.string.hint_add_type);
        tvSupport.setText(R.string.support_text_add_type);

        btEdit.setOnClickListener((View v)->{
            Type type = createType();
            cvm.insertType(type);
            cvm.getLiveInsertType().observe(getViewLifecycleOwner(), aLong -> {
                if(aLong > 0){
                    NavHostFragment.findNavController(this).navigate(R.id.action_typeFragment_to_FirstFragment);
                }
            });
        });

        btCancel.setOnClickListener((View v)->{
            NavHostFragment.findNavController(this).navigate(R.id.action_typeFragment_to_FirstFragment);
        });

    }

    private void loadEdit(){

        spType.setEnabled(true);

        btDelete.setVisibility(View.VISIBLE);

        btEdit.setText("Edit");
        tlNameType.setHint(R.string.hint_edit_delete_type);
        tvSupport.setText(R.string.support_text_delete_type);
        btEdit.setOnClickListener((View v)->{
            Type type = updateType();
            cvm.updateType(type);
            cvm.getLiveUpdateType().observe(getViewLifecycleOwner(), integer -> {
                if(integer == 1){
                    NavHostFragment.findNavController(this).navigate(R.id.action_typeFragment_to_FirstFragment);
                }
            });
        });

        btDelete.setOnClickListener((View v)->{
            Type type = deleteType();
            cvm.deleteType(type);
            cvm.getLiveDeleteType().observe(getViewLifecycleOwner(), integer -> {
                if(integer == 1){
                    NavHostFragment.findNavController(this).navigate(R.id.action_typeFragment_to_FirstFragment);
                }
            });
        });


        btCancel.setOnClickListener((View v)->{
            NavHostFragment.findNavController(this).navigate(R.id.action_typeFragment_to_FirstFragment);
        });
    }

}