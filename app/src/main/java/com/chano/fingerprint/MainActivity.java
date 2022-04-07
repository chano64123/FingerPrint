package com.chano.fingerprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.chano.fingerprint.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //binding
    private ActivityMainBinding binding;

    private Context context;

    //Control biometrico
    private BiometricManager biometricManager;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initObjects();

        View view = binding.getRoot();
        setContentView(view);

        initListeners();

        checkBiometricSensor();
    }

    private void initObjects() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        context = getBaseContext();
    }

    private void initListeners() {
        binding.ibLogin.setOnClickListener(this);
    }

    private void checkBiometricSensor() {
        //revisar estado del sensor
        biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                binding.tvMessage.setText(R.string.text_bimetric_success);
                setFingerPintIcon(true, getString(R.string.color_green));
                createDialogAuthentication();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                binding.tvMessage.setText(R.string.text_biometric_error_no_hardware);
                setFingerPintIcon(false, getString(R.string.color_red));
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                binding.tvMessage.setText(R.string.text_biometric_error_hw_unavailable);
                setFingerPintIcon(false, getString(R.string.color_red));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                binding.tvMessage.setText(R.string.text_biometric_error_none_enrolled);
                setFingerPintIcon(false, getString(R.string.color_orange));
                break;
        }
    }

    private void setFingerPintIcon(boolean isEnabled, String color) {
        binding.ibLogin.setEnabled(isEnabled);
        binding.ibLogin.setColorFilter(Color.parseColor(color));
    }

    private void createDialogAuthentication() {
        Executor executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                navigateToActivity(AccessGranted.class, R.anim.left_in, R.anim.left_out);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                biometricPrompt.cancelAuthentication();
                navigateToActivity(AccessDenied.class, R.anim.right_in, R.anim.right_out);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v != null){
            switch (v.getId()){
                case R.id.ibLogin:
                    createPromptInfo();
                    biometricPrompt.authenticate(promptInfo);
                    break;
            }
        }
    }

    private void createPromptInfo() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.tittle_prompt_biometric))
                .setDescription(getString(R.string.description_prompt_biometric))
                .setNegativeButtonText(getString(R.string.negative_button_prompt_biometric))
                .build();
    }

    private void navigateToActivity(Class activity, int in, int out) {
        startActivity(new Intent(this, activity));
        overridePendingTransition(in, out);
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}