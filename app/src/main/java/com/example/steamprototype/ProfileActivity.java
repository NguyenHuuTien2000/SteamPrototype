package com.example.steamprototype;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.steamprototype.network.LocationAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity  extends AppCompatActivity {
    private static final int ACTIVITY_SELECT_IMAGE = 406;
    ImageView img_profile;
    TextView txtV_profile_username, txtV_profile_location;
    Button btn_profile_library,btn_profile_wishlist;

    User user;
    UserLibraryStorage userLibraryStorage = StoreFrontActivity.userLibraryStorage;

    List<Game> wishListArr;
    List<Game> libListArr;

    private String imagePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        init();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");

        getLocation();
        txtV_profile_username.setText(user.getUsername());

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        imagePath = sharedPreferences.getString("profile_pic", "");
        if (imagePath.isEmpty()) {
            img_profile.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
        } else {
            img_profile.setImageBitmap(getAvatarImage(this.imagePath));
        }


        img_profile.setOnClickListener(view -> {
            Intent pickIntent = null;
            pickIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickIntent, ACTIVITY_SELECT_IMAGE);
        });

        wishListArr = this.user.getWishlist();
        libListArr = this.user.getLibrary();

        btn_profile_library.setText(getString(R.string.game) + "\n" + libListArr.size());
        btn_profile_wishlist.setText(getString(R.string.wishlist) + "\n" + wishListArr.size());

        btn_profile_library.setOnClickListener(v -> goToLibrary());

        btn_profile_wishlist.setOnClickListener(v -> goToWishList());

    }

    private void getLocation() {
        LocationAPI locationAPI = new LocationAPI();
        locationAPI.setListener(new LocationAPI.OnLocateCompleteListener() {
            @Override
            public void onCompleted(String text) {
                txtV_profile_location.setText(text);
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        locationAPI.execute();
    }

    public void init(){
        img_profile = findViewById(R.id.img_profile);
        txtV_profile_username = findViewById(R.id.txtV_profile_username);
        txtV_profile_location = findViewById(R.id.txtV_profile_location);
        btn_profile_library = findViewById(R.id.btn_profile_library);
        btn_profile_wishlist = findViewById(R.id.btn_profile_wishlist);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Bitmap image = BitmapFactory.decodeStream(imageStream);
                img_profile.setImageBitmap(image);

                imagePath = saveImage(this,"profile_pic.png",image);

                SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
                sharedPreferences.edit().putString("profile_pic", imagePath).apply();
            }
        }
    }

    public Bitmap getAvatarImage(String path) {
        File imgFile = new  File(path);
        Bitmap bitmap = null;
        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return bitmap;
    }

    public String saveImage(Activity activity, String imageName, Bitmap image) {
        String path = "";
        try {
            String savePath = activity.getApplicationInfo().dataDir + "/avatar/";
            File file = new File(savePath + imageName);
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            path = savePath + imageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
