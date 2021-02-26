package it.stefanocasagrande.covid_stats.Common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Common {

    public static DB Database;
    public static String ISO_CODE;

    public static int Back_Action=0;

    public static final int Back_To_Main=0;
    public static final int Back_To_RegionList=1;
    public static final int Back_To_ProvinceList=2;
    public static final int Back_To_Bookmark=3;
    public static final int Back_To_History=4;


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

    public static String Date_ToJsonFormat(Date data_da_Convertire)
    {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(data_da_Convertire);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
