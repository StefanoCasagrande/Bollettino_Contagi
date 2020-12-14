package it.stefanocasagrande.covid_stats.json_classes.regions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data_Regions implements Serializable {

    @SerializedName("iso")
    @Expose
    public String iso;

    @SerializedName("name")
    @Expose
    public String name;

}
