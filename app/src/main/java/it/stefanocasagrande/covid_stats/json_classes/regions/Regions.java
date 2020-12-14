package it.stefanocasagrande.covid_stats.json_classes.regions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Regions {

    @SerializedName("data")
    @Expose
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }
}
