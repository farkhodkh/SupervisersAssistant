package ru.haknazarovfarkhod.supervisersAssistant.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.Product;
import ru.haknazarovfarkhod.supervisersAssistant.MainActivity;

public class DatabaseHelper_Products extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "supervisorsAssistant.db";
    public static final String TABLE_PRODUCTS = "products";

    // названия столбцов products
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productName";
    public static final String COLUMN_UNITOFMEASUREMENT = "unitOfMeasurement";
    public static final String COLUMN_ARTICLENUMBER = "articleNumber";
    public static final String COLUMN_MINIMUMQUANTITY = "minimumQuantity";

    private Context myContext;

    public DatabaseHelper_Products(Context context) {
        super(context, DB_NAME, null, MainActivity.SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCTNAME + " TEXT, "
                + COLUMN_UNITOFMEASUREMENT + " CHAR (2), "
                + COLUMN_ARTICLENUMBER + " CHAR (12), "
                + COLUMN_MINIMUMQUANTITY + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public SQLiteDatabase open() throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void saveProduct(SQLiteDatabase db, Product product) {

        ContentValues newValues = new ContentValues();

        newValues.put(COLUMN_PRODUCTNAME, product.getProductName());
        newValues.put(COLUMN_UNITOFMEASUREMENT, product.getUnitOfMeasurement());
        newValues.put(COLUMN_ARTICLENUMBER, product.getActicle());
        newValues.put(COLUMN_MINIMUMQUANTITY, product.getMinimumQuantity());
        newValues.put(COLUMN_ARTICLENUMBER, product.getActicle());

        try {
            db.insert(TABLE_PRODUCTS, null, newValues);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper_Products.TABLE_PRODUCTS, null);
        Boolean b = true;
    }

    public Product getProductById(SQLiteDatabase db, long currentProductId) {
        Product product = null;
        try {
            Cursor cursor = db.rawQuery("SELECT " + COLUMN_PRODUCTNAME + "," + COLUMN_UNITOFMEASUREMENT + "," + COLUMN_ARTICLENUMBER + "," + COLUMN_MINIMUMQUANTITY + " FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_ID + " = " + currentProductId, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                product = new Product(cursor.getString(0).toString(), cursor.getString(1), cursor.getLong(3), cursor.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public void deleteProductById(SQLiteDatabase db, Long currentItemId) {
        try {
            db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_ID + " = " + currentItemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(SQLiteDatabase db, Product product, Long currentProductId) {
        try {
            StringBuilder insertText = new StringBuilder();
            insertText.append("UPDATE ")
                    .append(TABLE_PRODUCTS)
                    .append(" SET ")
                    .append(COLUMN_PRODUCTNAME)
                    .append("=")
                    .append("'")
                    .append(product.getProductName())
                    .append("'")
                    .append(",")
                    .append(COLUMN_UNITOFMEASUREMENT)
                    .append("=")
                    .append("'")
                    .append(product.getUnitOfMeasurement())
                    .append("'")
                    .append(",")
                    .append(COLUMN_ARTICLENUMBER)
                    .append("=")
                    .append("'")
                    .append(product.getActicle())
                    .append("'")
                    .append(",")
                    .append(COLUMN_MINIMUMQUANTITY)
                    .append("=")
                    .append("'")
                    .append(product.getMinimumQuantity())
                    .append("'")
                    .append(" WHERE ")
                    .append(COLUMN_ID)
                    .append("=")
                    .append(currentProductId);
            db.execSQL(insertText.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
