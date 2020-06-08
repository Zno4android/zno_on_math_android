package com.example.jatcool.zno_on_math.activity.admin;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.SimpleGroupAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.entity.Question;
import com.example.jatcool.zno_on_math.entity.QuestionType;
import com.example.jatcool.zno_on_math.entity.Test;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.entity.wrapper.TestWrapper;
import com.example.jatcool.zno_on_math.listeners.MathKeyboardActionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.kexanie.library.MathView;
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
    MathView textQuestionMathView;
    MathView variantsMathView;
    Spinner spinnerThemeQuestion;
    Spinner spinnerType;
    KeyboardView mKeyboardView;
    KeyboardView mKeyboardViewVariants;

    MathKeyboardActionListener variantsMathKeyboardActionListener;

    String token;
    String testId;
    List<Question> questions = new ArrayList<>();
    int currentQuestion = 0;
    private List<View> allEds = new ArrayList<>();
    private AdapterView.OnItemSelectedListener selectedListener;
    private boolean flagSelectedItemListener = true;

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
        textQuestionMathView = findViewById(R.id.text_question_math);
        variantsMathView = findViewById(R.id.variants_mathview);
        spinnerThemeQuestion = findViewById(R.id.theme_test);
        count_paper = findViewById(R.id.count_papers_test);
        count_paper.setText("1/1");
        spinnerType = findViewById(R.id.Add_type_spinner);
        SimpleGroupAdapter adapter = new SimpleGroupAdapter(this, R.layout.simple_list_view, Arrays.asList(QuestionType.getTypes()));
        spinnerType.setAdapter(adapter);
        btnAddTest.setText(ADD_TEST_TEXT);

        Keyboard mKeyboard = new Keyboard(this, R.xml.keyboard);

        mKeyboardView = findViewById(R.id.keyboardview);
        mKeyboardViewVariants = findViewById(R.id.keyboardview_variants);

        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardViewVariants.setKeyboard(mKeyboard);

        mKeyboardView.setPreviewEnabled(false);
        mKeyboardViewVariants.setPreviewEnabled(false);

        mKeyboardView.setOnKeyboardActionListener(new MathKeyboardActionListener(txtTextQuestion));
        variantsMathKeyboardActionListener = new MathKeyboardActionListener();
        mKeyboardViewVariants.setOnKeyboardActionListener(variantsMathKeyboardActionListener);

        selectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (flagSelectedItemListener) {
                    String item = (String) parent.getItemAtPosition(position);
                    if (item.equals(QuestionType.WRITE_ANSWER.getName())) {
                        addWriteAnswer();
                    } else if (item.equals(QuestionType.CHOOSE_ANSWER.getName())) {
                        addVariants(COUNT_VARIANTS_CHOOSE_ANSWER);
                    } else if (item.equals(QuestionType.CONFORMITY.getName())) {
                        addConformity(COUNT_VARIANTS_CONFORMITY, COUNT_ANSWERS_CONFORMITY);
                    }
                } else {
                    flagSelectedItemListener = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerType.setOnItemSelectedListener(selectedListener);

        Bundle values = getIntent().getExtras();
        token = values.getString(TOKEN);
        testId = values.getString(TEST_ID);

        setThemeSpinner();
        setOnclickListenerOnButton();

        txtTextQuestion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openKeyboard(v);
                } else {
                    closeKeyboard(v);
                }
            }
        });

        txtTextQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = txtTextQuestion.getText().toString();
                StringBuilder mathText = new StringBuilder();
                Pattern pattern = Pattern.compile("\\$\\$.*\\$\\$");
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    mathText.append(text.substring(matcher.start(), matcher.end()));
                }
                textQuestionMathView.setText(mathText.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void openKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void closeKeyboard(View v) {
        mKeyboardView.setVisibility(View.GONE);
    }


    private void setOnclickListenerOnButton() {
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuestion();

                currentQuestion++;

                if (currentQuestion < questions.size()) {
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
                count_paper.setText((currentQuestion + 1) + "/" + questions.size());
            }
        });

        btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questions.size() <= 0) {
                    clearElements();
                    count_paper.setText((currentQuestion + 1) + "/1");
                    questions.clear();
                    return;
                }

                if (questions.size() == 1) {
                    clearElements();
                    count_paper.setText((currentQuestion) + "/1");
                    currentQuestion--;
                    questions.clear();
                    return;
                }

                if (currentQuestion >= 0) {
                    if ((currentQuestion > questions.size() - 1)) {
                        currentQuestion = questions.size() - 1;
                        loadQuestion(currentQuestion);
                        count_paper.setText(currentQuestion + "/" + questions.size());
                        return;
                    } else {
                        questions.remove(currentQuestion--);
                    }
                }

                if (currentQuestion < 0) {
                    currentQuestion = 0;

                }

                loadQuestion(currentQuestion);
                count_paper.setText(currentQuestion + 1 + "/" + (questions.size()));
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
        NetworkService.getInstance()
                .getJSONApi()
                .getAllTheme(token)
                .enqueue(new Callback<List<Theme>>() {
                    @Override
                    public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                        List<Theme> themes = response.body();
                        ArrayAdapter<Object> arrayAdapterThemes = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, themes.toArray());
                        spinnerThemeQuestion.setAdapter(arrayAdapterThemes);

                        if (testId != null) {
                            btnAddTest.setText(EDIT_TEST_TEXT);
                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .getTest(token, testId)
                                    .enqueue(new Callback<TestWrapper>() {
                                        @Override
                                        public void onResponse(Call<TestWrapper> call, Response<TestWrapper> response) {
                                            setOnclickListenerOnButton();

                                            TestWrapper testWrapper = response.body();
                                            test = testWrapper.getTest();

                                            questions = test.getQuestions();
                                            txtTestName.setText(test.getName());
                                            loadQuestion(0);

                                            ArrayAdapter<Theme> adapter = (ArrayAdapter<Theme>) spinnerThemeQuestion.getAdapter();
                                            int position = adapter.getPosition(new Theme(test.getTheme()));
                                            spinnerThemeQuestion.setSelection(position);

                                            count_paper.setText("1/" + questions.size());
                                        }

                                        @Override
                                        public void onFailure(Call<TestWrapper> call, Throwable t) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Theme>> call, Throwable t) {
                        Toast.makeText(AddTest.this, ADD_TEST_CAN_NOT_GET_THEMES, Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mKeyboardViewVariants.setVisibility(View.VISIBLE);
                variantsMathKeyboardActionListener.setEditText((EditText) v);
            } else {
                mKeyboardViewVariants.setVisibility(View.GONE);
            }
        }
    };

    private void setQuestionVariantsChooseAnswer(List<String> variants, List<String> correct) {
        for (View view : allEds) {
            variants.add(((EditText) view.findViewById(R.id.edit_text_choose_answer)).getText().toString());

            if (((CheckBox) view.findViewById(R.id.check_box_choose_answer)).isChecked()) {
                correct.add(((EditText) view.findViewById(R.id.edit_text_choose_answer)).getText().toString().replace("\\", "\\\\"));
            }
        }
    }

    private void setQuestionVariantsConformity(List<String> variants, List<String> correct) {
        for (int i = 0; i < COUNT_VARIANTS_CONFORMITY; i++) {
            View view = allEds.get(i);
            StringBuilder variantBuilder = new StringBuilder();
            variantBuilder.append(((EditText) view.findViewById(R.id.edit_text_first_part)).getText().toString().replace("\\", "\\\\"));
            variantBuilder.append(DIVIDER_VARIANTS_CONFORMITY);
            variantBuilder.append(((EditText) view.findViewById(R.id.edit_text_second_part)).getText().toString().replace("\\", "\\\\"));
            variants.add(variantBuilder.toString());
            correct.add(((Spinner) view.findViewById(R.id.spinner_variants)).getSelectedItem().toString());
        }

        for (int i = COUNT_VARIANTS_CONFORMITY; i < COUNT_ANSWERS_CONFORMITY; i++) {
            View view = allEds.get(i);
            variants.add(((EditText) view.findViewById(R.id.edit_text_answer_part)).getText().toString().replace("\\", "\\\\"));
        }
    }

    private void setQuestionVariantsWriteAnswer(List<String> variants, List<String> correct) {
        View view = allEds.get(0);
        correct.add(((EditText) view.findViewById(R.id.edit_text_write_answer)).getText().toString().replace("\\", "\\\\"));
    }

    private void loadQuestion(int index) {
        if (index < 0) {
            clearElements();
        }

        flagSelectedItemListener = false;

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
            ((EditText) view.findViewById(R.id.edit_text_first_part)).setText(" ");
            ((EditText) view.findViewById(R.id.edit_text_second_part)).setText(" ");
            ((Spinner) view.findViewById(R.id.spinner_variants)).setSelection(0);
        }

        for (int i = COUNT_VARIANTS_CONFORMITY; i < COUNT_ANSWERS_CONFORMITY; i++) {
            View view = allEds.get(i);
            ((EditText) view.findViewById(R.id.edit_text_answer_part)).setText(" ");
        }
    }

    private void clearElementsWriteAnswer() {
        View view = allEds.get(0);
        ((EditText) view.findViewById(R.id.edit_text_write_answer)).setText("");
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = variantsMathKeyboardActionListener.getEditText().getText().toString();
            StringBuilder mathText = new StringBuilder();
            Pattern pattern = Pattern.compile("\\$\\$.*\\$\\$");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                mathText.append(text.substring(matcher.start(), matcher.end()));
            }

            variantsMathView.setText(mathText.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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

        String textQuestion = txtTextQuestion.getText().toString().replace("\\", "\\\\");
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

    private String[] setNumberArrayAnswer(int answer) {
        String[] array = new String[answer];
        for (int i = 0; i < answer; i++) {
            array[i] = String.valueOf(i + 1);
        }
        return array;
    }

    private void addVariants(int count) {
        linearLayout.removeAllViews();
        allEds.clear();
        for (int i = 0; i < count; i++) {
            View view = getLayoutInflater().inflate(R.layout.choose_variant_layout, null);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.topMargin = 20;

            EditText textVariant = view.findViewById(R.id.edit_text_choose_answer);

            textVariant.setOnFocusChangeListener(focusChangeListener);

            textVariant.addTextChangedListener(textWatcher);

            linearLayout.addView(view, param);
            allEds.add(view);
        }
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

    private void addConformity(int variant, int answer) {
        linearLayout.removeAllViews();
        allEds.clear();

        for (int i = 0; i < variant; i++) {
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout, null);
            Spinner spinner = view.findViewById(R.id.spinner_variants);
            String[] answers = setNumberArrayAnswer(answer);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, answers);
            spinner.setAdapter(adapter);

            EditText textVariant = view.findViewById(R.id.edit_text_first_part);
            textVariant.setOnFocusChangeListener(focusChangeListener);

            textVariant.addTextChangedListener(textWatcher);

            EditText textVariantSecond = view.findViewById(R.id.edit_text_second_part);
            textVariantSecond.setOnFocusChangeListener(focusChangeListener);

            textVariantSecond.addTextChangedListener(textWatcher);

            linearLayout.addView(view);
            allEds.add(view);
        }

        for (int i = 0; i < answer - variant; i++) {
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout_second, null);

            EditText textVariant = view.findViewById(R.id.edit_text_answer_part);

            textVariant.setOnFocusChangeListener(focusChangeListener);

            textVariant.addTextChangedListener(textWatcher);

            linearLayout.addView(view);
            allEds.add(view);
        }
    }

    private void addWriteAnswer() {
        linearLayout.removeAllViews();
        allEds.clear();
        final View view = getLayoutInflater().inflate(R.layout.write_answer_layout, null);
        EditText textVariant = view.findViewById(R.id.edit_text_write_answer);

        linearLayout.addView(view);
        allEds.add(view);
    }

}

