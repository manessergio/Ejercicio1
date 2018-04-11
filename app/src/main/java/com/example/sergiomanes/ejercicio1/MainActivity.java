package com.example.sergiomanes.ejercicio1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button ButtonTakePhoto;
    Button ButtonMail;
    ImageView imageViewPreview;
    final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        ButtonMail = (Button) findViewById(R.id.buttonMail);
        imageViewPreview = (ImageView) findViewById(R.id.imageViewPreview);

        ButtonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                         photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Continuo una vez q se haya creado bien el archivo
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        // Le digo al intent que quiero obtener la foto q se tome
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        //lanzo activity esperando una respuesta
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO );
                    }
                }
            }
        });

        ButtonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPhotoPath != null)
                {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("application/image");
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mCurrentPhotoPath));
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Primero, toma una foto",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle savedPhoto) {
        super.onSaveInstanceState(savedPhoto);
        //Almaceno la ruta de la ultima imagen sacada, para mostarla cuando rote el dispositivo
        savedPhoto.putString("CurrentPhoto", mCurrentPhotoPath);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedPhoto) {
        super.onRestoreInstanceState(savedPhoto);
        //recuperamos el String del Bundle
        String photo  = savedPhoto.getString("CurrentPhoto");
        if (photo != null)
        {
            mCurrentPhotoPath = photo;
            setThumb(mCurrentPhotoPath);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
           case REQUEST_TAKE_PHOTO :
                if (resultCode == RESULT_OK)
                {
                    // Muestro Thumb
                   Bundle extras = data.getExtras();
                   Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                   imageViewPreview.setImageBitmap(imageBitmap);



                   // Actualizo galeria
                    galleryAddPic();

                }
                break;

            default: break;
        }
    }

    private File createImageFile() throws IOException {
        // Creo nombre del archivo
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "Pic_" + timeStamp;
        //tomo ruta
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //hago template del archivo
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setThumb(String image)
    {
        Bitmap imageBitmap = BitmapFactory.decodeFile(image);
        imageViewPreview.setImageBitmap(imageBitmap);
    }
}



