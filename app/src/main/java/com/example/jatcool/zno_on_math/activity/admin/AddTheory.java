package com.example.jatcool.zno_on_math.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.FilesAdapter;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.constants.URLConstants;
import com.example.jatcool.zno_on_math.entity.Theoretics;
import com.example.jatcool.zno_on_math.listeners.MathKeyboardActionListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.kexanie.library.MathView;
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
    MathView textQuestionMathView;
    KeyboardView mKeyboardView;
    RecyclerView filesList;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theory);
        LinearLayoutManager horizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance(URLConstants.FIREBASE_URL);

        textQuestionMathView = findViewById(R.id.text_question_math);
        btnAddTheory = findViewById(R.id.add_theory);
        etTheoryName = findViewById(R.id.et_name_theory);
        etTheoryText = findViewById(R.id.text_theory);
        tvThemeTheory = findViewById(R.id.theme_theory);
        addFiles = findViewById(R.id.addFilesButton);
        filesList = findViewById(R.id.addTheotyListOfFiles);
        filesList.setLayoutManager(horizontal);

        Keyboard mKeyboard = new Keyboard(this, R.xml.keyboard);

        mKeyboardView = findViewById(R.id.keyboardview);

        mKeyboardView.setKeyboard(mKeyboard);

        mKeyboardView.setPreviewEnabled(false);

        mKeyboardView.setOnKeyboardActionListener(new MathKeyboardActionListener(etTheoryText));

        addFiles.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent filesExpoler = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        filesExpoler.setType("application/*");
                        filesExpoler.addCategory(Intent.CATEGORY_OPENABLE);
                        //String[] mimetypes = {"application/x-binary,application/octet-stream"};
                        filesExpoler.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        //filesExpoler.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                        startActivityForResult(filesExpoler, 1);
                    }
                }
        );

        etTheoryText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openKeyboard(v);
                } else {
                    closeKeyboard(v);
                }
            }
        });

        etTheoryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etTheoryText.getText().toString();
                StringBuilder mathText = new StringBuilder();
                Pattern pattern = Pattern.compile("\\$\\$.*\\$\\$");
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    mathText.append(text.substring(matcher.start(), matcher.end()));
                }
                textQuestionMathView.setText(mathText.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml", ""), MODE_PRIVATE);
        final String token = sharedPreferences.getString(TOKEN, "");
        Bundle values = getIntent().getExtras();
        final String theme = values.getString(THEME);
        tvThemeTheory.setText(theme);

        btnAddTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etTheoryName.getText().toString();
                String text = etTheoryText.getText().toString().replace("\\", "\\\\");

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

                AlertDialog.Builder waiterBuild = new AlertDialog.Builder(AddTheory.this);
                View waitLayout = getLayoutInflater().inflate(R.layout.dialog_wait, null);
                waiterBuild.setView(waitLayout);
                waiterBuild.setTitle("Запит до серверу");
                waiterBuild.setCancelable(false);
                final AlertDialog waiter = waiterBuild.create();
                waiter.show();
                NetworkService.getInstance()
                        .getJSONApi()
                        .addTheory(token, theoretics)
                        .enqueue(new Callback<Theoretics>() {
                            @Override
                            public void onResponse(Call<Theoretics> call, Response<Theoretics> response) {
                                if (response.isSuccessful()) {
                                    StorageMetadata metadata = new StorageMetadata.Builder()
                                            .setContentType(null)
                                            .build();

                                    for (int i = 0; i < files.size(); i++) {
                                        UploadTask storageReference = storage.getReference().child(FILE_DIR + files.get(i)).putFile(Uri.parse(allPath.get(i)), metadata);
                                    }
                                    waiter.dismiss();
                                    Toast.makeText(AddTheory.this, ADD_THEORY_SUCCESS_ADD_THEORY, Toast.LENGTH_LONG)
                                            .show();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Theoretics> call, Throwable t) {
                                Toast.makeText(AddTheory.this, ADD_THEORY_CAN_NOT_ADD_THEORY, Toast.LENGTH_LONG)
                                        .show();
                                waiter.dismiss();
                            }
                        });
            }
        });
    }

    public void openKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void closeKeyboard(View v) {
        mKeyboardView.setVisibility(View.GONE);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getOrder();
        switch (item.getItemId()) {
            case 1: {
                files.remove(position);
                allPath.remove(position);
                FilesAdapter adapter = new FilesAdapter(this, files);
                filesList.setAdapter(adapter);
                break;
            }
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            Uri selectedFile = data.getData();
            File f = new File(selectedFile.getPath());
            try {
                int a = Integer.parseInt(f.getName());
                Toast.makeText(this, "Файл не може бути доданний", Toast.LENGTH_SHORT)
                        .show();
            } catch (Exception e) {
                if (!allPath.contains(selectedFile + "")) {
                    allPath.add(selectedFile + "");
                    files.add(f.getName());
                    FilesAdapter adapter = new FilesAdapter(this, files);
                    filesList.setAdapter(adapter);
                }
            }
        }
    }
}
