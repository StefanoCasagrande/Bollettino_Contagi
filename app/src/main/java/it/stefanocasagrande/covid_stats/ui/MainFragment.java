package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import it.stefanocasagrande.covid_stats.Covid_Interface;
import it.stefanocasagrande.covid_stats.MainActivity;
import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.reports.Province_Response;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;

import static it.stefanocasagrande.covid_stats.Common.Common.AddDotToInteger;

public class MainFragment extends Fragment implements Covid_Interface, View.OnClickListener {

    TextView tv_date;
    TextView tv_confirmed_cases;
    TextView tv_confirmed_diff;
    TextView tv_deaths;
    TextView tv_deaths_diff;
    TextView tv_recovered;
    TextView tv_recovered_diff;
    TextView tv_active;
    TextView tv_active_diff;
    TextView tv_fatality_rate;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        tv_date = v.findViewById(R.id.tv_date);
        tv_confirmed_cases = v.findViewById(R.id.tv_confirmed_cases);
        tv_confirmed_diff = v.findViewById(R.id.tv_confirmed_diff);
        tv_deaths = v.findViewById(R.id.tv_deaths);
        tv_deaths_diff = v.findViewById(R.id.tv_deaths_diff);
        tv_recovered = v.findViewById(R.id.tv_recovered);
        tv_recovered_diff = v.findViewById(R.id.tv_recovered_diff);
        tv_active = v.findViewById(R.id.tv_active);
        tv_active_diff = v.findViewById(R.id.tv_active_diff);
        tv_fatality_rate = v.findViewById(R.id.tv_fatality_rate);
        TextView tv_region = v.findViewById(R.id.tv_region);
        tv_region.setText(getString(R.string.World));

        Button btn_refresh = v.findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(this);

        ((MainActivity) getActivity()).getTotalReport(this, null);

        return v;
    }

    @Override
    public void newReportAvailable(Total_Response wResponse) {

        tv_date.setText(wResponse.getData().getdate_dd_MM_yyyy());

        tv_confirmed_cases.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getData().getconfirmed())));
        tv_confirmed_diff.setText(String.format("%s +%s", getString(R.string.Title_Confirmed_Cases_Diff), AddDotToInteger(wResponse.getData().getconfirmed_diff())));

        tv_deaths.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getData().getdeaths())));
        tv_deaths_diff.setText(String.format("%s +%s", getString(R.string.Title_deaths_diff), AddDotToInteger(wResponse.getData().getdeaths_diff())));

        tv_recovered.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getData().getrecovered())));
        tv_recovered_diff.setText(String.format("%s +%s", getString(R.string.Title_recovered_diff), AddDotToInteger(wResponse.getData().getrecovered_diff())));

        tv_active.setText(String.format("%s %s", getString(R.string.Title_active), AddDotToInteger(wResponse.getData().getactive())));

        int diff = wResponse.getData().getactive_diff();

        if (diff>0)
            tv_active_diff.setText(String.format("%s +%s", getString(R.string.Title_active_diff), AddDotToInteger(diff)));
        else
            tv_active_diff.setText(String.format("%s %s", getString(R.string.Title_active_diff), AddDotToInteger(diff)));

        DecimalFormat df = new DecimalFormat("#.##");
        tv_fatality_rate.setText(String.format("%s %s%%", getString(R.string.Title_fatality), df.format(wResponse.getData().getfatality_rate()*100)));
    }

    @Override
    public void newProvinceReportAvailable(Province_Response wResponse) {}

    @Override
    public void onClick(View view) {
        ((MainActivity) getActivity()).Update_World_Report(this);
    }
}