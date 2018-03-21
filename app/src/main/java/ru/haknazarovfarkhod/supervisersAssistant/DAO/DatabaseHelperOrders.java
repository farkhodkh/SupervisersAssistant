package ru.haknazarovfarkhod.supervisersAssistant.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

import java.util.Date;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.Order;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.OrderLine;
import ru.haknazarovfarkhod.supervisersAssistant.MainActivity;

public class DatabaseHelperOrders extends SQLiteOpenHelper {
    private static String dbPath;
    private static String dbName = "supervisorsAssistant.db";
    private String integertype = "INTEGER";

    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ORDERS_DETAILS = "orders_details";

    // названия столбцов products
    public static final String TABLE_ORDERS_COLUMN_ORDER_NUMBER = "_id";
    public static final String TABLE_ORDERS_COLUMN_ORDER_DATE = "orderDate";
    public static final String TABLE_ORDERS_COLUMN_ORDER_DATE_STRING = "orderDateString";
    public static final String TABLE_ORDERS_COLUMN_TRADE_OUTLET_ID = "outletId";

    public static final String TABLE_ORDERS_DETAILS_COLUMN_LINE_NUMBER = "_id";
    public static final String TABLE_ORDERS_DETAILS_COLUMN_ORDER_NUMBER = "orderNumber";
    public static final String TABLE_ORDERS_DETAILS_COLUMN_PRODUCT_NUMBER = "productNumber";
    public static final String TABLE_ORDERS_DETAILS_COLUMN_QUANTITY = "quantity";

