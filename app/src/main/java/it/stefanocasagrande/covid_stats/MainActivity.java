package it.stefanocasagrande.covid_stats;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.stefanocasagrande.covid_stats.Common.Common;
import it.stefanocasagrande.covid_stats.Common.DB;
import it.stefanocasagrande.covid_stats.Network.API;
import it.stefanocasagrande.covid_stats.Network.NetworkClient;
import it.stefanocasagrande.covid_stats.json_classes.provinces.Provinces;
import it.stefanocasagrande.covid_stats.json_classes.regions.Regions;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;
import it.stefanocasagrande.covid_stats.ui.MainFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static it.stefanocasagrande.covid_stats.Common.Methods.Date_To_String_yyyy_MM_DD;

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

        Common.Database = new DB(this);
        getNations();

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

    public void getProvinces(String iso)
    {
        Retrofit retrofit= NetworkClient.getRetrofitClient();

        API covidAPIs = retrofit.create(API.class);

        Call call = covidAPIs.getProvinces(iso, getString(R.string.chiave));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
              /*This is the success callback. Though the response type is JSON, with Retrofit we get
              the response in the form of WResponse POJO class
              */
                if (response.body()!=null) {
                    Provinces wResponse = (Provinces) response.body();
                    Common.Database.Insert_Provinces(wResponse.getData());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(),String.format("Errore API - %s", t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getNations()
    {
        Retrofit retrofit= NetworkClient.getRetrofitClient();

        API covidAPIs = retrofit.create(API.class);

        Call call = covidAPIs.getregions(getString(R.string.chiave));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
              /*This is the success callback. Though the response type is JSON, with Retrofit we get
              the response in the form of WResponse POJO class
              */
                if (response.body()!=null) {
                    Regions wResponse = (Regions) response.body();
                    Common.Database.Insert_Nations(wResponse.getData());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(),String.format("Errore API - %s", t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getTotalReport(MainFragment var, Date data_da_considerare) {

        //Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit= NetworkClient.getRetrofitClient();

        API covidAPIs = retrofit.create(API.class);

        Call call;

        if (data_da_considerare==null)
            call = covidAPIs.getActualTotal(getString(R.string.chiave));
        else
            call = covidAPIs.getTotalbyDate(Date_To_String_yyyy_MM_DD(data_da_considerare), getString(R.string.chiave));

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

    //endregion

    public void Aggiorna_Report_Totali(MainFragment var)
    {
        new AlertDialog.Builder(this)
                .setTitle("Data situazione")
                .setMessage("Vuoi visualizzare ultimo report disponibile o selezionare data?")
                .setPositiveButton("Ultimo disponibile", (dialog2, which) ->
                        getTotalReport(var, null)
                )
                .setNegativeButton("Selezionare data", (dialog2, which) ->
                        Setta_Data(var)
                )
                .show();
    }

    //region Dialog Scelta Data

    public void Setta_Data(MainFragment var)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Inserisci data");
        alert.setView(R.layout.edit_date_style);
        alert.setPositiveButton("Conferma", (dialog, whichButton) -> {

            if (et_data_consegna.getText().toString().equals(""))
            {
                Toast.makeText(this, "Nessuna data indicata", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date date = format.parse(et_data_consegna.getText().toString());

                getTotalReport(var, date);
            }
            catch (ParseException ex)
            {
                Toast.makeText(this, "Data non valida", Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("Annulla", null);
        AlertDialog dd = alert.show();

        et_data_consegna = dd.findViewById(R.id.et_data_consegna);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        et_data_consegna.setOnClickListener((View v)->
                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        );
    }

    Calendar myCalendar = Calendar.getInstance();
    TextView et_data_consegna;

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        et_data_consegna.setText(sdf.format(myCalendar.getTime()));
    }

    //endregion

}