package com.example.jatcool.zno_on_math.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.ProfileListAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.User;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.PROFILE_CAN_NOT_CHANGE;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.FATHERNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.FIRSTNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.GROUP;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.IMAGE;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.LASTNAME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.STATISTICS;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.PROFILE_SUCCESS_CHANGE;

public class Profile extends AppCompatActivity {
    EditText etFartherName, etFirstname, etLastname;
    TextView group;
    Button save_btn, cancel_btn;
    ProgressBar pr;
    String token;
    Bitmap bitmap = null;
    User user;
    List<Statistics> mStatisticsWrappers;
    ListView mResultList;
    ImageView img;
    static final int GALLERY_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Профіль");
        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        token = sharedPreferences.getString(TOKEN, "");
        mResultList = findViewById(R.id.ProfileResultList);
        GetSudentData();
        img = findViewById(R.id.profileImage);
        etFartherName = findViewById(R.id.edFname);
        etFirstname = findViewById(R.id.edName);
        etLastname = findViewById(R.id.edLastName);
        group = findViewById(R.id.tvGroup);
        setActivityData(group, etFartherName, etFirstname, etLastname);
        pr = findViewById(R.id.Timer);
        save_btn = findViewById(R.id.pr_save_btn);
        cancel_btn = findViewById(R.id.pr_cancel_btn);
//        img.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                        photoPickerIntent.setType("image/*");
//                        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
//                    }
//                }
//        );
        cancel_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setActivityData(group, etFartherName, etFirstname, etLastname);

                    }
                }
        );

        save_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!etFartherName.getText().toString().equals("") && !etFirstname.getText().toString().equals("") && !etLastname.getText().toString().equals("")) {
                            user = new User();
                            user.setFathername(etFartherName.getText().toString());
                            user.setFirstname(etFirstname.getText().toString());
                            user.setLastname(etLastname.getText().toString());
                            Change(user);
                        } else
                            Toast.makeText(Profile.this, "Одне з полів порожнє", Toast.LENGTH_SHORT)
                                    .show();

                    }
                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                      bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//                        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//                        byte [] b = baos.toByteArray();
//                        user = new User();
//                        user.setFathername(etFartherName.getText().toString());
//                        user.setFirstname(etFirstname.getText().toString());
//                        user.setLastname(etLastname.getText().toString());
//                        user.setImage(b);
//                        Change(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Change(user);
                }
        }
    }


    public void GetSudentData() {
        NetworkService.getInstance()
                .getJSONApi()
                .getMyStatistic(token)
                .enqueue(new Callback<List<Statistics>>() {
                    @Override
                    public void onResponse(Call<List<Statistics>> call, Response<List<Statistics>> response) {
                        if (response.isSuccessful()) {
                            setListData(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Statistics>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }


    public void setActivityData(TextView t, EditText... ed) {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        ed[0].setText(sharedPreferences.getString(FATHERNAME, ""));
        ed[1].setText(sharedPreferences.getString(FIRSTNAME, ""));
        ed[2].setText(sharedPreferences.getString(LASTNAME, ""));
        t.setText(sharedPreferences.getString(GROUP, ""));
    }


    public void Change(final User user) {
        pr.setVisibility(View.VISIBLE);
        NetworkService.getInstance()
                .getJSONApi()
                .Change(token, user)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(FATHERNAME, user.getFathername());
                            editor.putString(FIRSTNAME, user.getFirstname());
                            editor.putString(LASTNAME, user.getLastname());
                            //editor.putString(IMAGE,user.getImage());
                            editor.commit();
                            //img.setImageBitmap(bitmap);
                            Toast.makeText(Profile.this, PROFILE_SUCCESS_CHANGE, Toast.LENGTH_SHORT)
                                    .show();
                            pr.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(Profile.this, PROFILE_CAN_NOT_CHANGE, Toast.LENGTH_SHORT)
                                .show();
                        pr.setVisibility(View.GONE);
                    }
                });
    }
    private void setListData(Response<List<Statistics>> response){
        mStatisticsWrappers = response.body();
        ProfileListAdapter profileListAdapter = new ProfileListAdapter(Profile.this, R.layout.profile_list, mStatisticsWrappers);
        mResultList.setAdapter(profileListAdapter);

        mResultList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Statistics stat = (Statistics) parent.getItemAtPosition(position);
                        Intent detailProfile = new Intent(Profile.this, ProfileDetail.class);
                        detailProfile.putExtra(STATISTICS, new Gson().toJson(stat));
                        startActivity(detailProfile);
                    }
                }
        );
    }
}

