package com.example.sergiomanes.ejercicio1.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.sergiomanes.ejercicio1.R;
import com.example.sergiomanes.ejercicio1.mvp.Utils.BusProvider;
import com.example.sergiomanes.ejercicio1.mvp.model.Model;
import com.example.sergiomanes.ejercicio1.mvp.presenter.Presenter;
import com.example.sergiomanes.ejercicio1.mvp.view.View;

public class MainActivity extends AppCompatActivity {

    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new Presenter(new Model(), new View(MainActivity.this, BusProvider.getInstance()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.register(presenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
       BusProvider.unregister(presenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedPhoto) {
        super.onSaveInstanceState(savedPhoto);
        //Almaceno la ruta de la ultima imagen sacada, para mostarla cuando rote el dispositivo
        savedPhoto.putString("CurrentPhoto", presenter.getmCurrentPhotoPath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedPhoto) {
        super.onRestoreInstanceState(savedPhoto);
        //recuperamos el String del Bundle
        String photo  = savedPhoto.getString("CurrentPhoto");
        if (photo != null)
        {
            presenter.setmCurrentPhotoPath(photo);
            presenter.setThumb(presenter.getmCurrentPhotoPath());
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
                    presenter.setThumb(presenter.getmCurrentPhotoPath());

                    // Actualizo galeria
                    presenter.galleryAddPic();

                }
                break;

            default: break;
        }
    }






}



