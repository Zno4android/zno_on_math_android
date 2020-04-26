package com.example.jatcool.zno_on_math.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.entity.Question;
import com.example.jatcool.zno_on_math.entity.QuestionType;
import com.example.jatcool.zno_on_math.entity.Test;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBResultQuestion;
import com.example.jatcool.zno_on_math.entity.dbEntity.DBStatistics;
import com.example.jatcool.zno_on_math.entity.wrapper.StatisticsWrapper;
import com.example.jatcool.zno_on_math.entity.wrapper.TestWrapper;
import com.example.jatcool.zno_on_math.util.Answer;
import com.example.jatcool.zno_on_math.util.MathTesting;
import com.google.gson.Gson;

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
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.COUNT_CORRECT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.COUNT_INCORRECT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATISTICS;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TEST_ID;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.TESTS_PROCESSING_RESULTS;

public class Tests extends AppCompatActivity {

    TextView tvTheme;
    TextView tvText;
    Button btnSkip;
    Button btnNext;
    TestWrapper testWrapper;
    Test test;
    String token;

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
        btnSkip = findViewById(R.id.Test_skip_btn);
        btnNext = findViewById(R.id.Test_next_btn);
        passingTestL = findViewById(R.id.PassingTestLiner);

        Bundle values = getIntent().getExtras();
        token = values.getString(TOKEN);
        String testId = values.getString(TEST_ID);

