package com.example.jatcool.zno_on_math.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.entity.Question;
import com.example.jatcool.zno_on_math.entity.QuestionType;
import com.example.jatcool.zno_on_math.entity.Test;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.entity.wrapper.TestWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.AddTestConstants.ADD_TEST_TEXT;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_ANSWERS_CONFORMITY;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_VARIANTS_CHOOSE_ANSWER;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_VARIANTS_CONFORMITY;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.DIVIDER_VARIANTS_CONFORMITY;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.EDIT_TEST_TEXT;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_TEST_CAN_NOT_ADD_TEST;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_TEST_CAN_NOT_EDIT_TEST;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_TEST_CAN_NOT_GET_THEMES;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_TEST_NOT_COMPLETE_TEST;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TEST_ID;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.ADD_TEST_SUCCESS_ADD_TEST;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.ADD_TEST_SUCCESS_EDIT_TEST;

public class AddTest extends AppCompatActivity {
    LinearLayout ll_variants;
    Test test;
    LinearLayout linearLayout;

    EditText txtTestName;

    TextView count_paper;
    Button btnDeleteQuestion;
    Button btnNextQuestion;
    Button btnPreviousQuestion;
    Button btnAddTest;
    EditText txtTextQuestion;
    Spinner spinnerThemeQuestion;
    Spinner spinnerType;
    String token;
    List<Question> questions = new ArrayList<>();
    int currentQuestion = 0;
    private List<View> allEds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);
        linearLayout = findViewById(R.id.Test_liner_layout);
        ll_variants = findViewById(R.id.variants);
        spinnerThemeQuestion = findViewById(R.id.theme_test);

        txtTestName = findViewById(R.id.test_name);
        btnDeleteQuestion = findViewById(R.id.delete_question);
        btnNextQuestion = findViewById(R.id.next_question);
        btnPreviousQuestion = findViewById(R.id.previous_question);
        btnAddTest = findViewById(R.id.add_test);
        txtTextQuestion = findViewById(R.id.text_question);
        spinnerThemeQuestion = findViewById(R.id.theme_test);
        count_paper = findViewById(R.id.count_papers_test);
        count_paper.setText("1/1");
        spinnerType = findViewById(R.id.Add_type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, QuestionType.getTypes());
        spinnerType.setAdapter(adapter);
        btnAddTest.setText(ADD_TEST_TEXT);

        setThemeSpinner();

        AdapterView.OnItemSelectedListener item = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                if (item.equals(QuestionType.WRITE_ANSWER.getName())) {
                    addWriteAnswer();
                } else if (item.equals(QuestionType.CHOOSE_ANSWER.getName())) {
                    addVariants(COUNT_VARIANTS_CHOOSE_ANSWER);
                } else if (item.equals(QuestionType.CONFORMITY.getName())) {
                    addConformity(COUNT_VARIANTS_CONFORMITY, COUNT_ANSWERS_CONFORMITY);
                }

                setOnclickListenerOnButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerType.setOnItemSelectedListener(item);

        Bundle values = getIntent().getExtras();
        token = values.getString(TOKEN);
        String testId = values.getString(TEST_ID);
        if (testId != null) {
            btnAddTest.setText(EDIT_TEST_TEXT);
            NetworkService.getInstance()
                    .getJSONApi()
                    .getTest(token, testId)
                    .enqueue(new Callback<TestWrapper>() {
                        @Override
                        public void onResponse(Call<TestWrapper> call, Response<TestWrapper> response) {
                            TestWrapper testWrapper = response.body();
                            test = testWrapper.getTest();

                            questions = test.getQuestions();
                            txtTestName.setText(test.getName());
                            loadQuestion(0);

                            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerThemeQuestion.getAdapter();
                            int position = adapter.getPosition(test.getTheme());
                            spinnerThemeQuestion.setSelection(position);

                            count_paper.setText("1/" + questions.size());

                            setOnclickListenerOnButton();
                        }

                        @Override
                        public void onFailure(Call<TestWrapper> call, Throwable t) {

                        }
                    });
        }
    }

    private void setOnclickListenerOnButton() {
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuestion();

                currentQuestion++;

                if (currentQuestion < questions.size() - 1) {
                    count_paper.setText((currentQuestion + 1) + "/" + questions.size());
                    loadQuestion(currentQuestion);
                } else {
                    count_paper.setText((currentQuestion + 1) + "/" + (questions.size() + 1));
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
                count_paper.setText(currentQuestion + "/" + (questions.size() + 1));
            }
        });

        btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion >= 0) {
                    if ((currentQuestion > questions.size() - 1)) {
                        loadQuestion(questions.size() - 1);
                        count_paper.setText(currentQuestion + 1 + "/" + questions.size());
                        return;
                    } else {
                        questions.remove(currentQuestion--);
                    }
                }


                if (questions.isEmpty()) {
                    clearElements();
                    count_paper.setText(currentQuestion + "/0");
                }

                if (currentQuestion < 0) {
                    loadQuestion(0);
                    count_paper.setText("0/" + (questions.size() + 1));
                }

                count_paper.setText((currentQuestion + 1 + 1) + "/" + (questions.size() + 1));

                loadQuestion(currentQuestion);
            }
        });

        btnAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuestion();

                if (!validateQuestion(questions)) {
                    Toast.makeText(AddTest.this, ADD_TEST_NOT_COMPLETE_TEST, Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                fillTest();
                TestWrapper testWrapper = new TestWrapper(test);

                if (btnAddTest.getText().toString().equals(ADD_TEST_TEXT)) {
                    NetworkService.getInstance()
                            .getJSONApi()
                            .createTest(token, testWrapper)
                            .enqueue(new Callback<Test>() {
                                @Override
                                public void onResponse(Call<Test> call, Response<Test> response) {
                                    Toast.makeText(AddTest.this, ADD_TEST_SUCCESS_ADD_TEST, Toast.LENGTH_LONG)
                                            .show();
                                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                                    FragmentManager manager = getSupportFragmentManager();
                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.detach(fragment);
                                    transaction.attach(fragment);
                                    transaction.commit();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Test> call, Throwable t) {
                                    Toast.makeText(AddTest.this, ADD_TEST_CAN_NOT_ADD_TEST, Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                } else {
                    NetworkService.getInstance()
                            .getJSONApi()
                            .updateTest(token, test.getId(), test)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(AddTest.this, ADD_TEST_SUCCESS_EDIT_TEST, Toast.LENGTH_LONG)
                                            .show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(AddTest.this, ADD_TEST_CAN_NOT_EDIT_TEST, Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                }


            }
        });
    }

    private void setThemeSpinner() {
        Bundle values = getIntent().getExtras();
        String token = values.getString("token");

        NetworkService.getInstance()
                .getJSONApi()
                .getAllTheme(token)
                .enqueue(new Callback<List<Theme>>() {
                    @Override
                    public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                        List<Theme> themes = response.body();
                        ArrayAdapter<Object> arrayAdapterThemes = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, themes.toArray());
                        spinnerThemeQuestion.setAdapter(arrayAdapterThemes);
                    }

                    @Override
                    public void onFailure(Call<List<Theme>> call, Throwable t) {
                        Toast.makeText(AddTest.this, ADD_TEST_CAN_NOT_GET_THEMES, Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void setQuestion() {
        String item = spinnerType.getSelectedItem().toString();
        List<String> variants = new ArrayList<>();
        List<String> correct = new ArrayList<>();
        Question question = new Question();

        if (item.equals(QuestionType.WRITE_ANSWER.getName())) {
            setQuestionVariantsWriteAnswer(variants, correct);
        } else if (item.equals(QuestionType.CHOOSE_ANSWER.getName())) {
            setQuestionVariantsChooseAnswer(variants, correct);
        } else if (item.equals(QuestionType.CONFORMITY.getName())) {
            setQuestionVariantsConformity(variants, correct);
        }

        String textQuestion = txtTextQuestion.getText().toString();
        int type = spinnerType.getSelectedItemPosition();
        String questionType = QuestionType.values()[type].name().toLowerCase();

        question.setType(questionType);
        question.setVariants(variants);
        question.setCorrect(correct);
        question.setText(textQuestion);

        if (currentQuestion > questions.size() - 1) {
            questions.add(question);
        } else {
            questions.set(currentQuestion, question);
        }
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
        if (index < 0) {
            clearElements();
        }

        Question question = questions.get(index);
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerType.getAdapter();
        int position = adapter.getPosition(QuestionType.valueOf(question.getType().toUpperCase()).getName());
        spinnerType.setSelection(position);
        String item = spinnerType.getSelectedItem().toString();

        txtTextQuestion.setText(question.getText());
        if (item.equals(QuestionType.WRITE_ANSWER.getName())) {
            addWriteAnswer();
            loadQuestionWriteAnswer(question);
        } else if (item.equals(QuestionType.CHOOSE_ANSWER.getName())) {
            addVariants(COUNT_VARIANTS_CHOOSE_ANSWER);
            loadQuestionChooseAnswer(question);
        } else if (item.equals(QuestionType.CONFORMITY.getName())) {
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
        if (item.equals(QuestionType.WRITE_ANSWER.getName())) {
            clearElementsWriteAnswer();
        } else if (item.equals(QuestionType.CHOOSE_ANSWER.getName())) {
            clearElementsChooseAnswer();
        } else if (item.equals(QuestionType.CONFORMITY.getName())) {
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

    private void addVariants(int count) {
        linearLayout.removeAllViews();
        allEds.clear();
        for (int i = 0; i < count; i++) {
            final View view = getLayoutInflater().inflate(R.layout.choose_variant_layout, null);
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
            String[] answers = setNumberArrayAnswer(answer);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, answers);
            spinner.setAdapter(adapter);
            linearLayout.addView(view);
            allEds.add(view);
        }

        for (int i = 0; i < answer - variant; i++) {
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout_second, null);
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
        linearLayout.addView(view);
        allEds.add(view);
    }

    private void fillTest() {
        if (test == null) {
            test = new Test();
        }

        String thema = spinnerThemeQuestion.getSelectedItem().toString();
        for (Question question : questions) {
            question.setTheme(thema);
        }
        test.setTheme(thema);
        test.setQuestions(questions);
        test.setName(txtTestName.getText().toString());
    }

    private boolean validateQuestion(List<Question> questions) {
        for (Question question : questions) {
            if (question.getText().isEmpty() || question.getCorrect().size() < 1) {
                return false;
            }

            for (String variant : question.getVariants()) {
                if (variant.isEmpty()) {
                    return false;
                }
            }

            for (String correct : question.getCorrect()) {
                if (correct.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

}