    public DatabaseHelperOrders(Context context) {
        super(context, dbName, null, MainActivity.SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dbInitialization(db);
    }

    public void dbInitialization(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + " ("
                + TABLE_ORDERS_COLUMN_ORDER_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TABLE_ORDERS_COLUMN_ORDER_DATE + " " + integertype + ", "
                + TABLE_ORDERS_COLUMN_ORDER_DATE_STRING + " STRING, "
                + TABLE_ORDERS_COLUMN_TRADE_OUTLET_ID + " INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS_DETAILS + "("
                + TABLE_ORDERS_DETAILS_COLUMN_LINE_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_ORDERS_DETAILS_COLUMN_ORDER_NUMBER + " INTEGER REFERENCES " + TABLE_ORDERS + " (_id), "
                + TABLE_ORDERS_DETAILS_COLUMN_PRODUCT_NUMBER + " " + integertype + ", "
                + TABLE_ORDERS_DETAILS_COLUMN_QUANTITY + " " + integertype + ", "
                + "UNIQUE ("
                + TABLE_ORDERS_DETAILS_COLUMN_LINE_NUMBER + " ASC,"
                + TABLE_ORDERS_DETAILS_COLUMN_ORDER_NUMBER + " ASC"
                + "));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Not used method
    }

    public void addOrderLine(SQLiteDatabase db, Long orderId, OrderLine productLine) {
        ContentValues newValues = new ContentValues();

        newValues.put(TABLE_ORDERS_DETAILS_COLUMN_ORDER_NUMBER, orderId);
        newValues.put(TABLE_ORDERS_DETAILS_COLUMN_PRODUCT_NUMBER, productLine.getProductId());
        newValues.put(TABLE_ORDERS_DETAILS_COLUMN_QUANTITY, productLine.getQuantity());
        try {
            long uniqNumber = db.insert(TABLE_ORDERS_DETAILS, null, newValues);
            if (uniqNumber != 0l) {
                productLine.setLineUniqNimber(uniqNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrderLine(SQLiteDatabase db, OrderLine productLine) {
        ContentValues newValues = new ContentValues();

        newValues.put(TABLE_ORDERS_DETAILS_COLUMN_PRODUCT_NUMBER, productLine.getProductId());
        newValues.put(TABLE_ORDERS_DETAILS_COLUMN_QUANTITY, productLine.getQuantity());

        try {
            db.update(TABLE_ORDERS_DETAILS, newValues, TABLE_ORDERS_DETAILS_COLUMN_LINE_NUMBER + " = " + productLine.getLineUniqNimber(), null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order createNewOrder(SQLiteDatabase db) {

        Date d = new Date();
        Long currentTime = d.getTime();

        Order order;
        Long result;
        try {
            ContentValues newValues = new ContentValues();
            newValues.put(TABLE_ORDERS_COLUMN_ORDER_DATE, currentTime);
            newValues.put(TABLE_ORDERS_COLUMN_ORDER_DATE_STRING, (String) DateFormat.format("yyyy-MM-dd hh:mm:ss", currentTime));
            result = db.insert(TABLE_ORDERS, null, newValues);
        } catch (SQLException e) {
            e.printStackTrace();
            result = 0l;
        }

        if (result > 0) {
            order = new Order(result, currentTime);
        } else {
            order = null;
        }
        return order;
    }

    public void saveOrder(SQLiteDatabase db, Order order) {
        ContentValues newValues = new ContentValues();

        for (int i = 0; i < order.getOrderLines().size(); i++) {
            newValues.clear();
            OrderLine orderLine = order.getOrderLines().get(i);
            updateOrderLine(db, orderLine);
        }

        newValues.clear();

        newValues.put(TABLE_ORDERS_COLUMN_ORDER_DATE, order.getOrderDate());
        newValues.put(TABLE_ORDERS_COLUMN_TRADE_OUTLET_ID, order.getTradeOutletId());

        db.update(TABLE_ORDERS, newValues, TABLE_ORDERS_COLUMN_ORDER_NUMBER + "=" + order.getOrderId(), null);
    }

    public void deleteOrder(SQLiteDatabase db, Order order) {
        if (order != null) {
            db.delete(TABLE_ORDERS, TABLE_ORDERS_COLUMN_ORDER_NUMBER + "=" + order.getOrderId(), null);
            db.delete(TABLE_ORDERS_DETAILS, TABLE_ORDERS_DETAILS_COLUMN_ORDER_NUMBER + "=" + order.getOrderId(), null);
        }
    }

    public SQLiteDatabase open() throws SQLException {

        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public Order getOrderByID(SQLiteDatabase db, long orderID) {
        Order order = null;
        Cursor orderCursor = db.rawQuery("select TABLE_ORDERS._id, TABLE_ORDERS.orderDate, TRADE_OUTLETS.name, TRADE_OUTLETS._id" + " FROM " + DatabaseHelperOrders.TABLE_ORDERS + " TABLE_ORDERS "
                + "LEFT JOIN " + DatabaseHelperTradeOutlets.TABLE_TRADE_OUTLETS + " TRADE_OUTLETS ON TRADE_OUTLETS._id = TABLE_ORDERS.outletId"
                + " WHERE TABLE_ORDERS._id = " + orderID, null);
        if (orderCursor.getCount() != 0) {
            orderCursor.moveToNext();
            order = new Order(orderCursor.getLong(0), orderCursor.getLong(1), orderCursor.getString(2), orderCursor.getLong(3));
        }
        if (order != null) {
            String query = "Select orders_details._id,\n"
                    + " (select count(*) from orders_details tbl  where orders_details.orderNumber = tbl.orderNumber and orders_details._id >= tbl._id) lineNumber,\n"
                    + " products.productName,\n"
                    + " orders_details.productNumber,\n"
                    + " orders_details.quantity\n"
                    + " FROM " + TABLE_ORDERS_DETAILS + " orders_details \n"
                    + " LEFT JOIN " + DatabaseHelperProducts.TABLE_PRODUCTS + " products "
                    + " ON products._id = orders_details.productNumber \n"
                    + " WHERE orders_details.orderNumber = " + orderID;

            Cursor orderDetailsCursor = db.rawQuery(query, null);

            if (orderDetailsCursor != null && orderDetailsCursor.moveToFirst()) {
                do {
                    order.addLine(new OrderLine(orderDetailsCursor.getLong(0), orderDetailsCursor.getInt(1), orderDetailsCursor.getString(2), orderDetailsCursor.getLong(3), orderDetailsCursor.getInt(4)));
                } while (orderDetailsCursor.moveToNext());
            }
        }

        return order;
    }
}
