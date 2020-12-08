package it.stefanocasagrande.covid_stats;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import it.stefanocasagrande.covid_stats.Network.API;
import it.stefanocasagrande.covid_stats.Network.NetworkClient;
import it.stefanocasagrande.covid_stats.json_classes.Total_Response;
import it.stefanocasagrande.covid_stats.ui.MainFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_gps)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.bringToFront();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //region Chiamate API

    public void getTotalReport(MainFragment var, String data_da_considerare) {

        //Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit= NetworkClient.getRetrofitClient();

        API covidAPIs = retrofit.create(API.class);

        Call call;

        if (data_da_considerare==null)
            call = covidAPIs.getActualTotal(getString(R.string.chiave));
        else
            call = covidAPIs.getTotalbyDate(data_da_considerare, getString(R.string.chiave));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
              /*This is the success callback. Though the response type is JSON, with Retrofit we get
              the response in the form of WResponse POJO class
              */
                if (response.body()!=null) {
                    Total_Response wResponse = (Total_Response) response.body();

                    if (var.isVisible())
                        var.newReportAvailable(wResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(),String.format("Errore API - %s", t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });

    }
}