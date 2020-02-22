package com.example.jatcool.zno_on_math.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jatcool.zno_on_math.R;

import java.io.IOException;

public class add_test extends AppCompatActivity {
    static final int GALLERY_REQUEST = 1;
    final int PIC_CROP = 2;
    Spinner spinnerVariants;
    LinearLayout lin;
    LinearLayout.LayoutParams  par;
    ImageView img;
    LinearLayout ll_variants;
    Bitmap thePic=null;
   String kol_variants [] = new String[]{"2","3","4","5","6"};
   int id = 0;
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
    public void AddImageView(View view){
         lin = findViewById(R.id.add_image_liner);
         img = new ImageView(this);
         par = new LinearLayout.LayoutParams(100,100);
        img.setId(id);
        id++;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        switch(requestCode) {
            case GALLERY_REQUEST: {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        thePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        if(thePic!=null) {
                            img.setRotation(0);
                            img.setImageBitmap(thePic);
                            lin.addView(img, par);
                            thePic = null;
                            img = null;

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void add_variants(int kol){
      linearLayout.removeAllViews();
      for(int i=0;i<kol;i++) {
          LinearLayout.LayoutParams ediText = new LinearLayout.LayoutParams(450, LinearLayout.LayoutParams.WRAP_CONTENT);
          LinearLayout.LayoutParams radio = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          RadioButton otv = new RadioButton(this);
          EditText desc_var = new EditText(this);
          LinearLayout line = new LinearLayout(this);
          line.setOrientation(LinearLayout.HORIZONTAL);
          line.addView(otv, radio);
          line.addView(desc_var, ediText);
          LinearLayout.LayoutParams r = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
          linearLayout.addView(line,r);
      }

    }

}

