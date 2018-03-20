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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_Orders;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_TradeOutlets;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.Order;
import ru.haknazarovfarkhod.supervisersAssistant.R;

public class OrdersListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> listAdapter;
    DatabaseHelper_Orders sqlHelper;
    SQLiteDatabase db;
    Cursor ordersCursor;
    SimpleCursorAdapter ordersListAdapter;
    ListView ordersList;
    String TAG = "LOGi";
    private Long currentItemId;
    FloatingActionButton newOrderButton;

    public OrdersListFragment() {
    }

    public static OrdersListFragment newInstance(String param1, String param2) {
        OrdersListFragment fragment = new OrdersListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new DatabaseHelper_Orders(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);

        sqlHelper = new DatabaseHelper_Orders(getContext());

        ordersList = view.findViewById(R.id.ordersListView);
        ordersList.setPadding(10, 10, 10, 10);

        newOrderButton = view.findViewById(R.id.newOrderButton);
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new OrderFragment(), "orderFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                db = sqlHelper.getReadableDatabase();
                Order order = sqlHelper.getOrderByID(db, id);
                args.putSerializable("order", order);
                OrderFragment orderFragment = (OrderFragment) getFragmentManager().findFragmentByTag("orderFragment");
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                }
                orderFragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, orderFragment, "orderFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

            }
        });

        ordersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentItemId = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.orderDeleteAlertHeader)
                        .setMessage(R.string.orderDeleteAlertText)
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
                                sqlHelper.deleteOrder(db, new Order(currentItemId, null));
                                onResume();
                            }
                        });

                builder.show();
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            db = sqlHelper.getReadableDatabase();
            sqlHelper.onCreate(db);

            ordersCursor = db.rawQuery("select TABLE_ORDERS._id, TABLE_ORDERS.orderDateString, TRADE_OUTLETS.name from " + DatabaseHelper_Orders.TABLE_ORDERS + " TABLE_ORDERS LEFT JOIN "
                    + DatabaseHelper_TradeOutlets.TABLE_TRADE_OUTLETS + " TRADE_OUTLETS ON TABLE_ORDERS.outletId = TRADE_OUTLETS._id", null);

            String[] headers = new String[]{DatabaseHelper_Orders.TABLE_ORDERS_COLUMN_ORDER_NUMBER, DatabaseHelper_Orders.TABLE_ORDERS_COLUMN_ORDER_DATE_STRING, DatabaseHelper_TradeOutlets.TABLE_TRADE_OUTLETS_COLUMN_NAME};

            ordersListAdapter = new SimpleCursorAdapter(getContext(), R.layout.order_line_item, ordersCursor, headers, new int[]{R.id.orderNumberTextView, R.id.orderDateTextView, R.id.tradeOutletInformationTextView}, 0);

            ordersList.setAdapter(ordersListAdapter);
        } catch (SQLException ex) {
            Log.i(TAG, "OrdersListFragment: " + ex.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
