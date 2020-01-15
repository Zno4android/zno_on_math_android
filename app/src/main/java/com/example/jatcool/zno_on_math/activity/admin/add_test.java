package com.example.jatcool.zno_on_math.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.jatcool.zno_on_math.R;

public class add_test extends AppCompatActivity {

    Spinner spinnerVariants;
    LinearLayout ll_variants;
   String kol_variants [] = new String[]{"2","3","4","5","6"};
    LinearLayout linearLayout;
    private int countID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);
          linearLayout = (LinearLayout)findViewById(R.id.Test_liner_layout);
         spinnerVariants = (Spinner)findViewById(R.id.Add_test_spinner);
        ll_variants = (LinearLayout) findViewById(R.id.variants);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,kol_variants);
        spinnerVariants.setAdapter(arrayAdapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                add_variants(Integer.parseInt(item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerVariants.setOnItemSelectedListener(itemSelectedListener);

    }
    private void add_variants(int kol){
      linearLayout.removeAllViews();
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RadioButton otv = new RadioButton(this);
        EditText desc_var = new EditText(this);
        linearLayout.addView(otv,layoutParams);
        linearLayout.addView(desc_var,layoutParams);

    }

}

