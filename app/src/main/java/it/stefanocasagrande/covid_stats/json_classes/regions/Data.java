package it.stefanocasagrande.covid_stats.json_classes.regions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("iso")
    @Expose
    private String iso;

    @SerializedName("name")
    @Expose
    private String name;

}
