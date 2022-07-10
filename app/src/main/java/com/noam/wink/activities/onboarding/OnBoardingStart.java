package com.noam.wink.activities.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.noam.wink.R;
import com.noam.wink.activities.MainActivity;
import com.noam.wink.helper.singleton.OnBoardingSingletonProgress;

public class OnBoardingStart extends AppCompatActivity {

    Button letsStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_start);


        letsStartBtn = findViewById(R.id.lets_start_btn_on_boarding_start_activity);
        letsStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OnBoardingStart.this, MainActivity.class);
                OnBoardingSingletonProgress.getInstance().setOnProgress(true);
                OnBoardingSingletonProgress.getInstance().setPositionScreen(0);
                startActivity(intent);
                finish();

            }
        });

    }
}