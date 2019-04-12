package com.aplicacion.essalud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.google.android.material.button.MaterialButton;

public class AnimationMainActivity extends AppCompatActivity {

    MaterialButton btnBlink;
    MaterialButton btnBounce;
    MaterialButton btnFadein;
    MaterialButton btnFadeout;
    MaterialButton btnLefttoright;
    MaterialButton btnMixed;
    MaterialButton btnRighttoleft;
    MaterialButton btnRotate;
    MaterialButton btnSampleAnim;
    MaterialButton btnZoomin;
    MaterialButton btnZoomout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_main);
        btnBlink = (MaterialButton) findViewById(R.id.btnBlink);
        btnBounce = (MaterialButton) findViewById(R.id.btnBounce);
        btnFadein = (MaterialButton) findViewById(R.id.btnFadein);
        btnFadeout = (MaterialButton) findViewById(R.id.btnFadeout);
        btnLefttoright = (MaterialButton) findViewById(R.id.btnLeftToRight);
        btnMixed = (MaterialButton) findViewById(R.id.btnMixedAnim);
        btnRighttoleft = (MaterialButton) findViewById(R.id.btnRightToLeft);
        btnRotate = (MaterialButton) findViewById(R.id.btnRotate);
        btnSampleAnim = (MaterialButton) findViewById(R.id.btnSampleAnim);
        btnZoomin = (MaterialButton) findViewById(R.id.btnZoomin);
        btnZoomout = (MaterialButton) findViewById(R.id.btnZoomout);
        btnBlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBlink.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.blink_anim));
            }
        });
        btnBounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBounce.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.bounce));
            }
        });
        btnFadein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFadein.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.fadein));
            }
        });
        btnFadeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFadeout.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.fadeout));
            }
        });
        btnLefttoright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLefttoright.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.lefttoright));
            }
        });
        btnMixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMixed.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.mixed_anim));
            }
        });
        btnRighttoleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRighttoleft.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.righttoleft));
            }
        });
        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRotate.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.rotate));
            }
        });
        btnSampleAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSampleAnim.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.sample_anim));
            }
        });
        btnZoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnZoomin.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.zoomin));
            }
        });
        btnZoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnZoomout.startAnimation(AnimationUtils.loadAnimation(AnimationMainActivity.this, R.anim.zoomout));
            }
        });

    }
}
