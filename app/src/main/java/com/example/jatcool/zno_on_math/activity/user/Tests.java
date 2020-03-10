package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Question;
import com.example.jatcool.zno_on_math.entity.Test;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBResultQuestion;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBStatistics;
import com.example.jatcool.zno_on_math.util.Answer;
import com.example.jatcool.zno_on_math.util.MathTesting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_ANSWERS_CONFORMITY;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_VARIANTS_CHOOSE_ANSWER;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.COUNT_VARIANTS_CONFORMITY;
import static com.example.jatcool.zno_on_math.constants.AddTestConstants.DIVIDER_VARIANTS_CONFORMITY;

public class Tests extends AppCompatActivity {

    TextView tvTheme;
    TextView tvText;
    Button btnPrev;
    Button btnSkip;
    Button btnNext;
    ListView variantsList;
    ArrayAdapter<String> adapter;
    Test test;
    String[] type_test = new String[]{"Виберіть правельну(ні) відповідь(ді)", "Відповідність", "Вести відповідь",};
    int currentQuestion = 0;
    List<Question> questions = new ArrayList<>();
    private List<View> allEds = new ArrayList<>();
    MathTesting mathTesting;
    LinearLayout passingTestL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvTheme = findViewById(R.id.Test_theme_textview);
        tvText = findViewById(R.id.Test_task);
        btnPrev = findViewById(R.id.Test_prev_btn);
        btnSkip = findViewById(R.id.Test_skip_btn);
        btnNext = findViewById(R.id.Test_next_btn);
        passingTestL = findViewById(R.id.PassingTestLiner);

