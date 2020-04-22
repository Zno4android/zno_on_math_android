package com.example.jatcool.zno_on_math.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class Zno extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView mFirstName, mLastName, mGroup;
     private ImageView img;
     private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Bundle values = getIntent().getExtras();
        navigationView = findViewById(R.id.nav_view);
        if(values.getString("status").equals(Status.Teacher.getName())) {
            navigationView.inflateMenu(R.menu.activity_zno_drawer);
        }
        else {
            navigationView.inflateMenu(R.menu.student_menu);
        }
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_theory, R.id.nav_home, R.id.nav_student,R.id.nav_theme)
                    .setDrawerLayout(drawer)
                    .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerLayout = navigationView.getHeaderView(0);
        mFirstName = headerLayout.findViewById(R.id.FirstName);
        mLastName = headerLayout.findViewById(R.id.LastName);
        mGroup = headerLayout.findViewById(R.id.GroupText);
        img = headerLayout.findViewById(R.id.imageView);
        img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Zno.this,Profile.class));
                    }
                }
        );
        mFirstName.setText(values.getString("FirstName"));
        mLastName.setText(values.getString("LastName"));
        mGroup.setText(values.getString("Group"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.zno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out: {
                new File(ConstFile.SHARED_PREFENCES_START_PATH + ConstFile.FILE_NAME).delete();
                startActivity(new Intent(Zno.this, Authorization.class));
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View headerLayout = navigationView.getHeaderView(0);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        mFirstName = headerLayout.findViewById(R.id.FirstName);
        mLastName = headerLayout.findViewById(R.id.LastName);
        mFirstName.setText(sharedPreferences.getString("FirstName", ""));
        mLastName.setText(sharedPreferences.getString("LastName", ""));
    }
}
