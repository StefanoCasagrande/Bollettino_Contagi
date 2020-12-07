package it.stefanocasagrande.covid_stats.json_classes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Total_Response {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

}
