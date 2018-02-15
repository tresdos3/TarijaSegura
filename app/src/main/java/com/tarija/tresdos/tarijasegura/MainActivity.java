package com.tarija.tresdos.tarijasegura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tarija.tresdos.tarijasegura.fragments.DashboardFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    public static final String mypreference = "mypref";
    public static final String Tipo = "tipoKey";
    public static final String Session = "SessionKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            String type = sharedPreferences.getString(Tipo, "");
            switch (type){
                case "p":
                    setContentView(R.layout.activity_main);
                    break;
                case "h":
                    setContentView(R.layout.activity_main_child);
                    break;
            }
        }
        else {
            goLogInScreen();
        }
    }
    private void chagueVariables() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SetupDrawerContent(nvDrawer);
        fragmentManager.beginTransaction()
                .replace(R.id.content,new DashboardFragment())
                .commit();
    }

    private void SetupDrawerContent(NavigationView nvDrawer) {
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
}