        Bundle values = getIntent().getExtras();
        String testId = values.getString("testId");
        test.setId(testId);
        NetworkService.getInstance()
                .getJSONApi()
                .getTest(testId)
                .enqueue(new Callback<com.example.jatcool.zno_on_math.entity.Test>() {
                    @Override
                    public void onResponse(Call<com.example.jatcool.zno_on_math.entity.Test> call, Response<com.example.jatcool.zno_on_math.entity.Test> response) {
                        test = response.body();
                        mathTesting = new MathTesting(test.getQuestions());
                        tvTheme.setText(test.getTheme());
                        questions = test.getQuestions();
                        tvText.setText(questions.get(0).getText());
                        loadQuestion(0);
                    }

                    @Override
                    public void onFailure(Call<com.example.jatcool.zno_on_math.entity.Test> call, Throwable t) {

                    }
                });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, test.getQuestions().get(0).getVariants());
        variantsList.setAdapter(adapter);
        variantsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String answer = adapter.getItem(position);
                mathTesting.nextQuestion(answer);

                if (mathTesting.isPassAllQuestions()) {
                    btnSkip.setVisibility(View.GONE);
                }

                if (mathTesting.isTestPass()) {
                    int countCorrect = mathTesting.getCountCorrect();
                    int countIncorrect = mathTesting.getCountIncorrect();
                    SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
                    String token = sharedPreferences.getString("token", "");
                    List<Answer> answers = mathTesting.getAnswers();
                    setDataInDBStatistics(token, test.getId(), countCorrect, countIncorrect);
                    setDataInDBResultQuestion(token, answers);
                    showResultTesting(countCorrect, countIncorrect);
                }
            }
        });


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mathTesting.skipQuestion();
                //loadQuestion(test.getQuestions().get(mathTesting.getCurrentQuestion()));
                if (mathTesting.isPassAllQuestions()) {
                    btnSkip.setVisibility(View.GONE);
                }
            }
        });
    }
        private void setOnclickListenerOnButton(){
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //setQuestion();
                    //count_paper.setText((currentQuestion + 1) + "/" + (questions.size() + 1));
                    currentQuestion++;

                    if (currentQuestion < questions.size() - 1) {
                        loadQuestion(currentQuestion);
                    } else {
                        clearElements();
                    }
                }
            });

            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentQuestion == 0) {
                        return;
                    }

                    //setQuestion();
                    //count_paper.setText((currentQuestion - 1) + "/" + questions.size() + 1);
                    loadQuestion(--currentQuestion);
                }
            });

            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }



    private void setDataInDBResultQuestion(String token, List<Answer> answers) {
        for (int i = 0; i < test.getQuestions().size(); i++) {
            DBResultQuestion dbResultQuestion = new DBResultQuestion(token, test.getQuestions().get(i).getId(), answers.get(i).isCorrect(), new Date());
            NetworkService.getInstance()
                    .getJSONApi()
                    .updateResultQuestion(dbResultQuestion)
                    .enqueue(new Callback<DBResultQuestion>() {
                        @Override
                        public void onResponse(Call<DBResultQuestion> call, Response<DBResultQuestion> response) {

                        }

                        @Override
                        public void onFailure(Call<DBResultQuestion> call, Throwable t) {

                        }
                    });

        }
    }

    private void setDataInDBStatistics(String token, String testId, int countCorrect, int countIncorrect) {
        DBStatistics dbStatistics = new DBStatistics(token, testId, (countCorrect + countIncorrect) / (double) countCorrect, new Date());
        NetworkService.getInstance()
                .getJSONApi()
                .updateStatistics(dbStatistics)
                .enqueue(new Callback<DBStatistics>() {
                    @Override
                    public void onResponse(Call<DBStatistics> call, Response<DBStatistics> response) {

                    }

                    @Override
                    public void onFailure(Call<DBStatistics> call, Throwable t) {

                    }
                });
    }

    private void showResultTesting(int countCorrect, int countIncorrect) {
        Intent intent = endTest();
        intent.putExtra("countCorrect", countCorrect);
        intent.putExtra("countIncorrect", countIncorrect);
        startActivity(intent);
        finish();
    }

    public Intent endTest() {
        return new Intent(this, ResultTest.class);
    }

    private void clearElementsWriteAnswer() {
        View view = allEds.get(0);
        ((EditText) view.findViewById(R.id.edit_text_write_answer)).setText("");
    }


    private void clearElements() {
        String item = type_test[0];
        if (item.equals(type_test[2])) {
            clearElementsWriteAnswer();
        } else if (item.equals(type_test[0])) {
            clearElementsChooseAnswer();
        } else if (item.equals(type_test[1])) {
            clearElementsConformity();
        }
        tvText.setText("");
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


    private void loadQuestionWriteAnswer(Question question) {
        View view = allEds.get(0);
        ((EditText) view.findViewById(R.id.edit_text_write_answer)).setText(question.getCorrect().get(0));
    }

    private void loadQuestion(int index) {
        if (index < 0||index>=questions.size()-1) {
            return;
        }

        Question question = questions.get(index);
        //ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerType.getAdapter();
        //int position = adapter.getPosition(question.getType());
//        spinnerType.setSelection(position);
//        String item = spinnerType.getSelectedItem().toString();

        tvText.setText(question.getText());
        if (question.getType().equals(type_test[1])) {
            addWriteAnswer();
            loadQuestionWriteAnswer(question);
        } else if (question.equals(type_test[0])) {
            addVariants(COUNT_VARIANTS_CHOOSE_ANSWER);
            loadQuestionChooseAnswer(question);
        } else if (question.equals(type_test[1])) {
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



    private void addWriteAnswer() {
        passingTestL.removeAllViews();
        allEds.clear();
        final View view = getLayoutInflater().inflate(R.layout.write_answer_layout, null);
        EditText editTextWriteAnswer = view.findViewById(R.id.edit_text_write_answer);
        passingTestL.addView(view);
        allEds.add(view);
    }

    private void addVariants(int count) {
        passingTestL.removeAllViews();
        allEds.clear();
        for (int i = 0; i < count; i++) {
            final View view = getLayoutInflater().inflate(R.layout.choose_variant_layout, null);
            CheckBox checkBox = view.findViewById(R.id.check_box_choose_answer);
            EditText editText = view.findViewById(R.id.edit_text_choose_answer);
            passingTestL.addView(view);
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

    private void addConformity(int variant, int answer) {
        passingTestL.removeAllViews();
        allEds.clear();

        for (int i = 0; i < variant; i++) {
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout, null);
            Spinner spinner = view.findViewById(R.id.spinner_variants);
            EditText editTextFirst = view.findViewById(R.id.edit_text_first_part);
            EditText editTextSecond = view.findViewById(R.id.edit_text_second_part);
            String[] answers = setNumberArrayAnswer(answer);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, answers);
            spinner.setAdapter(adapter);
            passingTestL.addView(view);
            allEds.add(view);
        }

        for (int i = 0; i < answer - variant; i++) {
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout_second, null);
            EditText editTextFirst = view.findViewById(R.id.edit_text_answer_part);
            passingTestL.addView(view);
            allEds.add(view);
        }
    }
}
