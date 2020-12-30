package it.stefanocasagrande.covid_stats.Network;

import it.stefanocasagrande.covid_stats.json_classes.provinces.Provinces;
import it.stefanocasagrande.covid_stats.json_classes.regions.Regions;
import it.stefanocasagrande.covid_stats.json_classes.reports.Province_Response;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface API {

    @GET("/provinces")
    Call<Provinces> getProvinces(@Query("iso")String iso_code, @Header("x-rapidapi-key")String key);

    @GET("/regions")
    Call<Regions> getregions(@Header("x-rapidapi-key")String key);

    @GET("/reports/total")
    Call<Total_Response> getActualTotal(@Header("x-rapidapi-key")String key);

    @GET("/reports/total")
    Call<Total_Response> getTotalbyDate(@Query("date")String Data, @Header("x-rapidapi-key")String key);

    @GET("/reports")
    Call<Province_Response> getTotalByProvinces_Date(@Query("date")String Data, @Query("iso")String iso_code, @Query("region_province")String region_province, @Header("x-rapidapi-key")String key);

    @GET("/reports")
    Call<Province_Response> getTotalByProvinces(@Query("iso")String iso_code, @Query("region_province")String region_province, @Header("x-rapidapi-key")String key);

    @GET("/reports")
    Call<Province_Response> getTotalByRegions(@Query("iso")String iso_code, @Header("x-rapidapi-key")String key);

}
