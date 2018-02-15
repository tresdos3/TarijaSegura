package com.tarija.tresdos.tarijasegura;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class ProfileConfActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText name, lastname, email, ci;
    private MaterialSpinner family;
    private Button btnRegister, btnUpdate;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_conf);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        name = (TextInputEditText) findViewById(R.id.name_padre);
        lastname = (TextInputEditText) findViewById(R.id.lastname_padre);
        email = (TextInputEditText) findViewById(R.id.email_padre);
        ci = (TextInputEditText) findViewById(R.id.ci_padre);
        family = (MaterialSpinner) findViewById(R.id.select_padre);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        awesomeValidation.addValidation(this, R.id.name_padre,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.lastname_padre,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.lastnameerror);
        awesomeValidation.addValidation(this, R.id.email_padre, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.ci_padre, "^[2-9]{2}[0-9]{8}$", R.string.cierror);

        btnRegister.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                ValidateForm();
                break;
            case R.id.btnUpdate:
                break;
        }
    }
    public void ValidateForm(){
        Toast.makeText(this, "Work!", Toast.LENGTH_SHORT).show();
    }
}
