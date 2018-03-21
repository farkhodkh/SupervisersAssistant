package ru.haknazarovfarkhod.supervisersAssistant.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import ru.haknazarovfarkhod.supervisersAssistant.MainActivity;

public class DatabaseHelperTradeOutlets extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "supervisorsAssistant.db";
    //private static final int SCHEMA = 2;
    private Context myContext;

    public static final String TABLE_TRADE_OUTLETS = "trade_outlets";
    public static final String TABLE_TRADE_OUTLETS_COLUMN_ID = "_id";
    public static final String TABLE_TRADE_OUTLETS_COLUMN_NAME = "name";
    public static final String TABLE_TRADE_OUTLETS_COLUMN_PHONE = "phone";
    public static final String TABLE_TRADE_OUTLETS_COLUMN_ADDRESS = "address";

    public DatabaseHelperTradeOutlets(Context context) {
        super(context, DB_NAME, null, MainActivity.SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TRADE_OUTLETS + " ("
                + TABLE_TRADE_OUTLETS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TABLE_TRADE_OUTLETS_COLUMN_NAME + " TEXT, "
                + TABLE_TRADE_OUTLETS_COLUMN_PHONE + " TEXT, "
                + TABLE_TRADE_OUTLETS_COLUMN_ADDRESS + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveTradeOutlet(SQLiteDatabase db, String tradeOutletTitle, String tradeOutletAddress, String tradeOutletPhone) {
        try {
            StringBuilder insertText = new StringBuilder();
            insertText.append("INSERT INTO ")
                    .append(TABLE_TRADE_OUTLETS)
                    .append("(")
                    .append(TABLE_TRADE_OUTLETS_COLUMN_NAME)
                    .append(",")
                    .append(TABLE_TRADE_OUTLETS_COLUMN_PHONE)
                    .append(",")
                    .append(TABLE_TRADE_OUTLETS_COLUMN_ADDRESS)
                    .append(") VALUES (")
                    .append("'")
                    .append(tradeOutletTitle)
                    .append("'")
                    .append(",")
                    .append("'")
                    .append(tradeOutletAddress)
                    .append("'")
                    .append(",")
                    .append("'")
                    .append(tradeOutletPhone)
                    .append("'")
                    .append(")");
            db.execSQL(insertText.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap readTradeOutletById(SQLiteDatabase db, long id) {
        HashMap tradeOutletParams = new HashMap<>();

        try {
            Cursor cursorTradeOutlet = db.rawQuery("SELECT " + TABLE_TRADE_OUTLETS_COLUMN_NAME + ", " + TABLE_TRADE_OUTLETS_COLUMN_PHONE + "," + TABLE_TRADE_OUTLETS_COLUMN_ADDRESS + " FROM " + TABLE_TRADE_OUTLETS + " WHERE " + TABLE_TRADE_OUTLETS_COLUMN_ID + " = " + id, null);
            if (cursorTradeOutlet.getCount() != 0) {
                cursorTradeOutlet.moveToFirst();
                tradeOutletParams.put("title", cursorTradeOutlet.getString(0));
                tradeOutletParams.put("phone", cursorTradeOutlet.getString(1));
                tradeOutletParams.put("address", cursorTradeOutlet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tradeOutletParams;
    }

    public void deleteTradeOutletById(SQLiteDatabase db, int id) {
        try {
            db.execSQL("DELETE FROM " + TABLE_TRADE_OUTLETS + " WHERE " + TABLE_TRADE_OUTLETS_COLUMN_ID + " = " + id);
            //db.rawQuery("DELETE FROM " + TABLE_TRADE_OUTLETS + " WHERE " + TABLE_TRADE_OUTLETS_COLUMN_ID + " = " + id, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void overWriteTradeOutletById(SQLiteDatabase db, long currentItemId, String tradeOutletTitle, String tradeOutletAddress, String tradeOutletPhone) {
        try {
            StringBuilder insertText = new StringBuilder();
            insertText.append("UPDATE ")
                    .append(TABLE_TRADE_OUTLETS)
                    .append(" SET ")
                    .append(TABLE_TRADE_OUTLETS_COLUMN_NAME)
                    .append("=")
                    .append("'")
                    .append(tradeOutletTitle)
                    .append("'")
                    .append(",")
                    .append(TABLE_TRADE_OUTLETS_COLUMN_PHONE)
                    .append("=")
                    .append("'")
                    .append(tradeOutletPhone)
                    .append("'")
                    .append(",")
                    .append(TABLE_TRADE_OUTLETS_COLUMN_ADDRESS)
                    .append("=")
                    .append("'")
                    .append(tradeOutletAddress)
                    .append("'")
                    .append(" WHERE ")
                    .append(TABLE_TRADE_OUTLETS_COLUMN_ID)
                    .append("=")
                    .append(currentItemId);
            db.execSQL(insertText.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
