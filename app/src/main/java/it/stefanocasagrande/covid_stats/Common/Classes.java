package it.stefanocasagrande.covid_stats.Common;

import java.io.Serializable;

public class Classes {

    public static class Nation_Detail implements Serializable
    {
        public String iso;
        public String name;
    }

    public static class Province_Detail implements Serializable
    {
        public String iso;
        public String name;
        public String province;
    }
}
