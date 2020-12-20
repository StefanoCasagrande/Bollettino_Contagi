package it.stefanocasagrande.covid_stats.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.stefanocasagrande.covid_stats.R;

public class ListprovincesFragment extends Fragment {


    public ListprovincesFragment() {
        // Required empty public constructor
    }

    public static ListprovincesFragment newInstance() {
        ListprovincesFragment fragment = new ListprovincesFragment();
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
        View v = inflater.inflate(R.layout.fragment_listprovinces, container, false);

        return v;
    }
}