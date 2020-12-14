package it.stefanocasagrande.covid_stats;

import it.stefanocasagrande.covid_stats.json_classes.regions.Regions;
import it.stefanocasagrande.covid_stats.json_classes.reports.Total_Response;

public interface Covid_Interface {

    void newReportAvailable(Total_Response wResponse);

    void newNationsAvailable(Regions wResponse);

}
