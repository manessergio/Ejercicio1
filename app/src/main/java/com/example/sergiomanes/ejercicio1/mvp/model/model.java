package com.example.sergiomanes.ejercicio1.mvp.model;

public class model {

    private final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;

    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setmCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public int getREQUEST_TAKE_PHOTO() {
        return REQUEST_TAKE_PHOTO;
    }
}
