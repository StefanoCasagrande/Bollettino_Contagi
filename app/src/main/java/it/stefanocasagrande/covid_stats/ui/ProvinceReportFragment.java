package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Date;

import it.stefanocasagrande.covid_stats.Covid_Interface;
import it.stefanocasagrande.covid_stats.MainActivity;
import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.reports.Province_Response;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;

public class ProvinceReportFragment extends Fragment implements Covid_Interface, View.OnClickListener{

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
    TextView tv_region;

    static String iso;
    static Date selected_date;
    static String province;

    public ProvinceReportFragment() {
        // Required empty public constructor
    }

    public static ProvinceReportFragment newInstance(String p_iso, Date p_date, String p_province) {
        ProvinceReportFragment fragment = new ProvinceReportFragment();

        iso = p_iso;
        selected_date = p_date;
        province = p_province;

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        tv_region = v.findViewById(R.id.tv_region);
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

        ((MainActivity) getActivity()).getReportByProvince(iso, selected_date, province, this);

        Button btn_refresh = v.findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(this);

        return v;
    }

    @Override
    public void newProvinceReportAvailable(Province_Response wResponse) {

        if (wResponse.getDatas().size()==1)
        {
            tv_date.setText(wResponse.getDatas().get(0).getdate_dd_MM_yyyy());

            tv_confirmed_cases.setText(String.valueOf(wResponse.getDatas().get(0).getconfirmed()));
            tv_confirmed_diff.setText(String.valueOf(wResponse.getDatas().get(0).getconfirmed_diff()));

            tv_deaths.setText(String.valueOf(wResponse.getDatas().get(0).getdeaths()));
            tv_deaths_diff.setText(String.valueOf(wResponse.getDatas().get(0).getdeaths_diff()));

            tv_recovered.setText(String.valueOf(wResponse.getDatas().get(0).getrecovered()));
            tv_recovered_diff.setText(String.valueOf(wResponse.getDatas().get(0).getrecovered_diff()));

            tv_active.setText(String.valueOf(wResponse.getDatas().get(0).getactive()));
            tv_active_diff.setText(String.valueOf(wResponse.getDatas().get(0).getactive_diff()));

            tv_fatality_rate.setText(String.valueOf(wResponse.getDatas().get(0).getfatality_rate()));

            tv_region.setText(String.format("%s (%s)", wResponse.getDatas().get(0).getregion().province, wResponse.getDatas().get(0).getregion().name));
        }

    }

    @Override
    public void newReportAvailable(Total_Response wResponse) {}

    @Override
    public void onClick(View view) {
        ((MainActivity) getActivity()).Update_Province_Report(iso, province, this);
    }
}
