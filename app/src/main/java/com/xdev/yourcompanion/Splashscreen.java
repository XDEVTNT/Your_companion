package com.xdev.yourcompanion;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splashscreen extends AppCompatActivity {

    ActivityManager manager;
    int SPLASH_DISPLAY_LENGTH = 2000;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        ProgressBar pb = findViewById(R.id.progressbara);
        pb.setVisibility(View.VISIBLE);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Splashscreen.this);
                            builder.setTitle("we need ");
                            builder.setMessage("this permmsios");
                            builder.setPositiveButton("GOTSETTINGS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, 101);
                                }
                            });
                            builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();

                        }


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }


                }).check();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofInt(0, pb.getMax());

                animator.setDuration(SPLASH_DISPLAY_LENGTH);
                animator.addUpdateListener(animation -> pb.setProgress((Integer) animation.getAnimatedValue()));
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        new Handler().postDelayed(() -> {
                                },
                                SPLASH_DISPLAY_LENGTH);
                        mAuth = FirebaseAuth.getInstance();
                        if (mAuth.getCurrentUser() != null) {
                            Toast.makeText(Splashscreen.this, mAuth.getCurrentUser().getUid() + ",welcome", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Splashscreen.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Splashscreen.this, "Pleas login to continue", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Splashscreen.this, LoginActivity.class));
                            finish();
                        }


                    }
                });
                animator.start();


            }

        }, SPLASH_DISPLAY_LENGTH);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "coodConnectedTOinternet";
            color = Color.WHITE;
        } else {
            message = "pasdinternet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}