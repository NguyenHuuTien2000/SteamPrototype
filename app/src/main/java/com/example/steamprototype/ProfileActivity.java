package com.example.steamprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steamprototype.data_op.GameDataStorage;
import com.example.steamprototype.data_op.UserLibraryStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity  extends AppCompatActivity {
    ImageView img_profile;
    TextView txtV_profile_username, txtV_profile_location;
    Button btn_profile_library,btn_profile_wishlist;

    User user;
    UserLibraryStorage userLibraryStorage = StoreFrontActivity.userLibraryStorage;

    List<Game> wishListArr;
    List<Game> libListArr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        init();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        txtV_profile_username.setText(user.getUsername());
        txtV_profile_location.setText("Location");
        wishListArr = (ArrayList<Game>) this.user.getWishlist();
        libListArr = (ArrayList<Game>) this.user.getLibrary();
        btn_profile_library.setText(getString(R.string.game) + "\t" + libListArr.size());
        btn_profile_wishlist.setText(getString(R.string.wishlist) + "\t" + wishListArr.size());

        btn_profile_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLibrary();
            }
        });

        btn_profile_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWishList();
            }
        });

    }

    public void init(){
        img_profile = (ImageView) findViewById(R.id.img_profile);
        txtV_profile_username = (TextView) findViewById(R.id.txtV_profile_username);
        txtV_profile_location = (TextView) findViewById(R.id.txtV_profile_location);
        btn_profile_library = (Button) findViewById(R.id.btn_profile_library);
        btn_profile_wishlist = (Button) findViewById(R.id.btn_profile_wishlist);
    }

    public void goToLibrary( ) {
        Intent goIntent = new Intent(ProfileActivity.this, LibraryActivity.class);
        goIntent.putExtra("user", this.user);
        startActivity(goIntent);
    }
    public void goToWishList(){
        Intent goIntent = new Intent(ProfileActivity.this, WishListActivity.class);
        goIntent.putExtra("user", this.user);
        startActivity(goIntent);
    }
}
