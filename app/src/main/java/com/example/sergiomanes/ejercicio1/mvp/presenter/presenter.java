package com.example.sergiomanes.ejercicio1.mvp.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.sergiomanes.ejercicio1.mvp.model.model;
import com.example.sergiomanes.ejercicio1.mvp.MainActivity;
import com.example.sergiomanes.ejercicio1.mvp.view.view;
import com.example.sergiomanes.ejercicio1.mvp.view.viewActivity;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;

public class presenter {

    private model Model;
    private view View;

    public presenter(model Mod, view Vie)
    {
        this.Model = Mod;
        this.View = Vie;
    }

    public String getmCurrentPhotoPath()
    {
       return Model.getmCurrentPhotoPath();
    }

    public int getREQUEST_TAKE_PHOTO()
    {
        return Model.getREQUEST_TAKE_PHOTO();
    }

    public void setmCurrentPhotoPath(String path)
    {
        Model.setmCurrentPhotoPath(path);
    }

@Subscribe
public void OnButtonMailPressed(view.OnButtonMailPressedEvent event) {
    if (Model.getmCurrentPhotoPath() != null)
    {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/image");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Model.getmCurrentPhotoPath()));
        View.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    else
    {
        Toast.makeText(View.GetActivity(),"Primero, toma una foto",Toast.LENGTH_SHORT).show();
    }

}
    @Subscribe
    public void OnButtonTakePhotoPressedEvent (view.OnButtonTakePhotoPressedEvent event)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(View.getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continuo una vez q se haya creado bien el archivo
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(View.getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                // Le digo al intent que quiero obtener la foto q se tome
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //lanzo activity esperando una respuesta
                View.GetActivity().startActivityForResult(takePictureIntent, Model.getREQUEST_TAKE_PHOTO());
            }
        }
    }

    private File createImageFile() throws IOException {
        // Creo nombre del archivo
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "Pic_" + timeStamp;
        //tomo ruta
        File storageDir = View.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

       /* File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
        if (!storageDir.exists())
            storageDir.mkdirs();*/

        //hago template del archivo
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Model.setmCurrentPhotoPath(image.getAbsolutePath());
        return image;
    }

    public void setThumb(String image)
    {
        Bitmap imageBitmap = BitmapFactory.decodeFile(image);
        View.setImageViewPreview(imageBitmap);
    }

    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(Model.getmCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        View.GetActivity().sendBroadcast(mediaScanIntent);
    }



}