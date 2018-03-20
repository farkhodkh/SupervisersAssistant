package ru.haknazarovfarkhod.supervisersAssistant.Fragments;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_Orders;
import ru.haknazarovfarkhod.supervisersAssistant.R;

public class OrderItemsListFragment extends Fragment implements View.OnClickListener {
    //private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> listAdapter;
    DatabaseHelper_Orders sqlHelper;
    SQLiteDatabase db;
    Cursor productCursor;
    SimpleCursorAdapter productListAdapter;
    ListView productsList;
    String TAG = "LOGi";

    public OrderItemsListFragment() {
        // Required empty public constructor
    }

    public static OrderItemsListFragment newInstance(String param1, String param2) {
        OrderItemsListFragment fragment = new OrderItemsListFragment();
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
    public void onResume() {
        super.onResume();

        try {
            db = sqlHelper.getReadableDatabase();
            sqlHelper.onCreate(db);

            productCursor = db.rawQuery("select * from " + DatabaseHelper_Orders.TABLE_ORDERS_DETAILS + " where " + DatabaseHelper_Orders.TABLE_ORDERS_DETAILS_COLUMN_ORDER_NUMBER + " = 1", null);

            String[] headers = new String[]{DatabaseHelper_Orders.TABLE_ORDERS_DETAILS_COLUMN_LINE_NUMBER, DatabaseHelper_Orders.TABLE_ORDERS_DETAILS_COLUMN_PRODUCT_NUMBER};

            productListAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.two_line_list_item, productCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

            productsList.setAdapter(productListAdapter);
        } catch (SQLException ex) {
            Log.i(TAG, "productCursor: " + ex.getMessage());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_items_list, container, false);
        sqlHelper = new DatabaseHelper_Orders(getContext());
        productsList = view.findViewById(R.id.orderItemsScrollView);
        productsList.setPadding(10, 10, 10, 10);
        return view;
    }

    public void setListAdapter(ArrayAdapter<String> listAdapter) {
        this.listAdapter = listAdapter;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "Button clicked", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
