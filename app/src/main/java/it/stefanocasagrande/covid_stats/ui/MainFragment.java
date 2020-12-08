package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.stefanocasagrande.covid_stats.Covid_Interface;
import it.stefanocasagrande.covid_stats.MainActivity;
import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.Total_Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements Covid_Interface {

    TextView tv_giorno;
    TextView tv_casi_confermati;
    TextView tv_casi_confermati_aumento;
    TextView tv_morti;
    TextView tv_morti_aumento;
    TextView tv_ricoverati;
    TextView tv_ricoverati_aumento;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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

        tv_giorno = v.findViewById(R.id.tv_giorno);
        tv_casi_confermati = v.findViewById(R.id.tv_casi_confermati);
        tv_casi_confermati_aumento = v.findViewById(R.id.tv_casi_confermati_aumento);
        tv_morti = v.findViewById(R.id.tv_morti);
        tv_morti_aumento = v.findViewById(R.id.tv_morti_aumento);
        tv_ricoverati = v.findViewById(R.id.tv_ricoverati);
        tv_ricoverati_aumento = v.findViewById(R.id.tv_ricoverati_aumento);

        ((MainActivity) getActivity()).getTotalReport(this, null);

        return v;
    }

    @Override
    public void newReportAvailable(Total_Response wResponse) {

        tv_giorno.setText(wResponse.getData().getdate_dd_MM_yyyy());

        tv_casi_confermati.setText(String.valueOf(wResponse.getData().getconfirmed()));
        tv_casi_confermati_aumento.setText(String.valueOf(wResponse.getData().getconfirmed_diff()));

        tv_morti.setText(String.valueOf(wResponse.getData().getdeaths()));
        tv_morti_aumento.setText(String.valueOf(wResponse.getData().getdeaths_diff()));

        tv_ricoverati.setText(String.valueOf(wResponse.getData().getrecovered()));
        tv_ricoverati_aumento.setText(String.valueOf(wResponse.getData().getrecovered_diff()));
    }
}