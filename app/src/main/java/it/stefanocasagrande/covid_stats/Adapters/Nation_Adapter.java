package it.stefanocasagrande.covid_stats.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import it.stefanocasagrande.covid_stats.Common.Classes;

public class Nation_Adapter extends ArrayAdapter<Classes.Nation_Detail> {

    private List<Classes.Nation_Detail> Lista;
    private Context context;

    public Nation_Adapter(Context v_context, int resource, List<Classes.Nation_Detail> objects) {
        super(v_context, resource, objects);

        context = v_context;
        Lista = objects;
    }

}
