package com.example.sergiomanes.ejercicio1.mvp.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.sergiomanes.ejercicio1.mvp.model.Model;
import com.example.sergiomanes.ejercicio1.mvp.view.View;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Presenter {

    private Model model;
    private View view;

    public Presenter(Model mod, View vie)
    {
        this.model = mod;
        this.view = vie;
    }

    public String getmCurrentPhotoPath()
    {
       return model.getmCurrentPhotoPath();
    }

    public int getREQUEST_TAKE_PHOTO()
    {
        return model.getREQUESTTAKEPHOTO();
    }

    public void setmCurrentPhotoPath(String path)
    {
        model.setmCurrentPhotoPath(path);
    }

@Subscribe
public void OnButtonMailPressed(com.example.sergiomanes.ejercicio1.mvp.view.View.OnButtonMailPressedEvent event) {
    if (model.getmCurrentPhotoPath() != null)
    {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/image");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(model.getmCurrentPhotoPath()));
        view.GetContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    else
    {
        Toast.makeText(view.GetActivity(),"Primero, toma una foto",Toast.LENGTH_SHORT).show();
    }

}
    @Subscribe
    public void OnButtonTakePhotoPressedEvent (com.example.sergiomanes.ejercicio1.mvp.view.View.OnButtonTakePhotoPressedEvent event)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(view.GetContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continuo una vez q se haya creado bien el archivo
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(view.GetContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                // Le digo al intent que quiero obtener la foto q se tome
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //lanzo activity esperando una respuesta
                view.GetActivity().startActivityForResult(takePictureIntent, model.getREQUESTTAKEPHOTO());
            }
        }
    }

    private File createImageFile() throws IOException {
        // Creo nombre del archivo
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "Pic_" + timeStamp;
        //tomo ruta
        File storageDir = view.GetContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

       /* File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
        if (!storageDir.exists())
            storageDir.mkdirs();*/

        //hago template del archivo
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        model.setmCurrentPhotoPath(image.getAbsolutePath());
        return image;
    }

    public void setThumb(String image)
    {
        Bitmap imageBitmap = BitmapFactory.decodeFile(image);
        view.setImageViewPreview(imageBitmap);
    }

    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(model.getmCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        view.GetActivity().sendBroadcast(mediaScanIntent);
    }



}