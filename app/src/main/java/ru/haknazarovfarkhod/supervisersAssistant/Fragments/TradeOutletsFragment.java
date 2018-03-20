package ru.haknazarovfarkhod.supervisersAssistant.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.Serializable;
import java.util.Map;

import ru.haknazarovfarkhod.supervisersAssistant.R;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_TradeOutlets;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.Order;

public class TradeOutletsFragment extends Fragment implements Serializable{
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> listAdapter;
    private static View tradeOutletsFragment;
    private long currentItemId;
    public Integer selectionMode = 0;
    private Order order;
    DatabaseHelper_TradeOutlets sqlHelper;
    SQLiteDatabase db;
    Cursor ordersCursor;
    SimpleCursorAdapter ordersListAdapter;
    ListView productsList;

    public TradeOutletsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null) {
            if (args.containsKey("order")) {
                order = (Order) args.getSerializable("order");
            }

            if (args.containsKey("selectionMode")) {
                selectionMode = args.getInt("selectionMode");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tradeOutletsFragment = inflater.inflate(R.layout.fragment_trade_outlets, container, false);

        sqlHelper = new DatabaseHelper_TradeOutlets(getContext());
        productsList = tradeOutletsFragment.findViewById(R.id.tradeOutletsListView);
        productsList.setPadding(10, 10, 10, 10);
        return tradeOutletsFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            db = sqlHelper.getReadableDatabase();
            sqlHelper.onCreate(db);

            ordersCursor = db.rawQuery("select * from " + DatabaseHelper_TradeOutlets.TABLE_TRADE_OUTLETS, null);

            String[] headers = new String[]{DatabaseHelper_TradeOutlets.TABLE_TRADE_OUTLETS_COLUMN_NAME, DatabaseHelper_TradeOutlets.TABLE_TRADE_OUTLETS_COLUMN_ADDRESS, DatabaseHelper_TradeOutlets.TABLE_TRADE_OUTLETS_COLUMN_PHONE};

            ordersListAdapter = new SimpleCursorAdapter(getContext(), R.layout.trade_outlet_item, ordersCursor, headers, new int[]{R.id.tradeOutletNameTextView, R.id.tradeOutletAddresTextView, R.id.tradeOutletPhoneTextView}, 0);

            productsList.setAdapter(ordersListAdapter);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton newTradeOutletButton = tradeOutletsFragment.findViewById(R.id.newTradeOutletButton);
        newTradeOutletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnButtonClicked(v);
            }
        });

        if(selectionMode==1){
            newTradeOutletButton.setVisibility(View.INVISIBLE);
        }

        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    onListItemSelected(id);
                                                }
                                            }

        );
        productsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentItemId = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.tradeOutletAlertHeader)
                        .setMessage(R.string.tradeOutletAlertMessage)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                db = sqlHelper.getWritableDatabase();
                                sqlHelper.deleteTradeOutletById(db, (int) currentItemId);
                                onResume();
                            }
                        });
                builder.show();
                return true;
            }
        });
    }

    private void OnButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.newTradeOutletButton:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new TradeOutletFragment(), "tradeOutletFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
        }
    }

    private void onListItemSelected(long itemId) {
        Bundle b = new Bundle();
        b.putLong("tradeOutletId", itemId);
        if(selectionMode==1){
            order.setTradeOutletId(itemId);
            Map tradeOutletParams = sqlHelper.readTradeOutletById(db, itemId);
            order.setTradeOutletName((String) tradeOutletParams.get("title"));
            b.putSerializable("order", (Serializable) order);

            Fragment orderFragment = (OrderFragment) getFragmentManager().findFragmentByTag("orderFragment");

            if(orderFragment!= null){
                orderFragment.setArguments(b);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, orderFragment, "orderFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        } else {
            TradeOutletFragment tradeOutletFragment = new TradeOutletFragment();
            tradeOutletFragment.setArguments(b);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, tradeOutletFragment, "tradeOutletFragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }
}
