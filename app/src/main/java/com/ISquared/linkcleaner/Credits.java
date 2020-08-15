package com.ISquared.linkcleaner;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Credits extends Activity {
    public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        counter = 0;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.credits);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        final TextView credits = findViewById(R.id.credits);
        final Button exit = findViewById(R.id.Exitbutton);
        credits.setTextColor(Color.YELLOW);
        credits.setVisibility(View.INVISIBLE);
        exit.setVisibility(View.INVISIBLE);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Animation translatebu = AnimationUtils.loadAnimation(this, R.anim.animationfile);
        translatebu.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (counter == 0)
                    credits.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                credits.setVisibility(View.INVISIBLE);
                credits.clearAnimation();
                if (counter == 0)
                    counter++;
                switch (counter) {

                    case 2:
                        credits.setText("\nLinkCleaner\n\n Was lovingly crafted by lucknell");
                        //credits.setVisibility(View.INVISIBLE);
                        //credits.clearAnimation();
                        credits.startAnimation(translatebu);
                        credits.setVisibility(View.VISIBLE);
                        counter++;
                        break;

                    case 4:
                        credits.setText("Shout out to Mozilla Firefox");
                        credits.setVisibility(View.INVISIBLE);
                        credits.clearAnimation();
                        credits.startAnimation(translatebu);
                        credits.setVisibility(View.VISIBLE);
                        counter++;
                        break;

                    case 6:
                        credits.setText("We will miss all of you\n\n\n Yes even you Eric...");
                        credits.setVisibility(View.INVISIBLE);
                        credits.clearAnimation();
                        credits.startAnimation(translatebu);
                        credits.setVisibility(View.VISIBLE);
                        counter++;
                        break;

                    case 7:
                        finish();
                        break;

                    default:
                        //Smooth the transitions
                        credits.setText("");
                        credits.startAnimation(translatebu);
                        credits.setVisibility(View.VISIBLE);
                        exit.setVisibility(View.VISIBLE);
                        counter++;
                        break;
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        credits.startAnimation(translatebu);
        credits.setText("Credits\n\n\n\nMentor\n\nDr.Osama Mohammed");
    }
}
