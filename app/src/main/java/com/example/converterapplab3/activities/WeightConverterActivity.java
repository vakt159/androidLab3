package com.example.converterapplab3.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.converterapplab3.ActivityState;
import com.example.converterapplab3.services.ConversionType;
import com.example.converterapplab3.R;
import com.example.converterapplab3.RetainFragment;
import com.example.converterapplab3.services.WeightConverterService;


public class WeightConverterActivity extends AppCompatActivity implements RetainFragment.ActivityListener {

    private RetainFragment retainFragment;
    private WeightConverterService weightConverterService;
    private String[] valueTypes = {"Pound", "Pud", "Kilogram", "Gram", "Tonne", "Carat"};
    private Spinner spinnerFrom, spinnerTo;
    private EditText fromTextEdit, toTextEdit;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<String> adapterItems;
    public static final String SPINNER_FROM = "SPINNER_FROM";
    public static final String SPINNER_TO = "SPINNER_TO";
    public static final String FROM = "FROM";
    public static final String TO = "TO";

    @Override

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_FROM, spinnerFrom.getId());
        outState.putInt(SPINNER_TO, spinnerTo.getId());
        outState.putString(FROM, fromTextEdit.getText().toString());
        outState.putString(TO, toTextEdit.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_converter);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        spinnerFrom = findViewById(R.id.fromValue);
        spinnerTo = findViewById(R.id.toValue);

        fromTextEdit = findViewById(R.id.fromInput);
        toTextEdit = findViewById(R.id.toOutput);


        if (savedInstanceState != null) {
            spinnerFrom.setId(savedInstanceState.getInt(SPINNER_FROM));
            spinnerTo.setId(savedInstanceState.getInt(SPINNER_TO));
            fromTextEdit.setText(savedInstanceState.getString(FROM));
            toTextEdit.setText(savedInstanceState.getString(TO));
        }

        retainFragment = (RetainFragment) getSupportFragmentManager().findFragmentByTag(RetainFragment.TAG);

        if (retainFragment == null) {
            retainFragment = new RetainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(retainFragment, RetainFragment.TAG)
                    .commit();
        }
        adapterItems = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, valueTypes);
        spinnerFrom.setAdapter(adapterItems);
        spinnerTo.setAdapter(adapterItems);

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fromTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                update();
            }
        });
        findViewById(R.id.lengthButton).setOnClickListener(view -> {
            Intent intent1 = new Intent(this, LengthConverterActivity.class);
            startActivity(intent1);
        });
        findViewById(R.id.temperatureButton).setOnClickListener(view -> {
            Intent intent1 = new Intent(this, TemperatureConverterActivity.class);
            startActivity(intent1);
        });
        retainFragment.setListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        retainFragment.setListener(null);
    }

    private void update() {
        if (!fromTextEdit.getText().toString().equals("") && !spinnerFrom.getSelectedItem().toString().equals("")) {
            String input = fromTextEdit.getText().toString();
            String fromValue = spinnerFrom.getSelectedItem().toString().toUpperCase();
            String toValue = spinnerTo.getSelectedItem().toString().toUpperCase();
            retainFragment.convert(ConversionType.WEIGHT, Double.valueOf(input), fromValue, toValue,sharedPreferences);
        }
    }


    @Override
    public void onNewState(ActivityState activityState) {
        String result = activityState.result;
        toTextEdit.setText(result);
    }
}
