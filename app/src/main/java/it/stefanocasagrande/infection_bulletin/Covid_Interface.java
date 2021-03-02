package it.stefanocasagrande.infection_bulletin;

import it.stefanocasagrande.infection_bulletin.json_classes.reports.Province_Response;
import it.stefanocasagrande.infection_bulletin.json_classes.reports.Total_Response;

public interface Covid_Interface {

    void newReportAvailable(Total_Response wResponse);

    void newProvinceReportAvailable(Province_Response wResponse);
}
