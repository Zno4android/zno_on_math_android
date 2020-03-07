package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tests extends AppCompatActivity {

    TextView tvTheme;
    TextView tvText;
    Button btnPrev;
    Button btnSkip;
    Button btnNext;
    ListView variantsList;
    ArrayAdapter<String> adapter;
    Test test;
    MathTesting mathTesting;
    LinearLayout testLayout;
    String kol_variants[] = new String[]{"2", "3", "4", "5", "6"};
    String otvet[] = new String[]{"1", "2", "3", "4", "5"};
    String type_test[] = new String[]{"Виберіть правельну(ні) відповідь(ді)", "Відповідність", "Вести відповідь",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvTheme = findViewById(R.id.Test_theme_textview);
        tvText = findViewById(R.id.Test_task);
        btnPrev = findViewById(R.id.Test_prev_btn);
        btnSkip = findViewById(R.id.Test_skip_btn);
        btnNext = findViewById(R.id.Test_next_btn);
        //variantsList = findViewById(R.id.Test_variants);
        testLayout = findViewById(R.id.Test_liner_layout);
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
                        Question question = test.getQuestions().get(0);
                        tvText.setText(question.getText());
                        loadQuestion(question);
                    }

                    @Override
                    public void onFailure(Call<com.example.jatcool.zno_on_math.entity.Test> call, Throwable t) {

                    }
                });

//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, test.getQuestions().get(0).getVariants());
//        variantsList.setAdapter(adapter);
//        variantsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String answer = adapter.getItem(position);
//                mathTesting.nextQuestion(answer);
//
//                if (mathTesting.isPassAllQuestions()) {
//                    btnSkip.setVisibility(View.GONE);
//                }
//
//                if (mathTesting.isTestPass()) {
//                    int countCorrect = mathTesting.getCountCorrect();
//                    int countIncorrect = mathTesting.getCountIncorrect();
//                    SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
//                    String token = sharedPreferences.getString("token", "");
//                    List<Answer> answers = mathTesting.getAnswers();
//                    setDataInDBStatistics(token, test.getId(), countCorrect, countIncorrect);
//                    setDataInDBResultQuestion(token, answers);
//                    showResultTesting(countCorrect, countIncorrect);
//                }
//            }
//        });


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mathTesting.skipQuestion();
                loadQuestion(test.getQuestions().get(mathTesting.getCurrentQuestion()));
                if (mathTesting.isPassAllQuestions()) {
                    btnSkip.setVisibility(View.GONE);
                }
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

    private void loadQuestion(Question question) {
        tvText.setText(question.getText());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, test.getQuestions().get(0).getVariants());
        adapter.notifyDataSetChanged();
    }

    private void variant_test_type(int kol) {
        testLayout.removeAllViews();
        for (int i = 0; i < kol; i++) {
            LinearLayout.LayoutParams ediText = new LinearLayout.LayoutParams(450, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams radio = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            RadioButton otv = new RadioButton(this);
            EditText desc_var = new EditText(this);
            LinearLayout line = new LinearLayout(this);
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.addView(otv, radio);
            line.addView(desc_var, ediText);
            LinearLayout.LayoutParams r = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            testLayout.addView(line, r);
        }

    }

    private void repectively_test_type() {

        for (int i = 0; i < 5; i++) {
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);
            l.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            EditText e = new EditText(this);
            LinearLayout.LayoutParams Edparams = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
            Spinner sp = new Spinner(this);
            sp.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, otvet));
            l.addView(e, Edparams);
            l.addView(sp, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (i == 4) {
                e.setVisibility(View.INVISIBLE);
                sp.setVisibility(View.INVISIBLE);
            }
            LinearLayout.LayoutParams rightText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView t = new TextView(this);
            t.setText(String.valueOf(i + 1));
            EditText ed = new EditText(this);
            rightText.gravity = Gravity.RIGHT;
            l.addView(t, rightText);
            LinearLayout.LayoutParams righEd = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
            righEd.gravity = Gravity.RIGHT;
            l.addView(ed, righEd);
            testLayout.addView(l);
            ;
        }
    }
    private void once_answer_test_type(){
        EditText t = new EditText(getApplicationContext());
        LinearLayout.LayoutParams radio = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        testLayout.addView(t,radio);
    }
}
