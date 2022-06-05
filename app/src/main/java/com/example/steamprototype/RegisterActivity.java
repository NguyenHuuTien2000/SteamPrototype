package com.example.steamprototype;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.data_op.UserDataStorage;
import com.example.steamprototype.entity.User;


public class RegisterActivity extends AppCompatActivity {
    Button btnReg;
    EditText editNewUsername, editNewPassword, editNewEmail;
    private String errorMsg = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        innit();

        btnReg.setOnClickListener(view -> {
            String username = editNewUsername.getText().toString();
            String password = editNewPassword.getText().toString();
            String email = editNewUsername.getText().toString();

            if (checkInput(username, password, email)) {
                Intent intent = getIntent();
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("email", email);
                setResult(MainActivity.RESULT_CODE_REGISTER_SUCCESS);
                finish();
            } else {
                Toast.makeText(this, this.errorMsg,Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean checkInput(String name, String pass, String email) {
        if (name.isEmpty() || pass.isEmpty() || email.isEmpty()) {
            errorMsg = "Check your name,password or email and try again";
            return false;
        }
        UserDataStorage dataStorage = MainActivity.userDataStorage;
        User storedUser = dataStorage.getUserData();
        if (storedUser != null) {
            if (storedUser.getUsername().equals(name)) {
                errorMsg += "Username, ";
            }
            if (storedUser.getPassword().equals(pass)) {
                errorMsg += "Password, ";
            }
            if (storedUser.getEmail().equals(email)) {
                errorMsg += "Email ";
            }
            errorMsg = errorMsg.substring(errorMsg.length() - 2);
            errorMsg += " already taken";
            return false;
        }
        return true;
    }

    public void innit() {
        btnReg = findViewById(R.id.btnReg);
        editNewUsername = findViewById(R.id.editNewUsername);
        editNewPassword = findViewById(R.id.editNewPass);
        editNewEmail = findViewById(R.id.editNewEmail);
    }
}
