package com.example.jatcool.zno_on_math.activity.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.FilesAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.constants.URLConstants;
import com.example.jatcool.zno_on_math.entity.Theoretics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_THEORY_CAN_NOT_ADD_THEORY;
import static com.example.jatcool.zno_on_math.constants.ErrorMessageConstants.ADD_THEORY_NOT_FILL_FIELD;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.THEME;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;
import static com.example.jatcool.zno_on_math.constants.SuccessMessageConstants.ADD_THEORY_SUCCESS_ADD_THEORY;

public class AddTheory extends AppCompatActivity {

    Button btnAddTheory;
    EditText etTheoryName;
    EditText etTheoryText;
    TextView tvThemeTheory;
    static final String FILE_DIR = "files/";
    List<String> files = new ArrayList<>();
    List<String> allPath = new ArrayList<>();
    ImageButton addFiles;
    RecyclerView filesList;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theory);
        LinearLayoutManager horizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance(URLConstants.FIREBASE_URL);
        btnAddTheory = findViewById(R.id.add_theory);
        etTheoryName = findViewById(R.id.et_name_theory);
        etTheoryText = findViewById(R.id.text_theory);
        tvThemeTheory = findViewById(R.id.theme_theory);
        addFiles = findViewById(R.id.addFilesButton);
        filesList = findViewById(R.id.addTheotyListOfFiles);
        filesList.setLayoutManager(horizontal);

        addFiles.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent filesExpoler = new Intent(Intent.ACTION_PICK);
                        startActivityForResult(filesExpoler, 1);
                    }
                }
        );

        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        final String token = sharedPreferences.getString(TOKEN, "");
        Bundle values = getIntent().getExtras();
        final String theme = values.getString(THEME);

        tvThemeTheory.setText(theme);

        btnAddTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etTheoryName.getText().toString();
                String text = etTheoryText.getText().toString();

                if (name.isEmpty() || text.isEmpty()) {
                    Toast.makeText(AddTheory.this, ADD_THEORY_NOT_FILL_FIELD, Toast.LENGTH_LONG)
                            .show();
                    return;
                }


                Theoretics theoretics = new Theoretics();
                theoretics.setText(text);
                theoretics.setName(name);
                theoretics.setTheme(theme);
                theoretics.setFiles(files);

                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType(null)
                        .build();

                for (int i = 0; i < files.size(); i++) {
                    UploadTask storageReference = storage.getReference().child(FILE_DIR + files.get(i)).putFile(Uri.fromFile(new File(allPath.get(i))), metadata);
                }

                NetworkService.getInstance()
                        .getJSONApi()
                        .addTheory(token, theoretics)
                        .enqueue(new Callback<Theoretics>() {
                            @Override
                            public void onResponse(Call<Theoretics> call, Response<Theoretics> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(AddTheory.this, ADD_THEORY_SUCCESS_ADD_THEORY, Toast.LENGTH_LONG)
                                            .show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Theoretics> call, Throwable t) {
                                Toast.makeText(AddTheory.this, ADD_THEORY_CAN_NOT_ADD_THEORY, Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedFile = data.getData();
        File f = new File(selectedFile.getPath());
        allPath.add(selectedFile.getPath());
        files.add(f.getName());
        FilesAdapter adapter = new FilesAdapter(this, files);
        filesList.setAdapter(adapter);
    }
}
