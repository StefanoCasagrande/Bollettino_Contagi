package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.stefanocasagrande.covid_stats.Adapters.Provinces_Adapter;
import it.stefanocasagrande.covid_stats.Common.Common;
import it.stefanocasagrande.covid_stats.MainActivity;
import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.provinces.Data_Provinces;

public class ListBookmarkFragment extends Fragment {

    ListView list;
    EditText textFilter;
    private Provinces_Adapter adapter;
    private List<Data_Provinces> full_list;

    public ListBookmarkFragment() {
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
        View v = inflater.inflate(R.layout.fragment_cerca, container, false);

        list = v.findViewById(R.id.listView);
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

        full_list = Common.Database.Bookmark_Select(getActivity(), null, null, null);

        Load_Data("");

        return v;
    }

    public void Load_Data(String filter)
    {
        List<Data_Provinces> list_to_load = new ArrayList<>();

        if (filter==null || filter.equals(""))
            list_to_load = full_list;
        else
        {
            for(Data_Provinces var : full_list)
            {
                if (filter!=null && !filter.equals(""))
                {
                    if (var.iso.toLowerCase().contains(filter.toLowerCase()) || var.name.toLowerCase().contains(filter.toLowerCase()))
                        list_to_load.add(var);
                }
            }
        }

        adapter = new Provinces_Adapter(getActivity(), R.layout.single_item,list_to_load);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id)-> {

            String province = adapter.getItemList(position).province;

            if (province.equals(getString(R.string.General)))
                province=null;

            ((MainActivity) getActivity()).goToProvinceReport(adapter.getItemList(position).iso, province, adapter.getItemList(position).name);

        });
    }
}