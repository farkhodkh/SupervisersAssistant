package ru.haknazarovfarkhod.supervisersAssistant.Fragments;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelperOrders;
import ru.haknazarovfarkhod.supervisersAssistant.R;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelperProducts;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.Order;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.OrderLine;

public class OrderFragment extends Fragment implements Serializable {
    public Order order;

    private OnFragmentInteractionListener mListener;
    private Button orderOKButton;
    private Button orderCancelButton;
    private FloatingActionButton selectTradeOutletButton;
    private FloatingActionButton addItemButton;
    private TextView orderTradeOutletTextView;
    private TextView orderNumberTextView;
    private TextView orderDateTextView;
    private static View orderFragment;
    private static ProductsListFragment productsListFragment;
    private static TradeOutletsFragment tradeOutletsFragment;
    SimpleCursorAdapter ordersListAdapter;
    ListView productsList;
    private ArrayList<OrderLine> orderLines;

    Cursor ordersCursor;
    DatabaseHelperOrders sqlHelper;

    SQLiteDatabase db;

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("order")) {
                order = (Order) args.getSerializable("order");
            }
        }
        if (order != null) {
            orderTradeOutletTextView.setText(order.getTradeOutletName());
            orderNumberTextView.setText(order.getOrderId().toString());
            orderDateTextView.setText(order.getOrderDate(true));

        }
        Long productId = null;
        String productName = null;

        if (args != null&&order != null) {
            if (args.containsKey("productId")) {
                productId = args.getLong("productId");
            }

            if (args.containsKey("productName")) {
                productName = args.getString("productName");
            }

            if (productId != null) {
                OrderLine orderLine = new OrderLine(productId, productName);

                db = sqlHelper.getWritableDatabase();
                sqlHelper.addOrderLine(db, order.getOrderId(), orderLine);
                order.addLine(orderLine);
            }

            if (args != null && order != null && args.containsKey("orderLine")) {
                OrderLine orderLine = (OrderLine) args.getSerializable("orderLine");
                db = sqlHelper.getWritableDatabase();
                sqlHelper.updateOrderLine(db, orderLine);
            }
        }
        fillOrderItems();

    }

    public OrderFragment() {

    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new DatabaseHelperOrders(getContext());
        db = sqlHelper.getWritableDatabase();
        sqlHelper.dbInitialization(db);

        Bundle args = getArguments();
        if (args == null) {
            order = sqlHelper.createNewOrder(db);
        } else {
            if (args.containsKey("orderID")) {
                db = sqlHelper.getReadableDatabase();
                order = sqlHelper.getOrderByID(db, args.getLong("orderID"));
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        orderFragment = inflater.inflate(R.layout.fragment_order, container, false);

        productsList = orderFragment.findViewById(R.id.orderItemsScrollView);

        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                OrderLine orderLine = order.getOrderLine(position);
                bundle.putSerializable("orderLine", (Serializable) orderLine);
                OrderLineFragment orderLineFragment = new OrderLineFragment();
                orderLineFragment.setArguments(bundle);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, orderLineFragment, "orderLineFragment")
                        .addToBackStack("Home")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

            }
        });

        productsListFragment = new ProductsListFragment();
        tradeOutletsFragment = new TradeOutletsFragment();

        addItemButton = orderFragment.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });

        orderOKButton = orderFragment.findViewById(R.id.orderOKButton);
        orderOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });

        orderCancelButton = orderFragment.findViewById(R.id.orderCancelButton);
        orderCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });

        selectTradeOutletButton = orderFragment.findViewById(R.id.selectTradeOutletButton);
        selectTradeOutletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });


        orderTradeOutletTextView = orderFragment.findViewById(R.id.orderFragmentTradeOutletTextView);

        orderNumberTextView = orderFragment.findViewById(R.id.orderNumberTextView);
        orderDateTextView = orderFragment.findViewById(R.id.orderDate);
        return orderFragment;
    }

    private void fillOrderItems() {
        try {
            db = sqlHelper.getReadableDatabase();
            sqlHelper.onCreate(db);

            String query ="SELECT orders_details._id _id,\n" +
                    "  (select count(*) from orders_details tbl  where orders_details.orderNumber = tbl.orderNumber and orders_details._id >= tbl._id) lineNumber,\n" +
                    "  tblProducts.productName " + DatabaseHelperProducts.COLUMN_PRODUCTNAME + ",\n" +
                    "  " + DatabaseHelperOrders.TABLE_ORDERS_DETAILS_COLUMN_QUANTITY + ",\n" +
                    "  tblProducts.unitOfMeasurement " + DatabaseHelperProducts.COLUMN_UNITOFMEASUREMENT +
                    "  FROM orders_details orders_details\n" +
                    "  LEFT JOIN " + DatabaseHelperProducts.TABLE_PRODUCTS + " tblProducts\n" +
                    "  ON tblProducts._id = orders_details.productNumber\n" +
                    "  WHERE orders_details.orderNumber = " + order.getOrderId() +
                    "  ORDER BY orders_details._id;\n";

            ordersCursor = db.rawQuery(query, null);
            String[] headers = new String[]{DatabaseHelperOrders.TABLE_ORDERS_DETAILS_COLUMN_LINE_NUMBER, "lineNumber", DatabaseHelperProducts.COLUMN_PRODUCTNAME, DatabaseHelperOrders.TABLE_ORDERS_DETAILS_COLUMN_QUANTITY, DatabaseHelperProducts.COLUMN_UNITOFMEASUREMENT};

            ordersListAdapter = new SimpleCursorAdapter(getContext(), R.layout.order_details_product_line, ordersCursor, headers, new int[]{R.id.lineUniqNumber, R.id.lineNumberTextView, R.id.productNameTextView, R.id.quantityTextView, R.id.unitOfMeasurementTextView}, 0);
            productsList.setAdapter(ordersListAdapter);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.addItemButton: {
                Bundle b = new Bundle();
                b.putInt("selectionMode", 1);
                productsListFragment.setArguments(b);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, productsListFragment, "ordersListFragment")
                        .setPrimaryNavigationFragment(this)
                        .addToBackStack("Home")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            }
            case R.id.orderOKButton: {
                sqlHelper.saveOrder(db, order);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new OrdersListFragment(), "ordersListFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            }
            case R.id.orderCancelButton: {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new OrdersListFragment(), "ordersListFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            }
            case R.id.selectTradeOutletButton: {
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", (Serializable) order);
                tradeOutletsFragment.selectionMode = 1;
                tradeOutletsFragment.setArguments(bundle);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, tradeOutletsFragment, "tradeOutletsFragment")
                        .setPrimaryNavigationFragment(this)
                        .addToBackStack("Home")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
