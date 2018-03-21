package ru.haknazarovfarkhod.supervisersAssistant.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

import ru.haknazarovfarkhod.supervisersAssistant.MainActivity;

/**
 * Created by USER on 20.03.2018.
 */

public class DatabaseHelperMain extends SQLiteOpenHelper {
    private static String dbName = "supervisorsAssistant.db";

    public DatabaseHelperMain(Context context) {
        super(context, dbName, null, MainActivity.SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //not used
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //not used
    }

    public Map getMainInformation(SQLiteDatabase db) {

        HashMap totalParams = new HashMap<>();

        String query = "SELECT \n" +
                "    sum(ordersQuantyti)\n" +
                "    ,sum(productsQuantity)\n" +
                "    ,sum(tradeOutletQuantity)\n" +
                " FROM(\n" +
                "SELECT \n" +
                "    count(*) ordersQuantyti\n" +
                "    ,0 productsQuantity\n" +
                "    ,0 tradeOutletQuantity\n" +
                " FROM " + DatabaseHelperOrders.TABLE_ORDERS + "\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "Select\n" +
                "    0\n" +
                "    ,count(*) \n" +
                "    ,0\n" +
                " FROM " + DatabaseHelperProducts.TABLE_PRODUCTS + "\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "Select\n" +
                "    0\n" +
                "    ,0\n" +
                "    ,count(*) \n" +
                "FROM " + DatabaseHelperTradeOutlets.TABLE_TRADE_OUTLETS + "\n" +
                ") \n" +
                ";\n";

        try {
            Cursor cursorTradeOutlet = db.rawQuery(query, null);
            if (cursorTradeOutlet.getCount() != 0) {
                cursorTradeOutlet.moveToFirst();
                totalParams.put("ordersQuantity", cursorTradeOutlet.getString(0));
                totalParams.put("productMatrixQuantity", cursorTradeOutlet.getString(1));
                totalParams.put("tradeOutletsQuantity", cursorTradeOutlet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalParams;
    }
}
