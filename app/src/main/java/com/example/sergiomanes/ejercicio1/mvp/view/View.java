package com.example.sergiomanes.ejercicio1.mvp.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sergiomanes.ejercicio1.R;
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class View extends ViewActivity {

    private final Bus bus;

    @BindView(R.id.buttonTakePhoto)
    Button buttonTakePhoto;
    @BindView(R.id.buttonMail)
    Button buttonMail;
    @BindView(R.id.imageViewPreview)
    ImageView imageViewPreview;

    public View(Activity act, Bus bus) {
        super(act);
        this.bus = bus;
        ButterKnife.bind(this, act);
    }

    public void setImageViewPreview(Bitmap imageBitmap)
    {
        imageViewPreview.setImageBitmap(imageBitmap);
    }

    @OnClick(R.id.buttonTakePhoto)
    public void OnButtonTakePhotoPressed()
    {
        bus.post(new OnButtonTakePhotoPressedEvent());
    }

    @OnClick(R.id.buttonMail)
    public void OnButtonMailPressed()
    {
        bus.post(new OnButtonMailPressedEvent());
    }


    public static class OnButtonTakePhotoPressedEvent {}

    public static class OnButtonMailPressedEvent {}
}
