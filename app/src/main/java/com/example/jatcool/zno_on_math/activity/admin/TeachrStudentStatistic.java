package com.example.jatcool.zno_on_math.activity.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.activity.user.ProfileDetail;
import com.example.jatcool.zno_on_math.adapters.ProfileListAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.User;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STUDENT;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;

public class TeachrStudentStatistic extends AppCompatActivity {

    TextView tvName, tvFname, tvLastName, tvGroup;
    ListView studentResultList;
    String token;
    List<Statistics> statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachr_student_statistic);
        tvName = findViewById(R.id.tvName);
        tvFname = findViewById(R.id.tvFname);
        tvLastName = findViewById(R.id.tvLastName);
        tvGroup = findViewById(R.id.tvGroup);
        studentResultList = findViewById(R.id.ProfileResultListToTeacher);
        Bundle st = getIntent().getExtras();
        Bundle bundle = getIntent().getExtras();
        String js = bundle.getString(STUDENT);
        User student = new Gson().fromJson(js, User.class);
        tvFname.setText(student.getFathername());
        tvName.setText(student.getFathername());
        tvLastName.setText(student.getLastname());
        tvGroup.setText(student.getGroup());
        SharedPreferences s = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        token = s.getString(TOKEN, "");
        NetworkService.getInstance()
                .getJSONApi()
                .getStudentStatistic(token, student.getId())
                .enqueue(
                        new Callback<List<Statistics>>() {
                            @Override
                            public void onResponse(Call<List<Statistics>> call, Response<List<Statistics>> response) {
                                if (response.isSuccessful()) {
                                    statistics = response.body();
                                    InitList();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Statistics>> call, Throwable t) {

                            }
                        }
                );
    }

    private void InitList() {
        ProfileListAdapter adapter = new ProfileListAdapter(TeachrStudentStatistic.this, R.layout.profile_list, statistics);
        studentResultList.setAdapter(adapter);

        studentResultList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Statistics st = (Statistics) parent.getItemAtPosition(position);
                        Intent detailProfile = new Intent(TeachrStudentStatistic.this, ProfileDetail.class);
                        detailProfile.putExtra("Statistic", new Gson().toJson(st));
                        startActivity(detailProfile);
                    }
                }
        );
    }
}
