package ru.haknazarovfarkhod.supervisersAssistant.Fragments.Presenters;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_Main;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_Orders;
import ru.haknazarovfarkhod.supervisersAssistant.Fragments.MainFragment;
import ru.haknazarovfarkhod.supervisersAssistant.R;

/**
 * Created by USER on 20.03.2018.
 */

public class MainFragmentPresenter implements MainFragmentInt {
    private MainFragment mainFragment;
    private DatabaseHelper_Main sqlHelper;
    private SQLiteDatabase db;

    public MainFragmentPresenter(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    @Override
    public void refreshMainMenu() {
        sqlHelper = new DatabaseHelper_Main(mainFragment.getContext());

        db = sqlHelper.getReadableDatabase();
        HashMap totalParams = sqlHelper.getMainInformation(db);
        if (!totalParams.isEmpty()) {
            mainFragment.ordersQuantityTextView.setText((String) totalParams.get("ordersQuantity"));
            mainFragment.productMatrixQuantityTextView.setText((String) totalParams.get("productMatrixQuantity"));
            mainFragment.tradeOutletsQuantityTextView.setText((String) totalParams.get("tradeOutletsQuantity"));
        }
    }
}
