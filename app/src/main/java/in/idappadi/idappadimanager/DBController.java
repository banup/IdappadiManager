package in.idappadi.idappadimanager;

/**
 * Created by banuparasuraman on 5/21/15.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DBController  extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
    }
    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE business ( businessid INTEGER PRIMARY KEY, businessname TEXT, businessaddress TEXT, businesscity TEXT, businessstate TEXT, businesscountry TEXT, businessphone TEXT,businesscontact TEXT,businesscatalog TEXT,surveyorphone TEXT,udpateStatus TEXT)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS business";
        database.execSQL(query);
        onCreate(database);
    }
    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertBusiness(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("businessname", queryValues.get("businessname"));
        values.put("businessaddress", queryValues.get("businessaddress"));
        values.put("businesscity", queryValues.get("businesscity"));
        values.put("businessstate", queryValues.get("businessstate"));
        values.put("businesscountry", queryValues.get("businesscountry"));
        values.put("businessphone", queryValues.get("businessphone"));
        values.put("businesscontact", queryValues.get("businesscontact"));
        values.put("businesscatalog", queryValues.get("businesscatalog"));
        values.put("surveyorphone", queryValues.get("surveyorphone"));
        values.put("udpateStatus", "no");
        database.insert("business", null, values);
        database.close();
    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM business";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("businessid", cursor.getString(0));
                map.put("businessname", cursor.getString(1));
                map.put("businessaddress", cursor.getString(2));
                map.put("businesscity", cursor.getString(3));
                map.put("businessstate", cursor.getString(4));
                map.put("businesscountry", cursor.getString(5));
                map.put("businessphone", cursor.getString(6));
                map.put("businesscontact", cursor.getString(7));
                map.put("businesscatalog", cursor.getString(8));
                map.put("surveyorphone", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }

    /**
     * Compose JSON out of SQLite records
     * @return
     */
    public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM business where udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("businessid", cursor.getString(0));
                map.put("businessname", cursor.getString(1));
                map.put("businessaddress", cursor.getString(2));
                map.put("businesscity", cursor.getString(3));
                map.put("businessstate", cursor.getString(4));
                map.put("businesscountry", cursor.getString(5));
                map.put("businessphone", cursor.getString(6));
                map.put("businesscontact", cursor.getString(7));
                map.put("businesscatalog", cursor.getString(8));
                map.put("surveyorphone", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    /**
     * Get Sync status of SQLite
     * @return
     */
    public String getSyncStatus(){
        String msg = null;
        if(this.dbSyncCount() == 0){
            msg = "SQLite and Remote MySQL DBs are in Sync!";
        }else{
            msg = "DB Sync needed\n";
        }
        return msg;
    }

    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT  * FROM business where udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    /**
     * Update Sync status against each User ID
     * @param id
     * @param status
     */
    public void updateSyncStatus(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update users set udpateStatus = '"+ status +"' where businessid="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
}
