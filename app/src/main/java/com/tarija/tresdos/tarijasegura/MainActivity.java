package com.tarija.tresdos.tarijasegura;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarija.tresdos.tarijasegura.fragments.DashboardFragment;
import com.tarija.tresdos.tarijasegura.fragments.NewChildFragment;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LinearLayout l1;
    private ConstraintLayout c1;

    private TextView father_ci, father_mail, father_phone, father_name, father_family, child_name;

    private FirebaseUser user;
    private DatabaseReference rootRef, profileRef, childRef;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    public static final String mypreference = "mypref";
    public static final String Tipo = "tipoKey";
    public static final String Session = "SessionKey";
    public static final String Huid = "HuidKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        l1 = (LinearLayout) findViewById(R.id.ll);
        c1 = (ConstraintLayout) findViewById(R.id.cl);

        father_name = (TextView) findViewById(R.id.father_name);
        father_ci = (TextView) findViewById(R.id.father_ci);
        father_mail = (TextView) findViewById(R.id.father_mail);
        father_phone = (TextView) findViewById(R.id.father_phone);
        father_family = (TextView) findViewById(R.id.father_family);
        child_name = (TextView) findViewById(R.id.child_name);

        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        profileRef = rootRef.child(user.getUid());
        childRef = rootRef.child(user.getUid()).child("/hijos");

        if (user != null){
            String type = sharedPreferences.getString(Tipo, "");
            switch (type){
                case "p":
                    setContentView(R.layout.activity_main);
                    chagueVariables();
                    break;
                case "h":
                    setContentView(R.layout.activity_main_child);
                    getSupportActionBar().hide();
                    break;
            }
        }
        else {
            goLogInScreen();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        String type = sharedPreferences.getString(Tipo, "");
        switch (type){
            case "p":
                menuInflater.inflate(R.menu.actionmenuf, menu);
                break;
            default:
                Log.d("User", "Error en switch");
        }
        return true;
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

    private void SetupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SelectItemDrawer(item);
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout){
            signOut();
        }
        if (item.getItemId() == R.id.addNew){
            fragmentManager.beginTransaction()
                    .replace(R.id.content,new NewChildFragment())
                    .addToBackStack(null)
                    .commit();
        }
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void SelectItemDrawer(MenuItem menuItem) {
        Fragment myFragment = null;
        Class FragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.db:
                FragmentClass = DashboardFragment.class;
                break;
            default:
                FragmentClass = DashboardFragment.class;
        }
        try {
            myFragment = (Fragment) FragmentClass.newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.content,myFragment)
                .addToBackStack(null)
                .commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            messages(MDToast.TYPE_SUCCESS, "Ha terminado su sesion");
                            goLogInScreen();
                        } else {
                            messages(MDToast.TYPE_ERROR, "Error al cerrar sesion");
                        }
                    }
                });
    }
    public void messages(int type, String message){
        MDToast.makeText(this, message, MDToast.LENGTH_LONG, type).show();
    }
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStackImmediate();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
    private void RegisterToken(){}
    private void ViewProfile() {
        c1.setVisibility(View.GONE);
        l1.setVisibility(View.VISIBLE);
    }
    private void HideProfile() {
        c1.setVisibility(View.VISIBLE);
        l1.setVisibility(View.GONE);
    }
    private void ChargeProfile() {
        HideProfile();
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    profileRef.child("profile").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            father_name.setText(dataSnapshot.child("name").getValue().toString() + " " + dataSnapshot.child("lastname").getValue().toString());
                            father_ci.setText(dataSnapshot.child("ci").getValue().toString());
                            father_mail.setText(dataSnapshot.child("email").getValue().toString());
                            father_phone.setText(user.getPhoneNumber());
                            father_family.setText(dataSnapshot.child("family").getValue().toString());
                            String uid = sharedPreferences.getString(Huid,"");
                            childRef.child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> map = (Map)dataSnapshot.getValue();
                                    child_name.setText(map.get("nombre"));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    messages(MDToast.TYPE_ERROR,"Algo salio mal :(");
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            messages(MDToast.TYPE_ERROR,"Algo salio mal :(");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                messages(MDToast.TYPE_ERROR,"Esto es vergonzoso :)");
            }
        });
    }
}
