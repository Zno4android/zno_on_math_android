package com.example.jatcool.zno_on_math.activity.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class add_test extends AppCompatActivity {
    static final int GALLERY_REQUEST = 1;
    final int PIC_CROP = 2;
    Spinner spinnerVariants;
    LinearLayout lin;
    LinearLayout.LayoutParams par;
    ImageView img;
    LinearLayout ll_variants;
    Bitmap thePic = null;
    String[] kol_variants = new String[]{"2", "3", "4", "5", "6"};
    int id = 0;
    LinearLayout linearLayout;
    private int countID = 0;
    Spinner spinnerType;
    TextView hide;
    String otvet [] = new String[] {"1","2","3","4","5"};
    String type_test [] = new String[]{"Виберіть правельну(ні) відповідь(ді)","Відповідність","Вести відповідь",};

    Button btnAddQuestion;
    Button btnDeleteQuestion;
    Button btnNextQuestion;
    Button btnPreviousQuestion;
    Button btnAddTest;
    EditText txtTextQuestion;
    Spinner spinnerThemeQuestion;
    List<Question> questions = new ArrayList<>();
    int currentQuestion = 0;
    private List<View> allEds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);
        linearLayout = findViewById(R.id.Test_liner_layout);
        spinnerVariants = findViewById(R.id.Add_test_spinner);
        ll_variants = findViewById(R.id.variants);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, kol_variants);
        spinnerVariants.setAdapter(arrayAdapter);
        hide = findViewById(R.id.text_variant_hide);
        spinnerType = findViewById(R.id.Add_type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,type_test);
        spinnerType.setAdapter(adapter);
        AdapterView.OnItemSelectedListener item = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                if(item.equals(type_test[2])){
                    allEds.clear();
                    spinnerVariants.setVisibility(View.GONE);
                    hide.setVisibility(View.GONE);
                    linearLayout.removeAllViews();
                    EditText t = new EditText(getApplicationContext());
                    LinearLayout.LayoutParams radio = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    linearLayout.addView(t,radio);

                }
                else if(item.equals(type_test[0])){
                    spinnerVariants.setVisibility(View.VISIBLE);
                    hide.setVisibility(View.VISIBLE);
                    linearLayout.removeAllViews();
                    allEds.clear();
                    add_variants(Integer.parseInt(spinnerVariants.getSelectedItem().toString()));

                }
                else if(item.equals(type_test[1])){
                    linearLayout.removeAllViews();
                    hide.setVisibility(View.GONE);
                    spinnerVariants.setVisibility(View.GONE);
                    linearLayout.removeAllViews();
                    allEds.clear();
                    addSotv();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerType.setOnItemSelectedListener(item);

        btnDeleteQuestion = findViewById(R.id.delete_question);
        btnNextQuestion = findViewById(R.id.next_question);
        btnPreviousQuestion = findViewById(R.id.previous_question);
        btnAddTest = findViewById(R.id.add_test);
        txtTextQuestion = findViewById(R.id.text_question);
        spinnerThemeQuestion = findViewById(R.id.theme_test);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                add_variants(Integer.parseInt(item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerVariants.setOnItemSelectedListener(itemSelectedListener);

    }

    private void setOnclickListenerOnButton() {
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> variants = new ArrayList<>();
                StringBuilder correctBuilder = new StringBuilder();
                for (View view : allEds) {
                    variants.add(((EditText) view.findViewById(R.id.editTextChooseAnswer)).getText().toString());
                    if (view.findViewById(R.id.checkBoxChooseAnswer).isSelected()) {
                        correctBuilder.append(((EditText) view.findViewById(R.id.editTextChooseAnswer)).getText().toString()).append("|");
                    }
                }
                correctBuilder.setLength(correctBuilder.length() - 1);
                String correct = correctBuilder.toString();
                String textQuestion = txtTextQuestion.getText().toString();
                String thema = spinnerThemeQuestion.getSelectedItem().toString();

                Question question = new Question();
                question.setTheme(thema);
                question.setVariants(variants);
                question.setCorrect(correct);
                question.setText(textQuestion);
                currentQuestion++;
            }
        });
    }

    private void addCorrect(StringBuilder correctBuilder, String answer) {
        correctBuilder.append(answer).append("|");
    }

    public void AddImageView(View view) {
        lin = findViewById(R.id.add_image_liner);
        img = new ImageView(this);
        par = new LinearLayout.LayoutParams(100, 100);
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
        switch (requestCode) {
            case GALLERY_REQUEST: {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        thePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        if (thePic != null) {
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
    private void addSotv(){

        for(int i=0;i<5;i++){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);
            l.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            EditText e = new EditText(this);
            LinearLayout.LayoutParams Edparams = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
            Spinner sp = new Spinner(this);
            sp.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,otvet));
            l.addView(e,Edparams);
            l.addView(sp , new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if(i==4){
                e.setVisibility(View.INVISIBLE);
                sp.setVisibility(View.INVISIBLE);
            }
            LinearLayout.LayoutParams rightText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView t =new TextView(this);
            t.setText(String.valueOf(i+1));
            EditText ed = new EditText(this);
            rightText.gravity = Gravity.RIGHT;
            l.addView(t,rightText );
            LinearLayout.LayoutParams righEd = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
            righEd.gravity = Gravity.RIGHT;
            l.addView(ed,righEd);
            linearLayout.addView(l);
        }

    }

    private void add_variants(int kol) {
        linearLayout.removeAllViews();
        for (int i = 0; i < kol; i++) {
            final View view = getLayoutInflater().inflate(R.layout.choose_variant_layout, null);
            CheckBox radioButton = view.findViewById(R.id.checkBoxChooseAnswer);
            EditText editText = view.findViewById(R.id.editTextChooseAnswer);
            linearLayout.addView(view);
            allEds.add(view);
//            LinearLayout.LayoutParams ediText = new LinearLayout.LayoutParams(450, LinearLayout.LayoutParams.WRAP_CONTENT);
//            LinearLayout.LayoutParams radio = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            RadioButton otv = new RadioButton(this);
//            EditText desc_var = new EditText(this);
//            LinearLayout line = new LinearLayout(this);
//            line.setOrientation(LinearLayout.HORIZONTAL);
//            otv.setId((i+1));
//            desc_var.setId((i+1)*10);
//            line.addView(otv, radio);
//            line.addView(desc_var, ediText);
//            LinearLayout.LayoutParams r = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            linearLayout.addView(line, r);
        }

    }

}

