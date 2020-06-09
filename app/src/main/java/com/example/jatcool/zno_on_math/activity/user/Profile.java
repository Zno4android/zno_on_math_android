package com.example.jatcool.zno_on_math.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.ProfileListAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.constants.URLConstants;
import com.example.jatcool.zno_on_math.entity.Statistics;
import com.example.jatcool.zno_on_math.entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
    AlertDialog.Builder builder;
    String token;
    EditText newPassword;
    EditText oldPassword;
    Button accept;
    Button cancel;
    View customLayout;
    AlertDialog dialog;
    FirebaseStorage storage;
    Button change_password_btn;
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
        user = new User();
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance(URLConstants.FIREBASE_URL);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        token = sharedPreferences.getString(TOKEN, "");
        mResultList = findViewById(R.id.ProfileResultList);
        GetSudentData();
        img = findViewById(R.id.profileImage);
        etFartherName = findViewById(R.id.edFname);
        change_password_btn = findViewById(R.id.btn_password_change_pr);
        etFirstname = findViewById(R.id.edName);
        etLastname = findViewById(R.id.edLastName);
        group = findViewById(R.id.tvGroup);
        setActivityData(group, etFartherName, etFirstname, etLastname);
        pr = findViewById(R.id.Timer);
        save_btn = findViewById(R.id.pr_save_btn);
        cancel_btn = findViewById(R.id.pr_cancel_btn);
        change_password_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertDialogButtonClicked();
                    }
                }
        );
        img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                    }
                }
        );
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

    public void showAlertDialogButtonClicked() {
        builder = new AlertDialog.Builder(this);
        customLayout = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        builder.setView(customLayout);
        builder.setTitle("Зміна пароля");
        builder.setCancelable(false);
        accept = customLayout.findViewById(R.id.accept_password_change_btn);
        cancel = customLayout.findViewById(R.id.cancel_password_change_btn);
        newPassword = customLayout.findViewById(R.id.new_password_ed);
        oldPassword = customLayout.findViewById(R.id.old_password_ed);
        dialog = builder.create();
        dialog.show();
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );

        accept.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePassword();
                    }
                }
        );
    }

    private void changePassword() {
        if (!newPassword.getText().toString().equals("") && !oldPassword.getText().toString().equals("")) {
            JsonObject passwordObject = new JsonObject();
            passwordObject.addProperty("oldPassword", oldPassword.getText().toString());
            passwordObject.addProperty("newPassword", newPassword.getText().toString());
            View waitLayout = getLayoutInflater().inflate(R.layout.dialog_wait, null);
            builder.setView(waitLayout);
            dialog.hide();
            builder.setTitle("Запит до серверу");
            dialog = builder.create();
            dialog.show();
            NetworkService.getInstance()
                    .getJSONApi()
                    .changePassword(token, passwordObject)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                dialog.dismiss();

                                Toast.makeText(Profile.this, "Ви успішно змінили пароль", Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                try {
                                    JSONObject message = new JSONObject(response.errorBody().string());
                                    Toast.makeText(Profile.this, message.getString("message"), Toast.LENGTH_SHORT)
                                            .show();

                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }

                                if (customLayout.getParent() != null) {
                                    ((ViewGroup) customLayout.getParent()).removeAllViews();
                                }
                                dialog.dismiss();
                                builder.setView(customLayout);
                                dialog = builder.create();
                                dialog.show();
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            final View customLayout = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
                            builder.setView(customLayout);
                            dialog = builder.create();
                            dialog.show();
                        }
                    });
        } else {
            Toast.makeText(Profile.this, "Одне з полів порожне", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);



        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    final Uri selectedImage = imageReturnedIntent.getData();
                    try {
                       StorageMetadata metadata = new StorageMetadata.Builder()
                                .setContentType("image/jpeg")
                                .build();
                       final String path = selectedImage.getLastPathSegment();
                        UploadTask storageReference = storage.getReference().child("images/"+path).putFile(selectedImage,metadata);
                        storageReference.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                user.setImage(path);
                                Change(user);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        String imgPath = sharedPreferences.getString(IMAGE,"");
        ed[0].setText(sharedPreferences.getString(FATHERNAME, ""));
        ed[1].setText(sharedPreferences.getString(FIRSTNAME, ""));
        ed[2].setText(sharedPreferences.getString(LASTNAME, ""));
        t.setText(sharedPreferences.getString(GROUP, ""));
        if(!imgPath.isEmpty()) {
            Glide.with(Profile.this).load(storage.getReference("/images" + imgPath)).into(img);
        }
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
                            User newUser = response.body();
                            SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(FATHERNAME, newUser.getFathername());
                            editor.putString(FIRSTNAME, newUser.getFirstname());
                            editor.putString(LASTNAME, newUser.getLastname());
                            editor.putString(IMAGE, newUser.getImage());
                            editor.commit();
                            if (!newUser.getImage().isEmpty()) {
                                Glide.with(Profile.this).load(storage.getReference("/images" + newUser.getImage())).into(img);
                                editor.putString(IMAGE, newUser.getImage());
                            }
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

