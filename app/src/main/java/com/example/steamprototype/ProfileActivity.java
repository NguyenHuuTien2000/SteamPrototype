package com.example.steamprototype;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.steamprototype.adapter.PurchaseListAdapter;
import com.example.steamprototype.data_op.UserLibraryStorage;
import com.example.steamprototype.entity.Game;
import com.example.steamprototype.entity.User;
import com.example.steamprototype.network.LocationAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private static final int ACTIVITY_SELECT_IMAGE = 406;
    ImageView img_profile;
    TextView txtV_profile_username, txtV_profile_location, txtV_profile_email;
    Button btn_profile_library, btn_profile_wishlist;

    User user;
    UserLibraryStorage userLibraryStorage = StoreFrontActivity.userLibraryStorage;

    List<Game> wishListArr;
    List<Game> libListArr;

    private String imagePath = "";

    private FusedLocationProviderClient fusedLocationClient;

    private PurchaseListAdapter purchaseListAdapter;
    private ListView purchaseList;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        init();

        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");

        txtV_profile_username.setText(user.getUsername());
        txtV_profile_email.setText(user.getEmail());

        setLocationByGoogle();

        SharedPreferences sharedPreferences = getSharedPreferences("User Profile Pic", MODE_PRIVATE);
        imagePath = sharedPreferences.getString(user.getUsername(), "");
        if (imagePath.isEmpty()) {
            img_profile.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
        } else {
            img_profile.setImageBitmap(getAvatarImage(this.imagePath));
        }


        img_profile.setOnClickListener(view -> {
            Intent pickIntent = new Intent();
            pickIntent.setType("image/*");
            pickIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), ACTIVITY_SELECT_IMAGE);
        });

        wishListArr = this.user.getWishlist();
        libListArr = this.user.getLibrary();

        libListArr.sort(Comparator.comparing(Game::getDateAdded));
        loadListView((ArrayList<Game>) this.libListArr);

        btn_profile_library.setText(getString(R.string.game) + "\n" + libListArr.size());
        btn_profile_wishlist.setText(getString(R.string.wishlist) + "\n" + wishListArr.size());

        btn_profile_library.setOnClickListener(v -> goToLibrary());

        btn_profile_wishlist.setOnClickListener(v -> goToWishList());

    }

    public void init(){
        img_profile = findViewById(R.id.img_profile);
        txtV_profile_username = findViewById(R.id.txtV_profile_username);
        txtV_profile_location = findViewById(R.id.txtV_profile_location);
        txtV_profile_email = findViewById(R.id.txtV_profile_email);
        btn_profile_library = findViewById(R.id.btn_profile_library);
        btn_profile_wishlist = findViewById(R.id.btn_profile_wishlist);
        purchaseList = findViewById(R.id.profile_purchase_list);
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

                SharedPreferences sharedPreferences = getSharedPreferences("User Profile Pic", MODE_PRIVATE);
                sharedPreferences.edit().putString(user.getUsername(), imagePath).apply();
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
            File folder = new File(savePath);
            if (!folder.exists()) {
                folder.mkdir();
            }
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

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION }, 99);
        }
    }

    public void setLocationByGoogle() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    Address address = addresses.get(0);

                    ArrayList<String> addressParts = new ArrayList<>();

                    addressParts.add(address.getSubAdminArea());
                    addressParts.add(address.getAdminArea());
                    addressParts.add(address.getCountryName());

                    txtV_profile_location.setText(TextUtils.join("\n", addressParts));
                } catch (Exception e) {

                }
            }
        });
    }

    public void loadListView(ArrayList<Game> displayList) {
        this.purchaseListAdapter = new PurchaseListAdapter(ProfileActivity.this, displayList);
        this.purchaseList.setAdapter(this.purchaseListAdapter);
    }

    private void setLocationByIP() {
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
}
