package it.stefanocasagrande.covid_stats.Common;

public class Common {

    public static DB Database;

    public static String AddDotToInteger(Integer value)
    {
        StringBuilder dotfiedNum= new StringBuilder();
        String inputNum = String.valueOf(Math.abs(value));

        int numLength = inputNum.length();
        for (int i=0; i<numLength; i++) {
            if ((numLength-i)%3 == 0 && i != 0) {
                dotfiedNum.append(".");
            }
            dotfiedNum.append(inputNum.charAt(i));
        }

        if (value<0)
            return String.format("-%s",dotfiedNum.toString());
        else
            return dotfiedNum.toString();
    }
}
