package com.restaurant.nenda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.nenda.R;
import com.restaurant.nenda.data.Constans;
import com.restaurant.nenda.data.Session;
import com.restaurant.nenda.model.RestoranResponse;
import com.restaurant.nenda.utils.DialogUtils;

public class CreateRestaurantActivity extends AppCompatActivity {

    Session session;
    EditText namarm, kategori, link_foto, alamat;
    Button create_restaurant;
    ProgressDialog progressDialog;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        userId = getIntent().getStringExtra("userId");
        initBinding();
        initClick();
    }

    private void initBinding() {
        namarm = findViewById(R.id.et_namarm);
        kategori = findViewById(R.id.et_kategori);
        link_foto = findViewById(R.id.et_link_foto);
        alamat = findViewById(R.id.et_alamat);
        create_restaurant = findViewById(R.id.btn_create_restaurant);
    }

    private void initClick() {
        create_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namarm.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "nama restauran harus diisi", Toast.LENGTH_SHORT).show();
                }else if(kategori.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "kategori harus diisi", Toast.LENGTH_SHORT).show();
                }else if(link_foto.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "url foto harus diisi", Toast.LENGTH_SHORT).show();
                }else if(alamat.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "alamat harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    createRestaurant();
                }
            }
        });
    }
    public void createRestaurant() {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(Constans.CREATE_RESTAURANT)
                .addBodyParameter("userid", userId)
                .addBodyParameter("namarm", namarm.getText().toString())
                .addBodyParameter("kategori", kategori.getText().toString())
                .addBodyParameter("link_foto", link_foto.getText().toString())
                .addBodyParameter("alamat", alamat.getText().toString())
                .build()
                .getAsObject(RestoranResponse.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RestoranResponse) {
                            RestoranResponse res = (RestoranResponse) response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(CreateRestaurantActivity.this,"Berhasil menambah restauran", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CreateRestaurantActivity.this,"Gagal menambah restauran", Toast.LENGTH_SHORT).show();
                            }
                        }
                        DialogUtils.closeDialog();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CreateRestaurantActivity.this, "Terjadi kesalahan API", Toast.LENGTH_SHORT).show();
                        Toast.makeText(CreateRestaurantActivity.this, "Terjadi kesalahan API : "+anError.getCause().toString(), Toast.LENGTH_SHORT).show();
                        DialogUtils.closeDialog();
                    }
                });
    }

}
