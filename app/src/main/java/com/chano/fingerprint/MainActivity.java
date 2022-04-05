package com.chano.fingerprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chano.fingerprint.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //binding
    private ActivityMainBinding binding;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(R.layout.activity_main);
        context = getBaseContext();

        //revisar estado del sensor
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(context, "Si tiene sensor de huella dactilar y se puede usar", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(context, "No tiene sendsor de de huella dactilar", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(context, "Si tiene sensor, pero no esta disponible", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(context, "No hay huellas dactilares registradas en el telefono", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}