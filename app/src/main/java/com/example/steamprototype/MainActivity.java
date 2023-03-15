package com.example.steamprototype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.data_op.LocalizeGameAttribute;
import com.example.steamprototype.data_op.SettingMethods;
import com.example.steamprototype.data_op.UserDataStorage;
import com.example.steamprototype.entity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnToSignIn, btnToReg;

    public static UserDataStorage userDataStorage;
    public static GameDataStorage gameDataStorage;

    public static SettingMethods settingMethods = new SettingMethods();

    public static final int REQUEST_CODE_SIGN_IN = 69;
    public static final int RESULT_CODE_SIGN_IN_SUCCESS = 70;
    public static final int RESULT_CODE_SIGN_IN_NO_ACC = 71;
    public static final int REQUEST_CODE_REGISTER = 420;
    public static final int RESULT_CODE_REGISTER_SUCCESS = 421;

    public static final int REQUEST_CODE_STORAGE_PERMISSION = 645;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingMethods.setLanguage(MainActivity.this);
        setContentView(R.layout.activity_main);

        btnToSignIn = findViewById(R.id.btnToSignIn);
        btnToReg = findViewById(R.id.btnToReg);

        userDataStorage = new UserDataStorage(this, this);
        gameDataStorage = new GameDataStorage(this);

        btnToSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
        });

        btnToReg.setOnClickListener(view -> {
            toRegister();
        });

        copyStockAvatars();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == RESULT_CODE_REGISTER_SUCCESS) {
            String username = data.getStringExtra("username");
            String password = data.getStringExtra("password");
            String email = data.getStringExtra("email");
            User newUser = new User(username, password, email);

            if (userDataStorage.saveUserData(newUser)) {
                Toast.makeText(this, "Register success now you will be direct to the store front", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, StoreFrontActivity.class);
                intent.putExtra("user", newUser);
                startActivity(intent);
                finish();
            }
        }
        if (requestCode == REQUEST_CODE_SIGN_IN && resultCode == RESULT_CODE_SIGN_IN_NO_ACC) {
            toRegister();
        }
    }

    public void toRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }

    public void copyStockAvatars() {
        try {
            String savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/stock images/";
            File folder = new File(savePath);
            if (!folder.exists()) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_STORAGE_PERMISSION);
                folder.mkdir();
                String[] files = this.getAssets().list("stock avatar");
                for (String fileName : files) {
                    InputStream inputStream = this.getAssets().open("stock avatar/" + fileName);
                    File outFile = new File(savePath + fileName);
                    FileOutputStream fileOutputStream = new FileOutputStream(outFile);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, length);
                    }

                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(MainActivity.this, "Some stock photos \n might not be available", Toast.LENGTH_SHORT).show();
            }
        }
    }
}