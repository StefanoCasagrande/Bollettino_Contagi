package it.stefanocasagrande.infection_bulletin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import it.stefanocasagrande.infection_bulletin.R;
import it.stefanocasagrande.infection_bulletin.json_classes.provinces.Data_Provinces;
import it.stefanocasagrande.infection_bulletin.json_classes.regions.Data_Regions;

public class Provinces_Adapter extends ArrayAdapter<Data_Provinces> {

    private List<Data_Provinces> list;
    private Context context;

    public Provinces_Adapter(Context v_context, int resource, List<Data_Provinces> objects) {
        super(v_context, resource, objects);

        context = v_context;
        list = objects;
    }

    private static class ViewHolder {

        TextView mainText;
        TextView secondText;
    }

    public Data_Provinces getItemList(int position) {
        return list.get(position);
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

        if (list.get(position).province.equals(context.getString(R.string.General)))
        {
            mViewHolder.mainText.setText(list.get(position).name);
            mViewHolder.secondText.setText(context.getString(R.string.Nation_Situation));
        }
        else {
            mViewHolder.mainText.setText(list.get(position).province);
            mViewHolder.secondText.setText(list.get(position).name);
        }

        return convertView;
    }

}
