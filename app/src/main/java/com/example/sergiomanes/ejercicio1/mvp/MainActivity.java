package com.example.sergiomanes.ejercicio1.mvp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sergiomanes.ejercicio1.R;
import com.example.sergiomanes.ejercicio1.mvp.Utils.BusProvider;
import com.example.sergiomanes.ejercicio1.mvp.model.model;
import com.example.sergiomanes.ejercicio1.mvp.presenter.presenter;
import com.example.sergiomanes.ejercicio1.mvp.view.view;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
    private presenter Presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Presenter = new presenter(new model(), new view(MainActivity.this, BusProvider.getInstance()));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedPhoto) {
        super.onSaveInstanceState(savedPhoto);
        //Almaceno la ruta de la ultima imagen sacada, para mostarla cuando rote el dispositivo
        savedPhoto.putString("CurrentPhoto", Presenter.getmCurrentPhotoPath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedPhoto) {
        super.onRestoreInstanceState(savedPhoto);
        //recuperamos el String del Bundle
        String photo  = savedPhoto.getString("CurrentPhoto");
        if (photo != null)
        {
            Presenter.setmCurrentPhotoPath(photo);
            Presenter.setThumb(Presenter.getmCurrentPhotoPath());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 0 : //REQUEST_TAKE_PHOTO
                if (resultCode == RESULT_OK)
                {
                    // Muestro Thumb
                    Bundle extras = data.getExtras();
                    Presenter.setThumb(Presenter.getmCurrentPhotoPath());

                    // Actualizo galeria
                    Presenter.galleryAddPic();

                }
                break;

            default: break;
        }
    }






}



