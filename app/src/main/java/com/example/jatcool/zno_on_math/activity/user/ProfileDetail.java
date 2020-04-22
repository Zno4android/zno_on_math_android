package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.DetailStatisticAdapter;
import com.example.jatcool.zno_on_math.entity.Question;
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.google.gson.Gson;

import java.util.List;

public class ProfileDetail extends AppCompatActivity {

ListView questionResultList;
TextView testName, testScore;
List<Question> questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        Bundle bundle = getIntent().getExtras();
        String js = bundle.getString("Statistic");
        Statistics statistics = new Gson().fromJson(js, Statistics.class);
        questionResultList = findViewById(R.id.TestResultList_ProfileDetail);
        testName = findViewById(R.id.TestName_DetailProfile);
        testScore = findViewById(R.id.TestScore_ProfileDetail);
        questionResultList = findViewById(R.id.TestResultList_ProfileDetail);
        questions = statistics.getTest().getQuestions();
        testName.setText(statistics.getTest().getName());
        testScore.setText("Ви відповіли на "+statistics.getResult()+"% питань правильно");
        DetailStatisticAdapter adapter = new DetailStatisticAdapter(this, R.layout.detail_list_view,questions);
        questionResultList.setAdapter(adapter);
    }

}