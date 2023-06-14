package kr.hs.namyangju.report4byjson;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class TABLES {

    String TBLName1;
    String TBLName2;
    String TBLName3;

    public void setTable(String tbl1) {
        this.TBLName1 = tbl1;
    }

    public void setTable(String tbl1, String tbl2) {
        this.TBLName1 = tbl1;
        this.TBLName2 = tbl2;
    }

    public void setTable(String tbl1, String tbl2, String tbl3) {
        this.TBLName1 = tbl1;
        this.TBLName2 = tbl2;
        this.TBLName3 = tbl3;
    }

    public String getTBLName1() {
        return TBLName1;
    }

    public String getTBLName2() {
        return TBLName2;
    }

    public String getTBLName3() {
        return TBLName3;
    }
}

public class DB extends SQLiteOpenHelper {
    private Context context;
    TABLES tables = new TABLES();

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE IF NOT EXISTS " + "sawon" + "(id TEXT PRIMARY KEY, name TEXT, gender TEXT, salary INT, image TEXT);";
        db.execSQL(sql1);
    }

    public long insertData(@NonNull String mode, String d1, String d2, String d3, int d4, String d5) {

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues contentValues = null;
            contentValues = new ContentValues();
            contentValues.put("id", d1);
            contentValues.put("name", d2);
            contentValues.put("gender", d3);
            contentValues.put("salary", d4);
            contentValues.put("image", d5);
            long insertedRowId = database.insertWithOnConflict("sawon", null, contentValues, SQLiteDatabase.CONFLICT_NONE);
            database.setTransactionSuccessful();
            return insertedRowId;
        } catch (Exception e) {
            return -1L;
        } finally {
            database.endTransaction();
        }
    }

    public Cursor onSearchData(String tableName) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName + ";";

        Cursor cursor = database.rawQuery(sql, null);

        return cursor;
    }
    public Cursor onSearchDataDesc(String tableName) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName + " ORDER BY salary desc;";

        Cursor cursor = database.rawQuery(sql, null);

        return cursor;
    }

    public Cursor onSearchDataOrder(String tableName) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName + " ORDER BY salary;";

        Cursor cursor = database.rawQuery(sql, null);

        return cursor;
    }
    public Cursor onSearchDataGender(String tableName, String gender) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName + " WHERE gender = '"+gender+"';";

        Cursor cursor = database.rawQuery(sql, null);

        return cursor;
    }



    public void deleteData(int index) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            String sql = "DELETE FROM " + tables.getTBLName1() + " WHERE CODE = ?";
            String[] whereArgs = new String[]{"CD-010" + index};
            database.execSQL(sql, whereArgs);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

    }

    public void deleteAll() {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            String sql = "DELETE FROM " + tables.getTBLName1();
            database.execSQL(sql);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    @SuppressLint("Range")
    public void onUpdate(float price) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        SQLiteStatement statement = null;
        try {
            String sql = "INSERT OR REPLACE INTO " + tables.getTBLName1() + " (CODE, name, price) VALUES (?, ?, ?)";
            statement = database.compileStatement(sql);

            String selectSql = "SELECT CODE, name, price FROM " + tables.getTBLName1();
            Cursor cursor = database.rawQuery(selectSql, null);

            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor.getColumnIndex("CODE"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                statement.bindString(1, code);
                statement.bindString(2, name);
                statement.bindDouble(3, Double.parseDouble(String.valueOf(price)));
                statement.execute();
                statement.clearBindings();
            }

            cursor.close();
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            database.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS " + tables.getTBLName1() + ";";
        String sql2 = "DROP TABLE IF EXISTS " + tables.getTBLName2() + ";";
        db.execSQL(sql1);
        db.execSQL(sql2);
        onCreate(db);
    }
}
