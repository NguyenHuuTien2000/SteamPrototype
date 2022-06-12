package com.example.steamprototype;

import android.content.Intent;
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
            String username = editNewUsername.getText().toString().trim();
            String password = editNewPassword.getText().toString();
            String email = editNewEmail.getText().toString();

            if (checkInput(username, password, email)) {
                Intent intent = getIntent();
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("email", email);
                setResult(MainActivity.RESULT_CODE_REGISTER_SUCCESS, intent);
                finish();
            } else {
                Toast.makeText(this, this.errorMsg,Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean checkInput(String username, String password, String email) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            errorMsg = "Name,password or email cannot be empty";
            return false;
        }

        boolean checkPattern = true;
        String pattern = "^[a-zA-Z0-9]+$";
        if (!username.matches(pattern)) {
            this.errorMsg = "Username invalid, cannot contains special characters\n";
            checkPattern = false;
        }

        pattern = "^[\\S]{6,}$";
        if (!password.matches(pattern)) {
            this.errorMsg += "Password invalid, must be at least 6 characters long\n";
            checkPattern = false;
        }

        pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!email.matches(pattern)) {
            this.errorMsg += "Email format invalid\n";
            checkPattern = false;
        }

        if (!checkPattern) {
            return false;
        }

        UserDataStorage dataStorage = MainActivity.userDataStorage;
        User storedUser = dataStorage.getUserData(username, password);
        if (storedUser != null) {
            if (storedUser.getUsername().equals(username)) {
                errorMsg += "Username, ";
            }
            if (storedUser.getPassword().equals(password)) {
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
