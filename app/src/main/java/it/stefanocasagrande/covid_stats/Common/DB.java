package it.stefanocasagrande.covid_stats.Common;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import it.stefanocasagrande.covid_stats.json_classes.provinces.Data_Provinces;
import it.stefanocasagrande.covid_stats.json_classes.regions.Data_Regions;

public class DB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public DB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql_query="CREATE TABLE NATIONS ( ISO nvarchar(3), NAME nvarchar(150))";
        sqLiteDatabase.execSQL(sql_query);

        sql_query="CREATE TABLE PROVINCES ( ISO nvarchar(3), PROVINCE nvarchar(150))";
        sqLiteDatabase.execSQL(sql_query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //region Utility

    public boolean Insert_Multi(String header, List<String> list_insert)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder blocco = new StringBuilder();
        int i = 0;

        if (list_insert.size()==0)
            return true;

        for (String s: list_insert) {

            if (!s.equals(" ")) {

                if (i==0)
                {
                    blocco.append(header);
                }

                i = i + 1;
                blocco.append(String.format("%s, ", s));

                if (i == 500) {
                    i = 0;
                    db.execSQL(blocco.substring(0, blocco.length() - 2));
                    blocco.setLength(0);
                }
            }
        }

        if (blocco.length()!=0) {
            db.execSQL(blocco.substring(0, blocco.length() - 2));
            blocco.setLength(0);
        }

        return true;
    }

    public boolean Delete(String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName);

        return true;
    }

    public String Validate_String(String valore)
    {
        if (TextUtils.isEmpty(valore))
            return "NULL";
        else
            return DatabaseUtils.sqlEscapeString(valore);
    }

    //endregion

    //region Nations

    public List<Classes.Nation_Detail> get_Nations(String filter)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Classes.Nation_Detail> lista = new ArrayList<>();

        String sql_query="SELECT ISO, NAME from NATIONS ";

        sql_query += " order by NAME";

        Cursor c = db.rawQuery(sql_query, null);
        if (c.moveToFirst()){
            do {
                Classes.Nation_Detail var = new Classes.Nation_Detail();

                var.iso = c.getString(0);
                var.name = c.getString(1);

                if (filter!=null && !filter.equals(""))
                {
                    if (var.iso.toLowerCase().contains(filter.toLowerCase()) || var.name.toLowerCase().contains(filter.toLowerCase()))
                        lista.add(var);
                }
                else
                    lista.add(var);

            } while(c.moveToNext());
        }
        c.close();
        db.close();

        return lista;
    }

    public void Insert_Nations(List<Data_Regions> lista) {
        if (lista.size() == 0)
            return;

        if (Delete("NATIONS")) {

            List<String> sql_insert_values = new ArrayList<>();

            for (Data_Regions var : lista)
                sql_insert_values.add(String.format("(%s, %s)", Validate_String(var.iso), Validate_String(var.name)));

            Insert_Multi("INSERT INTO NATIONS ( ISO, NAME ) VALUES ", sql_insert_values);
        }
    }

    //endregion

    //region Provinces

    public List<Classes.Province_Detail> get_Provinces(String filter)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Classes.Province_Detail> lista = new ArrayList<>();

        String sql_query="SELECT ISO, NAME from NATIONS ";

        sql_query += " order by NAME";

        Cursor c = db.rawQuery(sql_query, null);
        if (c.moveToFirst()){
            do {
                Classes.Province_Detail var = new Classes.Province_Detail();

                var.iso = c.getString(0);
                var.name = c.getString(1);

                if (filter!=null && !filter.equals(""))
                {
                    if (var.iso.toLowerCase().contains(filter.toLowerCase()) || var.name.toLowerCase().contains(filter.toLowerCase()))
                        lista.add(var);
                }
                else
                    lista.add(var);

            } while(c.moveToNext());
        }
        c.close();
        db.close();

        return lista;
    }

    public void Insert_Provinces(List<Data_Provinces> lista) {
        if (lista.size() == 0)
            return;

        if (Delete("PROVINCES")) {

            List<String> sql_insert_values = new ArrayList<>();

            for (Data_Provinces var : lista)
            {
                if (var.province!=null && !var.province.equals(""))
                    var.province="general";

                sql_insert_values.add(String.format("(%s, %s)", Validate_String(var.iso), Validate_String(var.province)));
            }

            Insert_Multi("INSERT INTO PROVINCES ( ISO, PROVINCE ) VALUES ", sql_insert_values);
        }
    }

    //endregion
}
