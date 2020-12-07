package it.stefanocasagrande.covid_stats.Network;

import it.stefanocasagrande.covid_stats.json_classes.Total_Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface API {

    @GET("/reports/total")
    Call<Total_Response> getActualTotal(@Header("x-rapidapi-key")String chiave);

    @GET("/reports/total")
    Call<Total_Response> getTotalbyDate(@Query("date")String Data, @Header("x-rapidapi-key")String chiave);

}
