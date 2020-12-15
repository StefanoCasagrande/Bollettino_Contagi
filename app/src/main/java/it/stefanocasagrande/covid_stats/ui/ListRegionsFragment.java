package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import it.stefanocasagrande.covid_stats.Adapters.Regions_Adapter;
import it.stefanocasagrande.covid_stats.Common.Common;
import it.stefanocasagrande.covid_stats.Covid_Interface;
import it.stefanocasagrande.covid_stats.MainActivity;
import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.regions.Data_Regions;
import it.stefanocasagrande.covid_stats.json_classes.regions.Regions;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;

public class ListRegionsFragment extends Fragment implements Covid_Interface {

    ListView ls_stati;
    EditText textFilter;
    private Regions_Adapter adapter;

    public ListRegionsFragment() {
        // Required empty public constructor
    }

    public static ListRegionsFragment newInstance() {
        ListRegionsFragment fragment = new ListRegionsFragment();
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

        ls_stati = v.findViewById(R.id.listView);
        textFilter = v.findViewById(R.id.cercaEditText);

        textFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String filter = s.toString();
                Carica_Dati(filter);
            }
        });

        Carica_Dati("");

        return v;
    }

    public void Carica_Dati(String filter)
    {
        List<Data_Regions> lista = Common.Database.get_Nations(filter);
        adapter = new Regions_Adapter(getActivity(), R.layout.single_item,lista);
        ls_stati.setAdapter(adapter);
        ls_stati.setOnItemClickListener((parent, view, position, id)-> {

            Data_Regions nazione_selezionata = adapter.getItemList(position);
            ((MainActivity) getActivity()).getProvinces(nazione_selezionata.iso);

        });
    }

    @Override
    public void newReportAvailable(Total_Response wResponse) {

    }

    @Override
    public void newNationsAvailable(Regions wResponse) {



    }
}