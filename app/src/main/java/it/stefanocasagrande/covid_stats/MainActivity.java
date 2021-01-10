package it.stefanocasagrande.covid_stats;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import it.stefanocasagrande.covid_stats.Network.CheckNetwork;
import it.stefanocasagrande.covid_stats.Network.NetworkClient;
import it.stefanocasagrande.covid_stats.json_classes.provinces.Provinces;
import it.stefanocasagrande.covid_stats.json_classes.regions.Regions;
import it.stefanocasagrande.covid_stats.json_classes.reports.Province_Response;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;
import it.stefanocasagrande.covid_stats.ui.ListprovincesFragment;
import it.stefanocasagrande.covid_stats.ui.MainFragment;
import it.stefanocasagrande.covid_stats.ui.ProvinceReportFragment;
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
                R.id.nav_home, R.id.nav_search, R.id.nav_bookmark)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.bringToFront();

        CheckNetwork network = new CheckNetwork(getApplicationContext());
        network.registerNetworkCallback();

        Common.Database = new DB(this);

        if (getString(R.string.Key)==null || getString(R.string.Key).equals(""))
            Toast.makeText(this, getString(R.string.key_missing), Toast.LENGTH_LONG).show();
        else if (Common.Database.get_Nations(this).size()==0)
        {
            if (GlobalVariables.isNetworkConnected)
                getNations();
            else
                Toast.makeText(this,getString(R.string.Internet_Missing), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this,getString(R.string.To_Do), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.action_info))
                        .setMessage(String.format(getString(R.string.Info_About), "Johns Hopkins CSSE", "Axisbits", "RapiAPI.com", "stefano.casagrande@gmail.com"))
                        .setPositiveButton(getString(R.string.Ok), (dialog2, which) -> { }
                        )
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void List_Provinces(String iso)
    {
        if (Common.Database.get_Provinces(iso, this).size()>0)
            goToListprovincesFragment(iso);
        else {
            if (GlobalVariables.isNetworkConnected)
                getProvinces(iso);
            else
                Toast.makeText(this,getString(R.string.Internet_Missing), Toast.LENGTH_LONG).show();
        }
    }

    public void goToWorldStats()
    {
        Fragment fragment = MainFragment.newInstance(true);
        String tag=getString(R.string.MainFragment);
        Show_Fragment(fragment, tag);
    }

    public void goToProvinceReport(String iso, String province, String name)
    {
        Common.Database.Insert_History(iso, province, name, this);

        Fragment fragment = ProvinceReportFragment.newInstance(iso, null, province, name);
        String tag=getString(R.string.ProvinceReportFragment);
        Show_Fragment(fragment, tag);
    }

    public void goToListprovincesFragment(String iso_code)
    {
        Fragment fragment = ListprovincesFragment.newInstance(iso_code);
        String tag=getString(R.string.ListProvincesFragment);
        Show_Fragment(fragment, tag);
    }

    public void Show_Fragment(Fragment fragment, String tag)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    //region API Call

    public void getProvinces(String iso)
    {
        Retrofit retrofit= NetworkClient.getRetrofitClient();

        API covidAPIs = retrofit.create(API.class);

        Call call = covidAPIs.getProvinces(iso, getString(R.string.Key));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
              /*This is the success callback. Though the response type is JSON, with Retrofit we get
              the response in the form of WResponse POJO class
              */
                if (response.body()!=null) {
                    Provinces wResponse = (Provinces) response.body();
                    if (Common.Database.Insert_Provinces(wResponse.getData(), getApplicationContext()))
                        goToListprovincesFragment(iso);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(),String.format(getString(R.string.API_Error), t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getNations()
    {
        Retrofit retrofit= NetworkClient.getRetrofitClient();

        API covidAPIs = retrofit.create(API.class);

        Call call = covidAPIs.getregions(getString(R.string.Key));

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
                Toast.makeText(getApplicationContext(),String.format(getString(R.string.API_Error), t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getTotalReport(MainFragment var, Date selected_date) {

        //Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit= NetworkClient.getRetrofitClient();

        API covidAPIs = retrofit.create(API.class);

        Call call;

        if (selected_date==null)
            call = covidAPIs.getActualTotal(getString(R.string.Key));
        else
            call = covidAPIs.getTotalbyDate(Date_To_String_yyyy_MM_DD(selected_date), getString(R.string.Key));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
              /*This is the success callback. Though the response type is JSON, with Retrofit we get
              the response in the form of WResponse POJO class
              */
                if (response.body()!=null) {
                    Total_Response wResponse = (Total_Response) response.body();

                    if (wResponse.getData()==null)
                        Toast.makeText(getApplicationContext(),getString(R.string.No_Report_Available), Toast.LENGTH_LONG).show();
                    else if (var.isVisible())
                        var.newReportAvailable(wResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(),String.format(getString(R.string.API_Error), t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getReportByProvince(String iso, Date selected_date, String province, ProvinceReportFragment var)
    {
        //Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit= NetworkClient.getRetrofitClient();

        API covidAPIs = retrofit.create(API.class);

        Call call;

        if (selected_date == null)
            call = covidAPIs.getTotalByProvinces(iso, province, getString(R.string.Key));
        else
            call = covidAPIs.getTotalByProvinces_Date(Date_To_String_yyyy_MM_DD(selected_date), iso, province, getString(R.string.Key));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
              /*This is the success callback. Though the response type is JSON, with Retrofit we get
              the response in the form of WResponse POJO class
              */
                if (response.body()!=null) {
                    Province_Response wResponse = (Province_Response) response.body();

                    if (var.isVisible())
                        var.newProvinceReportAvailable(wResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(),String.format(getString(R.string.API_Error), t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }

    //endregion

    public void Update_Province_Report(String iso, String province, ProvinceReportFragment var)
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Refresh_Data))
                .setMessage(getString(R.string.Alert_Refresh_ChangeDay))
                .setPositiveButton(getString(R.string.Last_Avaible), (dialog2, which) -> {
                            if (GlobalVariables.isNetworkConnected)
                                getReportByProvince(iso, null, province, var);
                            else
                                Toast.makeText(this,getString(R.string.Internet_Missing), Toast.LENGTH_LONG).show();
                        }
                )
                .setNegativeButton(getString(R.string.Title_Date_Change), (dialog2, which) ->
                        Select_Date(var, iso, province)
                )
                .show();
    }

    public void Update_World_Report(MainFragment var)
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Refresh_Data))
                .setMessage(getString(R.string.Alert_Refresh_ChangeDay))
                .setPositiveButton(getString(R.string.Last_Avaible), (dialog2, which) -> {
                            if (GlobalVariables.isNetworkConnected)
                                getTotalReport(var, null);
                            else
                                Toast.makeText(this,getString(R.string.Internet_Missing), Toast.LENGTH_LONG).show();
                        }
                )
                .setNegativeButton(getString(R.string.Title_Date_Change), (dialog2, which) ->
                        Select_Date(var, null, null)
                )
                .show();
    }

    //region Dialog Scelta Data

    public void Select_Date(Fragment var, String iso, String province)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.Title_Date_Change));
        alert.setView(R.layout.edit_date_style);
        alert.setPositiveButton(getString(R.string.Confirm), (dialog, whichButton) -> {

            if (et_data.getText().toString().equals(""))
            {
                Toast.makeText(this, getString(R.string.No_Date), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date date = format.parse(et_data.getText().toString());

                if (!GlobalVariables.isNetworkConnected)
                    Toast.makeText(this, getString(R.string.Internet_Missing), Toast.LENGTH_LONG).show();
                else if (date.after(new Date()))
                    Toast.makeText(this, getString(R.string.Future_Date), Toast.LENGTH_LONG).show();
                else if (var.getTag()!=null && var.getTag().equals(getString(R.string.ProvinceReportFragment)))
                    getReportByProvince(iso, date, province, (ProvinceReportFragment) var);
                else
                    getTotalReport((MainFragment) var, date);
            }
            catch (ParseException ex)
            {
                Toast.makeText(this, getString(R.string.Invalid_Date), Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton(getString(R.string.Undo), null);
        AlertDialog dd = alert.show();

        et_data = dd.findViewById(R.id.et_data);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        et_data.setOnClickListener((View v)->
                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        );
    }

    Calendar myCalendar = Calendar.getInstance();
    TextView et_data;

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        et_data.setText(sdf.format(myCalendar.getTime()));
    }

    //endregion

}