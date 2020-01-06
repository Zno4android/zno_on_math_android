package com.example.jatcool.zno_on_math.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.constants.ConstFile;

public class Profile extends AppCompatActivity {
EditText Fname, Name, LastName;
TextView group;
Button save_btn,cancel_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Fname = (EditText)findViewById(R.id.edFname);
        Name = (EditText)findViewById(R.id.edName);
        LastName = (EditText)findViewById(R.id.edLastName);
        group = (TextView)findViewById(R.id.tvGroup);
         setActivityData(group,Fname,Name,LastName);
          save_btn = (Button)findViewById(R.id.pr_save_btn);
          cancel_btn = (Button)findViewById(R.id.pr_cancel_btn);
          cancel_btn.setOnClickListener(
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          setActivityData(group,Fname,Name,LastName);
                      }
                  }
          );

    }


    public void setActivityData(TextView t,EditText...ed){
        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        ed[2].setText(sharedPreferences.getString("Fname",""));
        ed[0].setText(sharedPreferences.getString("FirstName",""));
        ed[1].setText(sharedPreferences.getString("LastName",""));
        t.setText(sharedPreferences.getString("Group",""));
    }
}
