package it.stefanocasagrande.covid_stats.json_classes.provinces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Provinces {

    @SerializedName("data")
    @Expose
    private List<Data_Provinces> data;

    public List<Data_Provinces> getData() {
        return data;
    }

}
