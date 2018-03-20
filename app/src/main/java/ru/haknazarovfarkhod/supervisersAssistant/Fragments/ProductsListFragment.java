package ru.haknazarovfarkhod.supervisersAssistant.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_Products;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.Order;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.Product;
import ru.haknazarovfarkhod.supervisersAssistant.R;

public class ProductsListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> listAdapter;
    DatabaseHelper_Products sqlHelper;
    SQLiteDatabase db;
    Long currentItemId;
    Cursor productCursor;
    SimpleCursorAdapter productListAdapter;
    ListView productsList;
    String TAG = "LOGi";
    View productsListFragment;
    FloatingActionButton newProductAddButton;
    private Order order;
    private int selectionMode = 0;

    public ProductsListFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newProductAddButton = productsListFragment.findViewById(R.id.newProductAddButton);
        newProductAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new ProductFragment(), "productFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putLong("productId", id);
                db = sqlHelper.getReadableDatabase();
                Product product = sqlHelper.getProductById(db, id);
                if (product != null) {
                    b.putString("productName", product.getProductName());
                }
                ProductFragment productFragment = new ProductFragment();
                productFragment.setArguments(b);
                if (selectionMode == 1) {

                    //order.orderLines.add(new OrderLine(id));
                    //b.putSerializable("order", order);

                    Fragment orderFragment = (OrderFragment) getFragmentManager().findFragmentByTag("orderFragment");

                    if (orderFragment != null) {
                        orderFragment.setArguments(b);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment, orderFragment, "orderFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                    }
                } else {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, productFragment, "productFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
            }
        });

        productsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentItemId = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.productDeleteAlertHeader)
                        .setMessage(R.string.productDeleteAlertText)
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
                                sqlHelper.deleteProductById(db, currentItemId);
                                onResume();
                            }
                        });

                builder.show();
                return true;
            }
        });
    }

    public static ProductsListFragment newInstance(String param1, String param2) {
        ProductsListFragment fragment = new ProductsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new DatabaseHelper_Products(getContext());

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey("selectionMode")) {
                selectionMode = bundle.getInt("selectionMode");
            }

            if (bundle.containsKey("order")) {
                order = (Order) bundle.getSerializable("order");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        productsListFragment = inflater.inflate(R.layout.fragment_prodicts, container, false);
        sqlHelper = new DatabaseHelper_Products(getContext());
        productsList = productsListFragment.findViewById(R.id.orderListScrollView);
        productsList.setPadding(10, 10, 10, 10);

        return productsListFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            db = sqlHelper.getReadableDatabase();
            sqlHelper.onCreate(db);

            productCursor = db.rawQuery("select * from " + DatabaseHelper_Products.TABLE_PRODUCTS, null);

            String[] headers = new String[]{DatabaseHelper_Products.COLUMN_PRODUCTNAME, DatabaseHelper_Products.COLUMN_ARTICLENUMBER};

            productListAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.two_line_list_item, productCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

            productsList.setAdapter(productListAdapter);
        } catch (SQLException ex) {
            Log.i(TAG, "productCursor: " + ex.getMessage());
        }
    }

    public void setListAdapter(ArrayAdapter<String> listAdapter) {
        this.listAdapter = listAdapter;
    }

    interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