        NetworkService.getInstance()
                .getJSONApi()
                .getTest(token, testId)
                .enqueue(new Callback<TestWrapper>() {
                    @Override
                    public void onResponse(Call<TestWrapper> call, Response<TestWrapper> response) {
                        testWrapper = response.body();
                        test = testWrapper.getTest();
                        mathTesting = new MathTesting(test.getQuestions());
                        tvTheme.setText(test.getTheme());
                        questions = test.getQuestions();
                        tvText.setText(questions.get(0).getText());
                        loadQuestion(0);
                        setOnclickListenerOnButton();
                    }

                    @Override
                    public void onFailure(Call<TestWrapper> call, Throwable t) {

                    }
                });
    }

    private void setOnclickListenerOnButton() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerQuestion();

                loadQuestion(mathTesting.getCurrentQuestion());


                if (mathTesting.isPassAllQuestions()) {
                    btnSkip.setVisibility(View.GONE);
                }

                if (mathTesting.isTestPass()) {
                    btnNext.setEnabled(false);
                    Toast.makeText(Tests.this, TESTS_PROCESSING_RESULTS, Toast.LENGTH_LONG)
                            .show();
                    saveResult();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mathTesting.skipQuestion();
                loadQuestion(mathTesting.getCurrentQuestion());
                if (mathTesting.isPassAllQuestions()) {
                    btnSkip.setVisibility(View.GONE);
                }
            }
        });
    }


    private void saveResult() {
        List<Answer> answers = mathTesting.getAnswers();
        List<DBResultQuestion> answersStatistics = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i < answers.size(); i++) {
            DBResultQuestion dbResultQuestion = new DBResultQuestion();
            dbResultQuestion.setResult(answers.get(i).isCorrect());
            dbResultQuestion.setQuestionId(questions.get(i).getId());

            answersStatistics.add(dbResultQuestion);
        }

        String testId = test.getId();
        final int countCorrect = mathTesting.getCountCorrect();
        final int countIncorrect = mathTesting.getCountIncorrect();

        DBStatistics dbStatistics = new DBStatistics(testId, (double) countCorrect / (double) (countCorrect + countIncorrect) * 100, date);

        StatisticsWrapper statistics = new StatisticsWrapper(dbStatistics, answersStatistics);

        NetworkService.getInstance()
                .getJSONApi()
                .saveStatistics(token, statistics)
                .enqueue(new Callback<StatisticsWrapper>() {
                    @Override
                    public void onResponse(Call<StatisticsWrapper> call, Response<StatisticsWrapper> response) {
                        showResultTesting(countCorrect, countIncorrect);
                    }

                    @Override
                    public void onFailure(Call<StatisticsWrapper> call, Throwable t) {

                    }
                });
    }

    private void showResultTesting(int countCorrect, int countIncorrect) {
        Intent intent = endTest();
        intent.putExtra(COUNT_CORRECT, countCorrect);
        intent.putExtra(COUNT_INCORRECT, countIncorrect);
        intent.putExtra(STATISTICS, new Gson().toJson(mathTesting.getAnswers()));
        startActivity(intent);
        finish();
    }

    public Intent endTest() {
        return new Intent(this, ResultTest.class);
    }

    private void answerQuestion() {
        Question question = test.getQuestions().get(mathTesting.getCurrentQuestion());
        String item = question.getType();
        List<String> answer = new ArrayList<>();

        if (item.equals(QuestionType.WRITE_ANSWER.name().toLowerCase())) {
            answerQuestionVariantsWriteAnswer(answer);
        } else if (item.equals(QuestionType.CHOOSE_ANSWER.name().toLowerCase())) {
            answerQuestionVariantsChooseAnswer(answer);
        } else if (item.equals(QuestionType.CONFORMITY.name().toLowerCase())) {
            answerQuestionVariantsConformity(answer);
        }

        mathTesting.nextQuestion(answer);
    }

    private void answerQuestionVariantsChooseAnswer(List<String> answer) {
        for (View view : allEds) {

            if (((CheckBox) view.findViewById(R.id.check_box_choose_answer)).isChecked()) {
                answer.add(((TextView) view.findViewById(R.id.tv_choose_answer)).getText().toString());
            }
        }
    }

    private void answerQuestionVariantsConformity(List<String> answer) {
        for (int i = 0; i < COUNT_VARIANTS_CONFORMITY; i++) {
            View view = allEds.get(i);
            answer.add(((Spinner) view.findViewById(R.id.spinner_variants)).getSelectedItem().toString());
        }
    }

    private void answerQuestionVariantsWriteAnswer(List<String> answer) {
        View view = allEds.get(0);
        answer.add(((EditText) view.findViewById(R.id.edit_text_write_answer)).getText().toString());
    }

    private void clearElementsWriteAnswer() {
        View view = allEds.get(0);
        ((EditText) view.findViewById(R.id.edit_text_write_answer)).setText("");
    }

    private void loadQuestion(int index) {
        if (index < 0 || index > questions.size() - 1) {
            return;
        }

        Question question = questions.get(index);

        tvText.setText(question.getText());
        if (question.getType().equals(QuestionType.WRITE_ANSWER.name().toLowerCase())) {
            addWriteAnswer();
        } else if (question.getType().equals(QuestionType.CHOOSE_ANSWER.name().toLowerCase())) {
            addVariants(COUNT_VARIANTS_CHOOSE_ANSWER);
            loadQuestionChooseAnswer(question);
        } else if (question.getType().equals(QuestionType.CONFORMITY.name().toLowerCase())) {
            addConformity(COUNT_VARIANTS_CONFORMITY, COUNT_ANSWERS_CONFORMITY);
            loadQuestionConformity(question);
        }
    }

    private void loadQuestionChooseAnswer(Question question) {
        for (int i = 0; i < allEds.size(); i++) {
            String variant = question.getVariants().get(i);
            ((TextView) allEds.get(i).findViewById(R.id.tv_choose_answer)).setText(variant);
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
            ((TextView) view.findViewById(R.id.tv_first_part)).setText(variant);
            ((TextView) view.findViewById(R.id.tv_second_part)).setText(answer);
        }

        for (int i = COUNT_VARIANTS_CONFORMITY; i < COUNT_ANSWERS_CONFORMITY; i++) {
            View view = allEds.get(i);
            String variant = variants.get(i);
            ((TextView) view.findViewById(R.id.tv_answer_part)).setText(variant);
        }
    }


    private void addWriteAnswer() {
        passingTestL.removeAllViews();
        allEds.clear();
        final View view = getLayoutInflater().inflate(R.layout.write_answer_layout, null);
        passingTestL.addView(view);
        allEds.add(view);
    }

    private void addVariants(int count) {
        passingTestL.removeAllViews();
        allEds.clear();
        for (int i = 0; i < count; i++) {
            final View view = getLayoutInflater().inflate(R.layout.choose_variant_layout_testing, null);
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
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout_testing, null);
            Spinner spinner = view.findViewById(R.id.spinner_variants);
            String[] answers = setNumberArrayAnswer(answer);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, answers);
            spinner.setAdapter(adapter);
            passingTestL.addView(view);
            allEds.add(view);
        }

        for (int i = 0; i < answer - variant; i++) {
            final View view = getLayoutInflater().inflate(R.layout.conformity_layout_second_testing, null);
            passingTestL.addView(view);
            allEds.add(view);
        }
    }
}
