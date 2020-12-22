package com.cq.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity2 extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        button = findViewById(R.id.button);
        final Fragment_A fragmentA = new Fragment_A();
        final Fragment_B fragmentB = new Fragment_B();

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fa = supportFragmentManager.beginTransaction().add(R.id.fragment, fragmentA, "FA");
        fa.commitAllowingStateLoss();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fa.replace(R.id.fragment,fragmentA,"FA");
                fa.commitAllowingStateLoss();
            }
        });
    }
}
