package com.example.androidsqlitesetup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //helper variables
    public static final String DATABASE_NAME = "Mazes.db";
    //tables
    public static final String MAZES_TABLE = "Mazes";
    //column ids
    public static final String MAZES_TABLE_COLUMN_ID = "Id";
    //columns
    public static final String NUM_OF_ROWS_COL = "Number_of_Rows"; //num of rows
    public static final String NUM_OF_COLS_COL = "Number_of_Columns"; //num of columns
    public static final String START_X_COL = "Start_X"; //start x
    public static final String START_Y_COL = "Start_Y"; //start y
    public static final String GOAL_X_COL = "Goal_X"; //goal x
    public static final String GOAL_Y_COL = "Goal_Y"; //goal y
    public static final String WALLS_X_COL = "Walls_X"; //walls x
    public static final String WALLS_Y_COL = "Walls_Y"; //walls y
    public static final String SOLUTION_COL = "Possible_Solution"; //possible solution (this is either 0 or 1, which is true or false)

    public DBHelper(Context context){
        super(context, DATABASE_NAME , null, 1);
    }

    //May use the above constructor instead?
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version); //I think the database gets initialized here
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MAZES_TABLE + " (" + MAZES_TABLE_COLUMN_ID + " integer primary key autoincrement, " + NUM_OF_ROWS_COL + " integer, " + NUM_OF_COLS_COL + " integer, " + START_X_COL + " integer, " + START_Y_COL + " integer, " + GOAL_X_COL + " integer, " + GOAL_Y_COL + " integer, " + WALLS_X_COL + " blob, " + WALLS_Y_COL + " blob, " + SOLUTION_COL + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MAZES_TABLE);
        onCreate(db);
    }

    public boolean insertData(int numOfRows, int numOfCols, int start_x, int start_y, int goal_x, int goal_y, ArrayList<Integer> walls_x, ArrayList<Integer> walls_y, boolean solution_exists){
        SQLiteDatabase db = this.getWritableDatabase();

        //Create ContentValues
        ContentValues values = new ContentValues();
        values.put(NUM_OF_ROWS_COL, numOfRows);
        values.put(NUM_OF_COLS_COL, numOfCols);
        values.put(START_X_COL, start_x);
        values.put(START_Y_COL, start_y);
        values.put(GOAL_X_COL, goal_x);
        values.put(GOAL_Y_COL, goal_y);
        values.put(WALLS_X_COL, String.valueOf(walls_x)); //does String.valueOf() work as I hope it works?
        values.put(WALLS_Y_COL, String.valueOf(walls_y));
        Log.i("DBHelper", "solution_exists is: " + solution_exists);
        if (solution_exists) { //no problem with this conditional here
            Log.i("DBHelper", "Solution does exist :)");
            values.put(SOLUTION_COL, 1);
        } else {
            Log.i("DBHelper", "Solution does NOT exist");
            values.put(SOLUTION_COL, 0);
        }
        long result = db.insert(MAZES_TABLE, null, values); //insert() returns -1 if values are NOT inserted

        if(result == -1){
            return false;
        }
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MAZES_TABLE, null);
        return res;
    }

}
