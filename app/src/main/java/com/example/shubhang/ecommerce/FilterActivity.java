package com.example.shubhang.ecommerce;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import io.apptik.widget.MultiSlider;

public class FilterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    Spinner category_spinner;
    EditText brandName,productName;
    SeekBar simpleSeekBar;
    Button searchData;
    String category;
    Integer pos;
    Integer seekBarValue;
    TextView progressValue;
    ArrayAdapter<CharSequence> categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Auth();
        getPermissions();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        category_spinner = findViewById(R.id.categorySpinner);
        categoryAdapter = ArrayAdapter.createFromResource(this,R.array.categories,android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(categoryAdapter);
        brandName = findViewById(R.id.brandName);
        productName = findViewById(R.id.productName);
        searchData = findViewById(R.id.Search);
        simpleSeekBar =(SeekBar)findViewById(R.id.priceBar);
        progressValue = findViewById(R.id.progressValue);
        simpleSeekBar.setMax(2500);
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * seekBar.getWidth()) / seekBar.getMax();
                progressValue.setText("" + progress);
                progressValue.setX(seekBar.getX() + val - seekBar.getThumbOffset());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SharedPreferences sPref = getSharedPreferences("myFilters", Context.MODE_PRIVATE);
        brandName.setText(sPref.getString("brand", ""));
        pos = sPref.getInt("position",0);
        category_spinner.setSelection(pos);
        String price = sPref.getString("price","<0").substring(1);
        progressValue.setX(sPref.getInt("pricePosition",0));
        simpleSeekBar.setProgress(Integer.parseInt(price));
        addListeners();

    }

    private void addListeners(){

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this,MainActivity.class) ;
                //Log.d("product", String.valueOf(productName.getText()));
                Toast.makeText(FilterActivity.this, "Filter Settings Saved", Toast.LENGTH_SHORT).show();
                SharedPreferences sPref = getSharedPreferences("myFilters", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sPref.edit();
                editor.putString("name",String.valueOf(productName.getText()));
                editor.putInt("position",pos);
                editor.putString("category",category);
                seekBarValue = simpleSeekBar.getProgress();
                editor.putString("price", "<"+String.valueOf(seekBarValue));
                editor.putString("brand", String.valueOf(brandName.getText()));
                editor.putInt("pricePos", (int) progressValue.getX());
                editor.apply();
                startActivity(intent);
            }
        });

    }

    private void Auth() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

        }
        else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Could Not Sign You In", Toast.LENGTH_SHORT).show();
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }
            }
        }
    }


    private void getPermissions(){
        int perm1 = ContextCompat.checkSelfPermission(FilterActivity.this, android.Manifest.permission.READ_CONTACTS);
        int perm2 = ContextCompat.checkSelfPermission(FilterActivity.this, android.Manifest.permission.WRITE_CONTACTS);
        int perm3 = ContextCompat.checkSelfPermission(FilterActivity.this, android.Manifest.permission.SEND_SMS);
        if (perm3 == PackageManager.PERMISSION_GRANTED && (perm1 == PackageManager.PERMISSION_GRANTED
                || perm2 == PackageManager.PERMISSION_GRANTED)) {
        } else {
            ActivityCompat.requestPermissions(
                    FilterActivity.this,
                    new String[] {
                            android.Manifest.permission.READ_CONTACTS,
                            android.Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.SEND_SMS
                    },
                    121
            );
        }
    }

}
