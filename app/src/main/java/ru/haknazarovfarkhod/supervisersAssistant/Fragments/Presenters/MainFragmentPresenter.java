package ru.haknazarovfarkhod.supervisersAssistant.Fragments.Presenters;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelperMain;
import ru.haknazarovfarkhod.supervisersAssistant.Fragments.MainFragment;

/**
 * Created by USER on 20.03.2018.
 */

public class MainFragmentPresenter implements MainFragmentInt {
    private MainFragment mainFragment;
    private DatabaseHelperMain sqlHelper;
    private SQLiteDatabase db;

    public MainFragmentPresenter(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    @Override
    public void refreshMainMenu() {
        sqlHelper = new DatabaseHelperMain(mainFragment.getContext());

        db = sqlHelper.getReadableDatabase();
        Map totalParams = sqlHelper.getMainInformation(db);
        if (!totalParams.isEmpty()) {
            mainFragment.ordersQuantityTextView.setText((String) totalParams.get("ordersQuantity"));
            mainFragment.productMatrixQuantityTextView.setText((String) totalParams.get("productMatrixQuantity"));
            mainFragment.tradeOutletsQuantityTextView.setText((String) totalParams.get("tradeOutletsQuantity"));
        }
    }
}
