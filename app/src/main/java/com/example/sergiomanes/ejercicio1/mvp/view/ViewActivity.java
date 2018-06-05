package com.example.sergiomanes.ejercicio1.mvp.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public class ViewActivity {
    private WeakReference<Activity>  ActivityRef;

    public ViewActivity(Activity Act)
    {
        ActivityRef = new WeakReference<>(Act);
    }

    @Nullable
    public Activity GetActivity()
    {
        return ActivityRef.get();
    }

    @Nullable
    public Context GetContext()
    {
        return GetActivity();
    }

    @Nullable
    public FragmentManager getFragmentManager()
    {
        Activity Act = GetActivity();
        return (Act != null) ? Act.getFragmentManager() : null;
    }
}
