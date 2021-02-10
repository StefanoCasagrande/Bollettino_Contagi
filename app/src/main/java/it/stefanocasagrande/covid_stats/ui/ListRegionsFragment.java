package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.stefanocasagrande.covid_stats.Adapters.Regions_Adapter;
import it.stefanocasagrande.covid_stats.Common.Common;
import it.stefanocasagrande.covid_stats.Covid_Interface;
import it.stefanocasagrande.covid_stats.MainActivity;
import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.regions.Data_Regions;
import it.stefanocasagrande.covid_stats.json_classes.reports.Province_Response;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;

import static it.stefanocasagrande.covid_stats.Common.Common.hideKeyboard;

public class ListRegionsFragment extends Fragment implements Covid_Interface {

    ListView list;
    EditText textFilter;
    private Regions_Adapter adapter;
    private List<Data_Regions> full_list;

    public ListRegionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cerca, container, false);

        list = v.findViewById(R.id.listView);
        list.setEmptyView(v.findViewById(R.id.empty));
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
                Load_Data(filter);
            }
        });

        full_list = Common.Database.get_Nations(getActivity());
        Load_Data("");

        list.requestFocus();
        hideKeyboard(getActivity());

        return v;
    }

    public void Load_Data(String filter)
    {
        List<Data_Regions> list_to_load = new ArrayList<>();

        if (filter==null || filter.equals(""))
            list_to_load = full_list;
        else
        {
            for(Data_Regions var : full_list)
            {
                if (filter!=null && !filter.equals(""))
                {
                    if (var.iso.toLowerCase().contains(filter.toLowerCase()) || var.name.toLowerCase().contains(filter.toLowerCase()))
                        list_to_load.add(var);
                }
            }
        }

        adapter = new Regions_Adapter(getActivity(), R.layout.single_item,list_to_load);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id)-> {

            Data_Regions nation_selected = adapter.getItemList(position);

            if (nation_selected.name.equals(getString(R.string.World)))
                ((MainActivity) getActivity()).goToWorldStats();
            else
                ((MainActivity) getActivity()).List_Provinces(nation_selected.iso);

        });
    }

    @Override
    public void newReportAvailable(Total_Response wResponse) {}

    @Override
    public void newProvinceReportAvailable(Province_Response wResponse) {}
}