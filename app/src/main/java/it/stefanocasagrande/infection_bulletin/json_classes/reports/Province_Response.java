package it.stefanocasagrande.infection_bulletin.json_classes.reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Province_Response {

    @SerializedName("data")
    @Expose
    private List<Data_Reports> data;

    public List<Data_Reports> getDatas() {
        return data;
    }

}
