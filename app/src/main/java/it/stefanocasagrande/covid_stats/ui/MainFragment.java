package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import it.stefanocasagrande.covid_stats.Common.Common;
import it.stefanocasagrande.covid_stats.Covid_Interface;
import it.stefanocasagrande.covid_stats.MainActivity;
import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.provinces.Data_Provinces;
import it.stefanocasagrande.covid_stats.json_classes.reports.Province_Response;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;

import static it.stefanocasagrande.covid_stats.Common.Common.AddDotToInteger;
import static it.stefanocasagrande.covid_stats.Common.Common.hideKeyboard;

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
    static Boolean show_world_stats=false;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(Boolean p_show_world_stats) {
        MainFragment fragment = new MainFragment();

        show_world_stats = p_show_world_stats;

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.remove_bookmark).setVisible(false);
        menu.findItem(R.id.save_bookmark).setVisible(false);
        menu.findItem(R.id.save_home).setVisible(false);
        menu.findItem(R.id.remove_home).setVisible(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        Common.Back_Action = Common.Back_To_Main;

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
        btn_refresh.setTag("REFRESH");
        btn_refresh.setOnClickListener(this);

        LinearLayout ll_location = v.findViewById(R.id.ll_location);
        ll_location.setTag("HELP");
        ll_location.setOnClickListener(this);

        List<Data_Provinces> lista = Common.Database.Bookmark_Select(getActivity(),null, null, null, "HOME");

        if (lista.size()>0 && !show_world_stats)
        {
            String province;

            if (lista.get(0).province.equals(getString(R.string.General)))
                province=null;
            else
                province=lista.get(0).province;

            ((MainActivity) getActivity()).goToProvinceReport(lista.get(0).iso, province, lista.get(0).name);
        }
        else
        {
            show_world_stats=false;
            ((MainActivity) getActivity()).getTotalReport(this, null);
        }

        hideKeyboard(getActivity());

        return v;
    }

    @Override
    public void newReportAvailable(Total_Response wResponse) {

        tv_date.setText(wResponse.getData().getdate_dd_MM_yyyy());

        tv_confirmed_cases.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getData().getconfirmed())));
        tv_confirmed_diff.setText(String.format("%s +%s", getString(R.string.Title_Confirmed_Cases_Diff), AddDotToInteger(wResponse.getData().getconfirmed_diff())));

        tv_deaths.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getData().getdeaths())));
        tv_deaths_diff.setText(String.format("%s +%s", getString(R.string.Title_deaths_diff), AddDotToInteger(wResponse.getData().getdeaths_diff())));

        if (wResponse.getData().getrecovered()!=0)
        {
            tv_recovered.setText(String.format("%s %s", getString(R.string.Title_Total), AddDotToInteger(wResponse.getData().getrecovered())));
            tv_recovered_diff.setText(String.format("%s +%s", getString(R.string.Title_recovered_diff), AddDotToInteger(wResponse.getData().getrecovered_diff())));
        }
        else
        {
            tv_recovered.setText(String.format("%s %s", getString(R.string.Title_Total), getString(R.string.Not_Available)));
            tv_recovered_diff.setText(String.format("%s %s", getString(R.string.Title_recovered_diff), getString(R.string.Not_Available)));
        }

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

        if (view.getTag()!=null) {

            switch ((String) view.getTag()) {
                case "REFRESH":
                    ((MainActivity) getActivity()).Update_World_Report(this);
                    break;
                case "HELP":
                    ((MainActivity) getActivity()).Show_Help();
                    break;
            }
        }
    }
}