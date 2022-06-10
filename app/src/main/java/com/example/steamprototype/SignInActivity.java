package com.example.steamprototype;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.data_op.UserDataStorage;
import com.example.steamprototype.entity.User;


public class SignInActivity extends AppCompatActivity {
    Button btnSignIn;
    EditText editUsername, editPassword;
    CheckBox checkSave;
    private User user;
    private String errorMsg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        innit();

        UserDataStorage dataStorage = MainActivity.userDataStorage;

        if (!dataStorage.checkContains()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", (dialog, id) -> {
                setResult(MainActivity.RESULT_CODE_SIGN_IN_NO_ACC);
                finish();
            });
            builder.setNegativeButton("No", (dialog, id) -> {

            });
            builder.setMessage("No account was found do you want to create one?");
            builder.setTitle("Cannot retrieve account data");
            builder.setIcon(R.drawable.steamdeck_steamlogo);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        this.user = dataStorage.getCurrentUser();
        if (this.user != null) {
            editUsername.setText(user.getUsername());
            editPassword.setText(user.getPassword());
            checkSave.setChecked(true);
        }

        btnSignIn.setOnClickListener(view -> {
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            if (checkInput(username, password)) {
                if (checkSave.isChecked()) {
                    dataStorage.saveCurrentUser(username, password, this.user.getEmail());
                } else {
                    dataStorage.clearCurrentUser();
                }
                Intent intent = new Intent(SignInActivity.this, StoreFrontActivity.class);
                intent.putExtra("User", this.user);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, this.errorMsg,Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean checkInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            this.errorMsg = "Username or password cannot be empty";
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

        if (!checkPattern) {
            return false;
        }

        this.user = MainActivity.userDataStorage.getUserData(username, password);
        if (this.user == null) {
            this.errorMsg = "Username or password do not match";
            return false;
        }
        return true;
    }

    public void innit() {
        btnSignIn = findViewById(R.id.btnSignIn);
        checkSave = findViewById(R.id.checkRemember);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPass);
    }
}
