package com.example.jatcool.zno_on_math.activity.user;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;

import java.util.Locale;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.COUNT_CORRECT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.COUNT_INCORRECT;
import static java.lang.String.format;

public class ResultTest extends AppCompatActivity {

        TextView tvResultTest;
        ListView answers;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_test);
                Bundle values = getIntent().getExtras();
                double countCorrect = values.getInt(COUNT_CORRECT);
                double countInCorrect = values.getInt(COUNT_INCORRECT);

                tvResultTest = findViewById(R.id.Result_Test);
                answers = findViewById(R.id.Result_Test_Questions);

                tvResultTest.setText(format(Locale.ENGLISH, "%.2f", countCorrect + countInCorrect / countCorrect));
    }
}
