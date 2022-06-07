package com.example.steamprototype;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
        user = dataStorage.getUserData("","");

        if (user == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", (dialog, id) -> {
                Intent intent = getIntent();
                setResult(MainActivity.RESULT_CODE_SIGN_IN_NO_ACC);
                finish();
            });
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, id) -> {

            });
            builder.setMessage("No account was found do you want to create one?");
            builder.setTitle("Cannot retrieve account data");
            builder.setIcon(R.drawable.steamdeck_steamlogo);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        btnSignIn.setOnClickListener(view -> {
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            if (checkInput(username, password)) {
                Intent intent = new Intent(SignInActivity.this, StoreFrontActivity.class);
                intent.putExtra("user", (Parcelable) this.user);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, this.errorMsg,Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean checkInput(String name, String pass) {
        if (name.isEmpty() || pass.isEmpty()) {
            errorMsg = "Check your name or password and try again";
            return false;
        }
        if (!name.equals(user.getUsername()) || !pass.equals(user.getPassword())) {
            errorMsg = "Username or password do not match";
            return false;
        }
        return true;
    }

    public void innit() {
        btnSignIn = findViewById(R.id.btnSignIn);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPass);
    }
}
