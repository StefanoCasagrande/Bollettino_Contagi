package it.stefanocasagrande.covid_stats.json_classes.provinces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data_Provinces implements Serializable {

    @SerializedName("iso")
    @Expose
    public String iso;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("province")
    @Expose
    public String province;

    @SerializedName("lat")
    @Expose
    public String lat;


}
