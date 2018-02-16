package com.tarija.tresdos.tarijasegura;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.valdesekamdem.library.mdtoast.MDToast;

public class Intro_Activity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("Rapida..."
                , "Solo selecciones a nos de sus hijos...",
                R.drawable.tasks,
                Color.parseColor("#f39c12")));
        addSlide(AppIntroFragment.newInstance("Seguro...",
                "Aplicacion 100% segura",
                R.drawable.shield,
                Color.parseColor("#2980b9")));
        addSlide(AppIntroFragment.newInstance("Seguimiento...",
                "Siga las actividades de sus hijos...",
                R.drawable.mobile,
                Color.parseColor("#7f8c8d")));
        addSlide(AppIntroFragment.newInstance("Cuida...",
                "a tu familia",
                R.drawable.family,
                Color.parseColor("#273833")));
        addSlide(AppIntroFragment.newInstance("Te lo recomienda...",
                "2018",
                R.drawable.photo,
                Color.parseColor("#f39c12")));
        showStatusBar(false);
    }
    @Override
    public void onDonePressed(Fragment currentFragment) {
        Intent i = new Intent(Intro_Activity.this, SelectChildActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        Intent i = new Intent(Intro_Activity.this, SelectChildActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onSlideChanged() {
        MDToast mdToast = MDToast.makeText(getApplicationContext(), ":)", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }
}
