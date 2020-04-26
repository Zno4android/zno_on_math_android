package com.example.jatcool.zno_on_math.activity.user;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.TestResultAdapter;
import com.example.jatcool.zno_on_math.util.Answer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Locale;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.COUNT_CORRECT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.COUNT_INCORRECT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATISTICS;
import static java.lang.String.format;

public class ResultTest extends AppCompatActivity {

    TextView tvResultTest;
    ListView answersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_test);
        Bundle values = getIntent().getExtras();
        double countCorrect = values.getInt(COUNT_CORRECT);
        double countInCorrect = values.getInt(COUNT_INCORRECT);
        String js = values.getString(STATISTICS);
        List<Answer> answers = new Gson().fromJson(js, new TypeToken<List<Answer>>() {
        }.getType());

        TestResultAdapter adapter = new TestResultAdapter(this, R.layout.detail_list_view, answers);

        tvResultTest = findViewById(R.id.Result_Test);
        answersListView = findViewById(R.id.Result_Test_Questions);

        answersListView.setAdapter(adapter);

        tvResultTest.setText(format(Locale.ENGLISH, "%.2f", countCorrect / (countCorrect + countInCorrect) * 100) + " %");

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.detach(fragment);
        transaction.attach(fragment);
        transaction.commit();
    }
}
