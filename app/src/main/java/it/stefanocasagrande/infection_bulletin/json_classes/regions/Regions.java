package it.stefanocasagrande.infection_bulletin.json_classes.regions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Regions {

    @SerializedName("data")
    @Expose
    private List<Data_Regions> data;

    public List<Data_Regions> getData() {
        return data;
    }
}
