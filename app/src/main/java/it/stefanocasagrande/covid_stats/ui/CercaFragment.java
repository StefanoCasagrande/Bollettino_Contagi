package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import it.stefanocasagrande.covid_stats.Covid_Interface;
import it.stefanocasagrande.covid_stats.MainActivity;
import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.regions.Regions;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CercaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CercaFragment extends Fragment implements Covid_Interface {

    ListView lista;

    public CercaFragment() {
        // Required empty public constructor
    }

    public static CercaFragment newInstance() {
        CercaFragment fragment = new CercaFragment();
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
        View v = inflater.inflate(R.layout.fragment_cerca, container, false);

        ((MainActivity) getActivity()).getNations(this);

        lista = v.findViewById(R.id.listView);

        return v;
    }

    @Override
    public void newReportAvailable(Total_Response wResponse) {

    }

    @Override
    public void newNationsAvailable(Regions wResponse) {



    }
}