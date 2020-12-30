package it.stefanocasagrande.covid_stats.json_classes.reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.stefanocasagrande.covid_stats.json_classes.provinces.Data_Provinces;

public class Data_Reports {

    @SerializedName("date")
    @Expose
    private String date;

    public String getdate_dd_MM_yyyy() {

        Date date = getdate();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);

    }

    public Date getdate() {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(date);
        }
        catch(ParseException ex){
            return new Date("1999-01-01");
        }
    }

    @SerializedName("last_update")
    @Expose
    private String last_update;

    public Date getlast_update() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return dateFormat.parse(last_update);
        }
        catch(ParseException ex){
            return new Date("1999-01-01");
        }
    }

    @SerializedName("confirmed")
    @Expose
    private Integer confirmed;

    public Integer getconfirmed() {
        return confirmed;
    }

    @SerializedName("confirmed_diff")
    @Expose
    private Integer confirmed_diff;

    public Integer getconfirmed_diff() {
        return confirmed_diff;
    }

    @SerializedName("deaths")
    @Expose
    private Integer deaths;

    public Integer getdeaths() {
        return deaths;
    }

    @SerializedName("deaths_diff")
    @Expose
    private Integer deaths_diff;

    public Integer getdeaths_diff() {
        return deaths_diff;
    }

    @SerializedName("recovered")
    @Expose
    private Integer recovered;

    public Integer getrecovered() {
        return recovered;
    }

    @SerializedName("recovered_diff")
    @Expose
    private Integer recovered_diff;

    public Integer getrecovered_diff() {
        return recovered_diff;
    }

    @SerializedName("active")
    @Expose
    private Integer active;

    public Integer getactive() {
        return active;
    }

    @SerializedName("active_diff")
    @Expose
    private Integer active_diff;

    public Integer getactive_diff() {
        return active_diff;
    }

    @SerializedName("fatality_rate")
    @Expose
    private double fatality_rate;

    public double getfatality_rate() {
        return fatality_rate;
    }

    @SerializedName("region")
    @Expose
    private Data_Provinces region;

    public Data_Provinces getregion() {
        return region;
    }

}
