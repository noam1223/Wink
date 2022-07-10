package com.noam.wink.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.noam.wink.R;
import com.noam.wink.activities.onboarding.OnBoardingStart;
import com.noam.wink.helper.ShortCutsClass;
import com.noam.wink.helper.singleton.OnBoardingSingletonProgress;
import com.noam.wink.helper.singleton.SingletonUser;

public class SplashScreen extends AppCompatActivity {

    LinearLayout welcomeLinearLayout;
    LinearLayout skipLinearLayout;
    Button whatToDoBtn;
    Animation animation;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mAuth = FirebaseAuth.getInstance();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        welcomeLinearLayout = findViewById(R.id.linear_layout_welcome_area_splash_screen_activity);
        whatToDoBtn = findViewById(R.id.what_todo_btn_splash_screen_activity);
        skipLinearLayout = findViewById(R.id.linear_layout_skip_area_splash_screen_activity);

//        Intent intent = new Intent(SplashScreen.this, OnBoardingStart.class);
//        OnBoardingSingletonProgress.getInstance().setOnProgress(true);
//        OnBoardingSingletonProgress.getInstance().setPositionScreen(0);
//        startActivity(intent);


        whatToDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, OnBoardingStart.class);
                OnBoardingSingletonProgress.getInstance().setOnProgress(true);
                OnBoardingSingletonProgress.getInstance().setPositionScreen(0);
                startActivity(intent);
            }
        });


        skipLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.scale);
        welcomeLinearLayout.setVisibility(View.GONE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) welcomeLinearLayout.getLayoutParams();

                for (int i = 0; i < 720; i++) {
                    params.topMargin = i / 10;
                    welcomeLinearLayout.setLayoutParams(params);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAuth.getCurrentUser() == null) {

                    mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                skipLinearLayout.setAlpha(0f);
                                skipLinearLayout.setVisibility(View.VISIBLE);
                                skipLinearLayout.animate().alpha(1).setDuration(750);

                                welcomeLinearLayout.setVisibility(View.VISIBLE);
                                welcomeLinearLayout.startAnimation(animation);


                            } else if (task.getException() != null) {
                                ShortCutsClass.toastMsg(SplashScreen.this, task.getException().getLocalizedMessage());
                                Log.i("FIREBASE ERROR", task.getException().getMessage());
                            }else {
                                ShortCutsClass.toastMsg(SplashScreen.this, "ניסיון ההתחברות נכשל אנא נסה להיכנס שוב");
                                finish();
                            }
                        }
                    });

                } else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }


}