package com.example.sergiomanes.ejercicio1.mvp.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sergiomanes.ejercicio1.R;
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class view extends viewActivity {

    private final Bus bus;

    @BindView(R.id.buttonTakePhoto)
    Button ButtonTakePhoto;
    @BindView(R.id.buttonMail)
    Button ButtonMail;
    @BindView(R.id.imageViewPreview)
    ImageView imageviewPreview;

    public view(Activity Act, Bus BUS) {
        super(Act);
        this.bus = BUS;
        ButterKnife.bind(this, Act);
    }

    public void setImageViewPreview(Bitmap imageBitmap)
    {
        imageviewPreview.setImageBitmap(imageBitmap);
    }

    @OnItemClick(R.id.buttonTakePhoto)
    public void OnButtonTakePhotoPressed()
    {
        bus.post(new OnButtonTakePhotoPressedEvent());
    }

    @OnItemClick(R.id.buttonMail)
    public void OnButtonMailPressed()
    {
        bus.post(new OnButtonMailPressedEvent());
    }


    public static class OnButtonTakePhotoPressedEvent {}

    public static class OnButtonMailPressedEvent {}
}
