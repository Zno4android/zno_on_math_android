package com.example.jatcool.zno_on_math.activity.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.entity.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_ANSWERS_CONFORMITY;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_VARIANTS_CHOOSE_ANSWER;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_VARIANTS_CONFORMITY;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.DIVIDER_VARIANTS_CONFORMITY;

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
    String[] type_test = new String[]{"Виберіть правельну(ні) відповідь(ді)", "Відповідність", "Вести відповідь",};
    int id = 0;
    LinearLayout linearLayout;
    private int countID = 0;

    Button btnDeleteQuestion;
    Button btnNextQuestion;
    Button btnPreviousQuestion;
    Button btnAddTest;
    EditText txtTextQuestion;
    Spinner spinnerThemeQuestion;
    Spinner spinnerType;
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

        btnDeleteQuestion = findViewById(R.id.delete_question);
        btnNextQuestion = findViewById(R.id.next_question);
        btnPreviousQuestion = findViewById(R.id.previous_question);
        btnAddTest = findViewById(R.id.add_test);
        txtTextQuestion = findViewById(R.id.text_question);
        spinnerThemeQuestion = findViewById(R.id.theme_test);

        spinnerType = findViewById(R.id.Add_type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, type_test);
        spinnerType.setAdapter(adapter);

        AdapterView.OnItemSelectedListener item = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                if (item.equals(type_test[2])) {
                    addWriteAnswer();
                } else if (item.equals(type_test[0])) {
                    addVariants(COUNT_VARIANTS_CHOOSE_ANSWER);
                } else if (item.equals(type_test[1])) {
                    addConformity(COUNT_VARIANTS_CONFORMITY, COUNT_ANSWERS_CONFORMITY);
                }

                setOnclickListenerOnButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerType.setOnItemSelectedListener(item);

        //setOnclickListenerOnButton();
    }

    private void setOnclickListenerOnButton() {
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuestion();
                currentQuestion++;
                if (currentQuestion < questions.size() - 1) {
                    loadQuestion(currentQuestion);
                } else {
                    clearElements();
                }
            }
        });

        btnPreviousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion == 0) {
                    return;
                }

                setQuestion();

                loadQuestion(--currentQuestion);
            }
        });

        btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questions.remove(currentQuestion--);

                if (questions.isEmpty()) {
                    clearElements();
                }

                if (currentQuestion < 0) {
                    loadQuestion(0);
                }
                loadQuestion(currentQuestion);
            }
        });
    }

    private void setQuestion() {
        String item = spinnerType.getSelectedItem().toString();
        List<String> variants = new ArrayList<>();
        List<String> correct = new ArrayList<>();
        Question question = new Question();

        if (item.equals(type_test[2])) {
            setQuestionVariantsWriteAnswer(variants, correct);
        } else if (item.equals(type_test[0])) {
            setQuestionVariantsChooseAnswer(variants, correct);
        } else if (item.equals(type_test[1])) {
            setQuestionVariantsConformity(variants, correct);
        }

        if (currentQuestion > questions.size() - 1) {
            questions.add(question);
        } else {
            questions.set(currentQuestion, question);
        }
        String textQuestion = txtTextQuestion.getText().toString();
        String type = spinnerType.getSelectedItem().toString();

        question.setType(type);
        question.setVariants(variants);
        question.setCorrect(correct);
        question.setText(textQuestion);
    }

    private void setQuestionVariantsChooseAnswer(List<String> variants, List<String> correct) {
        for (View view : allEds) {
            variants.add(((EditText) view.findViewById(R.id.edit_text_choose_answer)).getText().toString());

            if (((CheckBox) view.findViewById(R.id.check_box_choose_answer)).isChecked()) {
                correct.add(((EditText) view.findViewById(R.id.edit_text_choose_answer)).getText().toString());
            }
        }
    }

    private void setQuestionVariantsConformity(List<String> variants, List<String> correct) {
        for (int i = 0; i < COUNT_VARIANTS_CONFORMITY; i++) {
            View view = allEds.get(i);
            StringBuilder variantBuilder = new StringBuilder();
            variantBuilder.append(((EditText) view.findViewById(R.id.edit_text_first_part)).getText().toString());
            variantBuilder.append(DIVIDER_VARIANTS_CONFORMITY);
            variantBuilder.append(((EditText) view.findViewById(R.id.edit_text_second_part)).getText().toString());
            variants.add(variantBuilder.toString());
            correct.add(((Spinner) view.findViewById(R.id.spinner_variants)).getSelectedItem().toString());
        }

        for (int i = COUNT_VARIANTS_CONFORMITY; i < COUNT_ANSWERS_CONFORMITY; i++) {
            View view = allEds.get(i);
            variants.add(((EditText) view.findViewById(R.id.edit_text_answer_part)).getText().toString());
        }
    }

    private void setQuestionVariantsWriteAnswer(List<String> variants, List<String> correct) {
        View view = allEds.get(0);
        correct.add(((EditText) view.findViewById(R.id.edit_text_write_answer)).getText().toString());
    }

    private void loadQuestion(int index) {
        Question question = questions.get(index);
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerType.getAdapter();
        int position = adapter.getPosition(question.getType());
        spinnerType.setSelection(position);
        String item = spinnerType.getSelectedItem().toString();

        txtTextQuestion.setText(question.getText());
        if (item.equals(type_test[2])) {
            addWriteAnswer();
            loadQuestionWriteAnswer(question);
        } else if (item.equals(type_test[0])) {
            addVariants(COUNT_VARIANTS_CHOOSE_ANSWER);
            loadQuestionChooseAnswer(question);
        } else if (item.equals(type_test[1])) {
            addConformity(COUNT_VARIANTS_CONFORMITY, COUNT_ANSWERS_CONFORMITY);
            loadQuestionConformity(question);
        }
    }

    private void loadQuestionChooseAnswer(Question question) {
        for (int i = 0; i < allEds.size(); i++) {
            String variant = question.getVariants().get(i);
            ((EditText) allEds.get(i).findViewById(R.id.edit_text_choose_answer)).setText(variant);

            if (question.getCorrect().contains(variant)) {
                ((CheckBox) allEds.get(i).findViewById(R.id.check_box_choose_answer)).setChecked(true);
            }
        }
    }

    private void loadQuestionConformity(Question question) {
        List<String> variants = question.getVariants();
        List<String> correct = question.getCorrect();

        for (int i = 0; i < COUNT_VARIANTS_CONFORMITY; i++) {
            View view = allEds.get(i);
            String variantAndAnswer = variants.get(i);
            String[] variantArr = variantAndAnswer.split(DIVIDER_VARIANTS_CONFORMITY);
            String variant = variantArr[0];
            String answer = variantArr[1];
            int correctPosition = Integer.parseInt(correct.get(i)) - 1;
            ((EditText) view.findViewById(R.id.edit_text_first_part)).setText(variant);
            ((EditText) view.findViewById(R.id.edit_text_second_part)).setText(answer);
            ((Spinner) view.findViewById(R.id.spinner_variants)).setSelection(correctPosition);
        }

        for (int i = COUNT_VARIANTS_CONFORMITY; i < COUNT_ANSWERS_CONFORMITY; i++) {
            View view = allEds.get(i);
            String variant = variants.get(i);
            ((EditText) view.findViewById(R.id.edit_text_answer_part)).setText(variant);
        }
    }

    private void loadQuestionWriteAnswer(Question question) {
        View view = allEds.get(0);
        ((EditText) view.findViewById(R.id.edit_text_write_answer)).setText(question.getCorrect().get(0));
    }

    private void clearElements() {
        String item = spinnerType.getSelectedItem().toString();
        if (item.equals(type_test[2])) {
            clearElementsWriteAnswer();
        } else if (item.equals(type_test[0])) {
            clearElementsChooseAnswer();
        } else if (item.equals(type_test[1])) {
            clearElementsConformity();
        }
        txtTextQuestion.setText("");
    }

    private void clearElementsChooseAnswer() {
        for (int i = 0; i < allEds.size(); i++) {
            ((EditText) allEds.get(i).findViewById(R.id.edit_text_choose_answer)).setText("");
            ((CheckBox) allEds.get(i).findViewById(R.id.check_box_choose_answer)).setChecked(false);
        }
    }

    private void clearElementsConformity() {
        for (int i = 0; i < COUNT_VARIANTS_CONFORMITY; i++) {
            View view = allEds.get(i);
            ((EditText) view.findViewById(R.id.edit_text_first_part)).setText("");
            ((EditText) view.findViewById(R.id.edit_text_second_part)).setText("");
            ((Spinner) view.findViewById(R.id.spinner_variants)).setSelection(0);
        }

        for (int i = COUNT_VARIANTS_CONFORMITY; i < COUNT_ANSWERS_CONFORMITY; i++) {
            View view = allEds.get(i);
            ((EditText) view.findViewById(R.id.edit_text_answer_part)).setText("");
        }
    }

    private void clearElementsWriteAnswer() {
        View view = allEds.get(0);
        ((EditText) view.findViewById(R.id.edit_text_write_answer)).setText("");
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

    private void addVariants(int count) {
        linearLayout.removeAllViews();
        allEds.clear();
        for (int i = 0; i < count; i++) {
            final View view = getLayoutInflater().inflate(R.layout.choose_variant_layout, null);
            CheckBox checkBox = view.findViewById(R.id.check_box_choose_answer);
            EditText editText = view.findViewById(R.id.edit_text_choose_answer);
            linearLayout.addView(view);
            allEds.add(view);
        }
    }

    private void addConformity(int variant, int answer) {
        linearLayout.removeAllViews();
        allEds.clear();

        for (int i = 0; i < variant; i++) {
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout, null);
            Spinner spinner = view.findViewById(R.id.spinner_variants);
            EditText editTextFirst = view.findViewById(R.id.edit_text_first_part);
            EditText editTextSecond = view.findViewById(R.id.edit_text_second_part);
            String[] answers = setNumberArrayAnswer(answer);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, answers);
            spinner.setAdapter(adapter);
            linearLayout.addView(view);
            allEds.add(view);
        }

        for (int i = 0; i < answer - variant; i++) {
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout_second, null);
            EditText editTextFirst = view.findViewById(R.id.edit_text_answer_part);
            linearLayout.addView(view);
            allEds.add(view);
        }
    }

    private String[] setNumberArrayAnswer(int answer) {
        String[] array = new String[answer];
        for (int i = 0; i < answer; i++) {
            array[i] = String.valueOf(i + 1);
        }
        return array;
    }

    private void addWriteAnswer() {
        linearLayout.removeAllViews();
        allEds.clear();
        final View view = getLayoutInflater().inflate(R.layout.write_answer_layout, null);
        EditText editTextWriteAnswer = view.findViewById(R.id.edit_text_write_answer);
        linearLayout.addView(view);
        allEds.add(view);
    }

}

