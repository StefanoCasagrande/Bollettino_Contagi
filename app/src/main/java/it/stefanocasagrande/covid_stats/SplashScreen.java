package it.stefanocasagrande.covid_stats;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import it.stefanocasagrande.covid_stats.Common.Common;
import it.stefanocasagrande.covid_stats.Common.DB;
import it.stefanocasagrande.covid_stats.Network.CheckNetwork;

public class SplashScreen extends AppCompatActivity {

    ProgressBar splashProgress;
    int SPLASH_TIME = 3000; //This is 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //This is additional feature, used to run a progress bar
        splashProgress = findViewById(R.id.splashProgress);
        playProgress();

        //Code to start timer and take action after the timer ends
        new Handler().postDelayed(() -> {
            //Do any action here. Now we are moving to next page
            Intent mySuperIntent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(mySuperIntent);

            //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
            finish();

        }, SPLASH_TIME);
    }

    //Method to run progress bar for 5 seconds
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();
    }
}