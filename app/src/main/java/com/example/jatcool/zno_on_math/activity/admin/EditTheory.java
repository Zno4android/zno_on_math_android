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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jatcool.zno_on_math.R;
import com.example.jatcool.zno_on_math.adapters.FilesAdapter;
import com.example.jatcool.zno_on_math.adapters.SimpleAdapterTheme;
import com.example.jatcool.zno_on_math.connection.NetworkService;
import com.example.jatcool.zno_on_math.constants.ConstFile;
import com.example.jatcool.zno_on_math.constants.URLConstants;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.entity.Theoretics;
import com.example.jatcool.zno_on_math.listeners.MathKeyboardActionListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.kexanie.library.MathView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.THEORY;
import static com.example.jatcool.zno_on_math.constants.SharedPreferencesConstants.TOKEN;

public class EditTheory extends AppCompatActivity {

    Spinner themeSpinner;
    EditText editTheoryEdit, theoryTextEdit;
    Button editBtn;
    static final String FILE_DIR = "files/";
    ListView list;
    RecyclerView ediFileList;
    List<String> files = new ArrayList<>();
    List<String> allPath = new ArrayList<>();
    MathView textQuestionMathView;
    KeyboardView mKeyboardView;
    FirebaseStorage storage;
    ImageButton addFile;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_theory);
        Bundle ar = getIntent().getExtras();
        String js = ar.getString(THEORY);
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance(URLConstants.FIREBASE_URL);
        addFile = findViewById(R.id.editFilesButton);
        ediFileList = findViewById(R.id.editTheotyListOfFiles);
        textQuestionMathView = findViewById(R.id.text_question_math);

        Keyboard mKeyboard = new Keyboard(this, R.xml.keyboard);

        mKeyboardView = findViewById(R.id.keyboardview);

        mKeyboardView.setKeyboard(mKeyboard);

        mKeyboardView.setPreviewEnabled(false);

        mKeyboardView.setOnKeyboardActionListener(new MathKeyboardActionListener(theoryTextEdit));

        LinearLayoutManager horizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ediFileList.setLayoutManager(horizontal);

        SharedPreferences s = getSharedPreferences(ConstFile.FILE_NAME.replace(".xml",""),MODE_PRIVATE);
        token = s.getString(TOKEN, "");
        final Theoretics theory = new Gson().fromJson(js, Theoretics.class);
        editTheoryEdit = findViewById(R.id.editTheoryEdit);
        theoryTextEdit = findViewById(R.id.theoryTextEdit);
        editBtn = findViewById(R.id.editTheoryButton);
        themeSpinner = findViewById(R.id.editTheorySpiner);
        editTheoryEdit.setText(theory.getName());
        theoryTextEdit.setText(theory.getText());

        FilesAdapter adapter = new FilesAdapter(this, theory.getFiles());
        files = theory.getFiles();
        ediFileList.setAdapter(adapter);

        theoryTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openKeyboard(v);
                } else {
                    closeKeyboard(v);
                }
            }
        });

        theoryTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = theoryTextEdit.getText().toString();
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

        NetworkService.getInstance()
                .getJSONApi()
                .getAllTheme(token)
                .enqueue(
                        new Callback<List<Theme>>() {
                            @Override
                            public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                                if(response.isSuccessful()){
                                    SimpleAdapterTheme adapter = new SimpleAdapterTheme(EditTheory.this, R.layout.simple_list_view, response.body());
                                    themeSpinner.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Theme>> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Щось пішло не так!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                );
        addFile.setOnClickListener(
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

        editBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!editTheoryEdit.getText().toString().equals("") && !theoryTextEdit.getText().toString().equals("")){
                            theory.setText(theoryTextEdit.getText().toString().replace("\\", "\\\\"));
                            theory.setName(editTheoryEdit.getText().toString());
                            theory.setTheme(themeSpinner.getSelectedItem().toString());
                            AlertDialog.Builder waiterBuild = new AlertDialog.Builder(EditTheory.this);
                            View waitLayout = getLayoutInflater().inflate(R.layout.dialog_wait, null);
                            waiterBuild.setView(waitLayout);
                            waiterBuild.setTitle("Запит до серверу");
                            waiterBuild.setCancelable(false);
                            final AlertDialog waiter = waiterBuild.create();
                            waiter.show();
                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .updateTheory(token,theory.getId(),theory)
                                    .enqueue(
                                            new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                    if(response.isSuccessful()){
                                                        StorageMetadata metadata = new StorageMetadata.Builder()
                                                                .setContentType(null)
                                                                .build();
                                                        for (int i = allPath.size() - 1; i >= 0; i--) {
                                                            UploadTask storageReference = storage.getReference().child(FILE_DIR + files.get(i)).putFile(Uri.parse(allPath.get(i)), metadata);
                                                        }
                                                        waiter.dismiss();
                                                        Toast.makeText(getApplicationContext(), response.body().get("message").toString(), Toast.LENGTH_SHORT)
                                                                .show();
                                                        finish();
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), "Щось пішло не так!", Toast.LENGTH_SHORT)
                                                                .show();
                                                        waiter.dismiss();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(), "Щось пішло не так!", Toast.LENGTH_SHORT)
                                                            .show();
                                                    waiter.dismiss();
                                                }
                                            }
                                    );
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Одне з полів порожне",Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
        );
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getOrder();
        switch (item.getItemId()) {
            case 1: {
                if (allPath.size() > 0) {
                    allPath.remove((files.size() - allPath.size()) - position);
                }
                files.remove(position);
                FilesAdapter adapter = new FilesAdapter(this, files);
                ediFileList.setAdapter(adapter);
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
                if (!files.contains(f.getName())) {
                    allPath.add(selectedFile + "");
                    files.add(f.getName());
                    FilesAdapter adapter = new FilesAdapter(this, files);
                    ediFileList.setAdapter(adapter);
                }
            }
        }
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
}
