package it.stefanocasagrande.covid_stats.json_classes.reports;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Total_Response {

    @SerializedName("data")
    @Expose
    private Data_Reports data;

    public Data_Reports getData() {
        return data;
    }

}
