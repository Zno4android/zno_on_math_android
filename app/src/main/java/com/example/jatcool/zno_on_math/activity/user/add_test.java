package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.jatcool.zno_on_math.R;

public class add_test extends AppCompatActivity {

    Button btnAddVariant;
    LinearLayout ll_variants;


    private int countID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        btnAddVariant=(Button)findViewById(R.id.add_variant);
        ll_variants=(LinearLayout) findViewById(R.id.variants);
        btnAddVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll=new LinearLayout(getApplicationContext());
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                CheckBox ch=new CheckBox(getApplicationContext());
                ch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                EditText et=new EditText(getApplicationContext());
                et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                Button b = new Button(getApplicationContext());
                b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.addView(ch);
                ll.addView(et);
                ll.addView(b);
                ll_variants.addView(ll);

            }
        });
    }
}
