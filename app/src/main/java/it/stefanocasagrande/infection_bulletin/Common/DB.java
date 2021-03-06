package it.stefanocasagrande.infection_bulletin.Common;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.stefanocasagrande.infection_bulletin.R;
import it.stefanocasagrande.infection_bulletin.json_classes.provinces.Data_Provinces;
import it.stefanocasagrande.infection_bulletin.json_classes.regions.Data_Regions;

import static it.stefanocasagrande.infection_bulletin.Common.Common.Date_ToJsonFormat;

public class DB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public DB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql_query="CREATE TABLE NATIONS (id INTEGER PRIMARY KEY, ISO nvarchar(3), NAME nvarchar(150))";
        sqLiteDatabase.execSQL(sql_query);

        sql_query="CREATE TABLE PROVINCES (id INTEGER PRIMARY KEY, ISO nvarchar(3), PROVINCE nvarchar(150), NAME nvarchar(150))";
        sqLiteDatabase.execSQL(sql_query);

        sql_query="CREATE TABLE BOOKMARKS (id INTEGER PRIMARY KEY, ISO nvarchar(3), PROVINCE nvarchar(150), NAME nvarchar(150), TYPE nvarchar(150))";
        sqLiteDatabase.execSQL(sql_query);

        sql_query="CREATE TABLE CONFIGURATION (id INTEGER PRIMARY KEY, NAME nvarchar(150), VALUE nvarchar(150))";
        sqLiteDatabase.execSQL(sql_query);

        sql_query="CREATE TABLE HISTORY (ISO nvarchar(3), PROVINCE nvarchar(150), NAME nvarchar(150), LAST_REQUEST nvarchar(14), PRIMARY KEY(ISO, PROVINCE, NAME))";
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

    public boolean Delete(String tableName, String filter)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(String.format("DELETE FROM %s %s", tableName, filter));

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

    public List<Data_Regions> get_Nations(Context ctx)
    {
        List<Data_Regions> lista = new ArrayList<>();

        String sql_query="select ISO, NAME from ( ";

        if (Bookmark_Select(ctx, null, null, null, "HOME").size()>0)
            sql_query+=String.format(" SELECT '---' as ISO, %s as NAME UNION ", Validate_String(ctx.getString(R.string.World)));

        sql_query+="SELECT ISO, NAME from NATIONS ) a ";

        sql_query += String.format(" order by CASE WHEN NAME=%s then 0 else 1 END, NAME", Validate_String(ctx.getString(R.string.World)));

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(sql_query, null);
        if (c.moveToFirst()){
            do {
                Data_Regions var = new Data_Regions();

                var.iso = c.getString(0);
                var.name = c.getString(1);

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

        if (Delete("NATIONS", "")) {

            List<String> sql_insert_values = new ArrayList<>();

            for (Data_Regions var : lista)
                sql_insert_values.add(String.format("(%s, %s)", Validate_String(var.iso), Validate_String(var.name)));

            Insert_Multi("INSERT INTO NATIONS ( ISO, NAME ) VALUES ", sql_insert_values);
        }
    }

    //endregion

    //region Provinces

    public List<Data_Provinces> get_Provinces(String iso, Context cxt)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Data_Provinces> lista = new ArrayList<>();

        String sql_query="SELECT ISO, PROVINCE, NAME from ( select ISO, PROVINCE, NAME from PROVINCES group by ISO, PROVINCE, NAME) PROVINCES ";
        sql_query+= " WHERE ISO= " + Validate_String(iso);
        sql_query += String.format(" order by CASE WHEN PROVINCE=%s then 0 else 1 END, PROVINCE ", Validate_String(cxt.getString(R.string.General)));

        Cursor c = db.rawQuery(sql_query, null);
        if (c.moveToFirst()){
            do {
                Data_Provinces var = new Data_Provinces();

                var.iso = c.getString(0);
                var.province = c.getString(1);
                var.name = c.getString(2);
                lista.add(var);

            } while(c.moveToNext());
        }
        c.close();
        db.close();

        return lista;
    }

    public boolean Insert_Provinces(List<Data_Provinces> lista, Context ctx) {
        if (lista.size() == 0)
            return false;

        if (Delete("PROVINCES", " WHERE ISO= " + Validate_String(lista.get(0).iso))) {

            List<String> sql_insert_values = new ArrayList<>();
            boolean record_for_the_nation=false;

            for (Data_Provinces var : lista)
            {
                if (var.province==null || var.province.equals("")) {
                    var.province = ctx.getString(R.string.General);
                    record_for_the_nation=true;
                }

                sql_insert_values.add(String.format("(%s, %s, %s)", Validate_String(var.iso), Validate_String(var.province), Validate_String(var.name)));
            }

            if (!record_for_the_nation)
                sql_insert_values.add(String.format("(%s, %s, %s)", Validate_String(lista.get(0).iso), Validate_String(ctx.getString(R.string.General)), Validate_String(lista.get(0).name)));

            return Insert_Multi("INSERT INTO PROVINCES ( ISO, PROVINCE, NAME ) VALUES ", sql_insert_values);
        }
        else
            return false;
    }

    //endregion

    //region Bookmarks

    public List<Data_Provinces> Bookmark_Select(Context ctx, String iso, String province, String name, String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Data_Provinces> lista = new ArrayList<>();

        String sql_query=String.format("SELECT ISO, PROVINCE, NAME from BOOKMARKS where TYPE=%s", Validate_String(type));

        if (iso!=null)
        {
            sql_query += String.format(" AND ISO=%s and IFNULL(PROVINCE,'')=IFNULL(%s,'') and NAME=%s", Validate_String(iso), Validate_String(province), Validate_String(name));
        }

        Cursor c = db.rawQuery(sql_query, null);
        if (c.moveToFirst()){
            do {
                Data_Provinces var = new Data_Provinces();

                var.iso = c.getString(0);
                var.province = c.getString(1);
                var.name = c.getString(2);

                if (var.province==null || var.province.equals(""))
                    var.province = ctx.getString(R.string.General);

                lista.add(var);

            } while(c.moveToNext());
        }
        c.close();
        db.close();

        return lista;
    }

    public boolean Bookmark_Remove(String iso, String province, String name, String type)
    {
        return Delete("BOOKMARKS", String.format("WHERE ISO=%s and IFNULL(PROVINCE,'')=IFNULL(%s,'') and NAME=%s and TYPE=%s", Validate_String(iso), Validate_String(province), Validate_String(name), Validate_String(type)));
    }

    public boolean Bookmark_Save(String iso, String province, String name, String type)
    {
        if (type.toLowerCase().equals("home"))
            Delete("BOOKMARKS", "WHERE TYPE='HOME'");

        String sql=String.format("INSERT INTO BOOKMARKS ( ISO, PROVINCE, NAME, TYPE ) VALUES (%s, %s, %s, %s)", Validate_String(iso), Validate_String(province), Validate_String(name), Validate_String(type));

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        return true;
    }

    //endregion

    //region Configuration

    public boolean Set_Configurazione(String name, String value)
    {
        if (Delete("CONFIGURATION", "where NAME=" + Validate_String(name))) {

            String sql = String.format("INSERT INTO CONFIGURATION ( NAME, VALUE ) VALUES (%s, %s)  ", Validate_String(name), Validate_String(value));

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(sql);

            return true;
        }
        else
            return false;
    }

    public String Get_Configurazione(String name)
    {
        String value="";

        String sql_query="SELECT VALUE from CONFIGURATION where NAME=" + Validate_String(name);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(sql_query, null);
        if (c.moveToFirst()){
            do {
                value = c.getString(0);

            } while(c.moveToNext());
        }
        c.close();
        db.close();

        return value;
    }

    //endregion

    //region History

    public boolean Insert_History(String iso, String province, String name, Context ctx)
    {
        // In History we can have no more than 10 items.
        // So if the items already exists we just update the last request field.
        // if it doesn't exist we have to add it and remove the one
        // wich is far away.

        String date_time_string = Date_ToJsonFormat(new Date());

        if (province==null)
            province=ctx.getString(R.string.General);

        String sql=String.format("INSERT OR REPLACE INTO HISTORY ( ISO, PROVINCE, NAME, LAST_REQUEST ) VALUES (%s, %s, %s, %s)", Validate_String(iso), Validate_String(province) , Validate_String(name), Validate_String(date_time_string));

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);

        sql="DELETE FROM HISTORY WHERE LAST_REQUEST NOT IN ( SELECT LAST_REQUEST FROM HISTORY ORDER BY LAST_REQUEST DESC LIMIT 10 ) ";
        db.execSQL(sql);

        return true;
    }

    public List<Data_Provinces> get_History()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Data_Provinces> lista = new ArrayList<>();

        String sql_query="SELECT ISO, PROVINCE, NAME from HISTORY order by LAST_REQUEST desc ";

        Cursor c = db.rawQuery(sql_query, null);
        if (c.moveToFirst()){
            do {
                Data_Provinces var = new Data_Provinces();

                var.iso = c.getString(0);
                var.province = c.getString(1);
                var.name = c.getString(2);
                lista.add(var);

            } while(c.moveToNext());
        }
        c.close();
        db.close();

        return lista;
    }

    //endregion

}
