package it.stefanocasagrande.covid_stats;

import it.stefanocasagrande.covid_stats.json_classes.Total_Response;

public interface Covid_Interface {

    void newReportAvailable(Total_Response wResponse);

}
