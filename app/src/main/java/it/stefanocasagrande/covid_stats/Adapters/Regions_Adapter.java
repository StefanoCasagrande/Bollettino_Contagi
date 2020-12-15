package it.stefanocasagrande.covid_stats.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import it.stefanocasagrande.covid_stats.R;
import it.stefanocasagrande.covid_stats.json_classes.regions.Data_Regions;

public class Regions_Adapter extends ArrayAdapter<Data_Regions> {

    private List<Data_Regions> Lista;
    private Context context;

    public Regions_Adapter(Context v_context, int resource, List<Data_Regions> objects) {
        super(v_context, resource, objects);

        context = v_context;
        Lista = objects;
    }

    private static class ViewHolder {

        TextView mainText;
        TextView secondText;
    }

    public Data_Regions getItemList(int position) {
        return Lista.get(position);
    }

    @Override
    public @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder mViewHolder;

        if (convertView == null) {

            mViewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (inflater!=null) {
                convertView = inflater.inflate(R.layout.single_item, parent, false);
                convertView.setTag(mViewHolder);
                mViewHolder.mainText = convertView.findViewById(R.id.tv_main_text);
                mViewHolder.secondText = convertView.findViewById(R.id.tv_subtext);
            }
        }
        else
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mainText.setText(Lista.get(position).name);
        mViewHolder.secondText.setText(String.format("ISO code %s", Lista.get(position).iso));

        return convertView;
    }
}
