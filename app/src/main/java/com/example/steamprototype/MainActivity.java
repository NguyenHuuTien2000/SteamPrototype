package com.example.steamprototype;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.steamprototype.data_op.UserDataStorage;
import com.example.steamprototype.entity.User;

public class MainActivity extends AppCompatActivity {
    Button btnToSignIn, btnToReg;

    public static UserDataStorage userDataStorage;

    public static final int REQUEST_CODE_SIGN_IN = 69;
    public static final int RESULT_CODE_SIGN_IN_SUCCESS = 70;
    public static final int RESULT_CODE_SIGN_IN_NO_ACC = 71;
    public static final int REQUEST_CODE_REGISTER = 420;
    public static final int RESULT_CODE_REGISTER_SUCCESS = 421;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToSignIn = findViewById(R.id.btnToSignIn);
        btnToReg = findViewById(R.id.btnToReg);
        userDataStorage = new UserDataStorage(this, this);

        btnToSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
        });

        btnToReg.setOnClickListener(view -> {
            toRegister();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == RESULT_CODE_REGISTER_SUCCESS) {
            String username = data.getStringExtra("username");
            String password = data.getStringExtra("password");
            String email = data.getStringExtra("email");
            User newUser = new User(username, password, email);
            userDataStorage.saveUserData(newUser);
            Toast.makeText(this, "Register success now you will be direct to the store front", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, StoreFrontActivity.class);
            startActivity(intent);
        }
        if (requestCode == REQUEST_CODE_SIGN_IN && resultCode == RESULT_CODE_SIGN_IN_NO_ACC) {
            toRegister();
        }
    }

    public void toRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }
}