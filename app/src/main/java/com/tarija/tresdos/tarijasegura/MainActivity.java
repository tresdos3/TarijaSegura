package com.tarija.tresdos.tarijasegura;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Button;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.karan.churi.PermissionManager.PermissionManager;
import com.tarija.tresdos.tarijasegura.fragments.ChildListFragment;
import com.tarija.tresdos.tarijasegura.fragments.DashboardFragment;
import com.tarija.tresdos.tarijasegura.fragments.EmergencyFragment;
import com.tarija.tresdos.tarijasegura.fragments.NewChildFragment;
import com.tarija.tresdos.tarijasegura.other.PolicyManager;
import com.tarija.tresdos.tarijasegura.service.BrowserService;
import com.tarija.tresdos.tarijasegura.service.ContactsService;
import com.tarija.tresdos.tarijasegura.service.DetectAppService;
import com.tarija.tresdos.tarijasegura.service.EmergencyService;
import com.tarija.tresdos.tarijasegura.service.LocationService;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> granted;
    ArrayList<String> deneid;

    private LinearLayout l1;
    private ConstraintLayout c1;

    private Button sendMessage, logout;

    private TextView father_ci, father_mail, father_phone, father_name, father_family, child_name;

    private FirebaseUser user;
    private DatabaseReference rootRef, profileRef, childRef;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private PolicyManager policyManager;
    private static final int REQUEST_CALL = 1;
    String device_unique_id,IMEI;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    public  static final int RequestPermissionCode  = 1 ;
    public static  final String NombreHIJO = "Nombrehijo";
    public static final String message = "btnKey";
    public static final String message2 = "btnAdmin";
    public static final String message3 = "btnServices";
    public static final String message4 = "btnSecurity";

    private PermissionManager permissionManager;
    private final int REQUEST_LOCATION = 200;
    private LocationManager lm;

    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    public static final String mypreference = "mypref";
    public static final String Tipo = "tipoKey";
    public static final String Session = "SessionKey";
    public static final String Huid = "HuidKey";
    public static final String star = "star";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        policyManager = new PolicyManager(this);
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        if (user != null){
            profileRef = rootRef.child(user.getUid());
            childRef = rootRef.child(user.getUid()).child("/hijos");
            String type = sharedPreferences.getString(Tipo, "");
            switch (type){
                case "p":
                    setContentView(R.layout.activity_main);
                    chagueVariables();
                    RegisterToken();
                    if (!sharedPreferences.contains(message))
                        alertPermission();
                    else {
                        String texto = sharedPreferences.getString(message,"");
                        if (!texto.equals("true")){
                            alertPermission();
                        }
                    }
                    break;
                case "h":
                    setContentView(R.layout.activity_main_child);
                    getSupportActionBar().hide();

                    policyManager = new PolicyManager(this);
                    lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

                    l1 = (LinearLayout) findViewById(R.id.ll);
                    c1 = (ConstraintLayout) findViewById(R.id.cl);
                    father_name = (TextView) findViewById(R.id.father_name);
                    father_ci = (TextView) findViewById(R.id.father_ci);
                    father_mail = (TextView) findViewById(R.id.father_mail);
                    father_phone = (TextView) findViewById(R.id.father_phone);
                    father_family = (TextView) findViewById(R.id.father_family);
                    child_name = (TextView) findViewById(R.id.child_name);
                    sendMessage = (Button) findViewById(R.id.sendMessage);
                    logout =  (Button) findViewById(R.id.logout);

                    sendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, PinLogoutActivity.class);
                            startActivity(intent);
                        }
                    });

                    ChargeProfile();
                    RegisterTokenChild();

                    String texto = sharedPreferences.getString(message2,"");
                    Log.d("Mensaje: ", texto);
                    if (texto.equals("true")){
                        Intent intentInternet = new Intent(MainActivity.this, ContactsService.class);
                        startService(intentInternet);
                        Intent intentBrowser = new Intent(MainActivity.this, BrowserService.class);
                        startService(intentBrowser);
                        Intent intentApps = new Intent(MainActivity.this, DetectAppService.class);
                        startService(intentApps);
                        Intent intentEmer = new Intent(MainActivity.this, EmergencyService.class);
                        startService(intentEmer);
                        Intent intentGeo = new Intent(MainActivity.this, LocationService.class);
                        startService(intentGeo);
                        Log.d("Mensaje: ", "Servicio iniciado");
                    }
                    break;
            }
        }
        else {
            goLogInScreen();
        }
    }
    private void alertPermission() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Atencion!")
                .setContentText("Por favor acepta los siguientes permisos :).")
                .setCustomImage(R.drawable.smartphone)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        permissionManager = new PermissionManager() {};
                        permissionManager.checkAndRequestPermissions(MainActivity.this);
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},10);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(message, "true");
                        editor.commit();
                    }
                })
                .show();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        String type = sharedPreferences.getString(Tipo, "");
        switch (type){
            case "p":
                menuInflater.inflate(R.menu.actionmenuf, menu);
                MenuItem item = menu.findItem(R.id.search);
                item.setVisible(false);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);

        granted = permissionManager.getStatus().get(0).granted;
        deneid = permissionManager.getStatus().get(0).denied;
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
            case R.id.addNew:
                FragmentClass = NewChildFragment.class;
                break;
            case R.id.childs:
                FragmentClass = ChildListFragment.class;
                break;
            case R.id.emergency:
                FragmentClass = EmergencyFragment.class;
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
                            cerrarServicio();
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
    private void RegisterToken(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        profileRef.child("tokenP").setValue(refreshedToken);
    }
    private void RegisterTokenChild(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        profileRef.child("tokenH").setValue(refreshedToken);
    }
    private void ViewProfile() {
        c1.setVisibility(View.GONE);
        l1.setVisibility(View.VISIBLE);
    }
    private void ChargeProfile() {
//        HideProfile();
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
                                    ViewProfile();

                                    if (!sharedPreferences.contains(message))
                                        alertPermission();
                                    if (!sharedPreferences.contains(message3))
                                            AdminDevice();
//                                    if (!sharedPreferences.contains(message4))


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
    private  void AdminDevice(){
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Atencion!")
                .setContentText("Activa el metodo de seguriada para evitar que la aplicacion sea quitada del telefono")
                .setCustomImage(R.drawable.smartphone)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if (!policyManager.isAdminActive()) {
                            Intent activateDeviceAdmin = new Intent(
                                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            activateDeviceAdmin.putExtra(
                                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                    policyManager.getAdminComponent());
                            activateDeviceAdmin
                                    .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                            "Al activar esta funcion la aplicacion Tarija Segura no podra ser quitada de este telefono cellular :)");
                            startActivityForResult(activateDeviceAdmin,
                                    PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(message2, "true");
                        editor.commit();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK
                && requestCode == PolicyManager.DPM_ACTIVATION_REQUEST_CODE) {
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean iniciarServicio(){
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Atencion!")
                .setContentText("Activar todos los servicios de Tarija Segura :)")
                .setCustomImage(R.drawable.smartphone)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        Intent intentInternet = new Intent(MainActivity.this, ContactsService.class);
                        startService(intentInternet);
                        Intent intentBrowser = new Intent(MainActivity.this, BrowserService.class);
                        startService(intentBrowser);
                        Intent intentApps = new Intent(MainActivity.this, DetectAppService.class);
                        startService(intentApps);
                        Intent intentEmer = new Intent(MainActivity.this, EmergencyService.class);
                        startService(intentEmer);
                        Intent intentGeo = new Intent(MainActivity.this, LocationService.class);
                        startService(intentGeo);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(message3, "true");
                        editor.commit();
                    }
                })
                .show();
        return true;
    }
    private void cerrarServicio(){
        Intent intentGeo = new Intent(this, LocationService.class);
        this.stopService(intentGeo);
        Intent intentInternet = new Intent(this, ContactsService.class);
        this.stopService(intentInternet);
        Intent intentBrowser = new Intent(this, BrowserService.class);
        this.stopService(intentBrowser);
        Intent intentApps = new Intent(this, DetectAppService.class);
        this.stopService(intentApps);
        Intent intentEmer = new Intent(this, EmergencyService.class);
        this.stopService(intentEmer);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(message, "false");
        editor.commit();
    }
}
