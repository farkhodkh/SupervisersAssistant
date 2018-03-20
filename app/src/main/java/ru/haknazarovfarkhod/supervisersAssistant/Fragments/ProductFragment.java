package ru.haknazarovfarkhod.supervisersAssistant.Fragments;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_Orders;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_Products;
import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.Product;
import ru.haknazarovfarkhod.supervisersAssistant.R;

public class ProductFragment extends Fragment {
    private View productFragment;
    private long currentProductId = 0l;
    private Button productOKButton;
    private Button productCancelButton;
    private DatabaseHelper_Products sqlHelper;
    private DatabaseHelper_Orders sqlOrderHelper;
    private SQLiteDatabase db;
    private EditText productName;
    private EditText productUnitOfMeasurement;
    private EditText productMinimumQuantity;
    private EditText productArticle;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        productOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnButtonClicked(v);
            }
        });

        productCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnButtonClicked(v);
            }
        });
    }

    private void OnButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.productOKButton:
                db = sqlHelper.getWritableDatabase();
                Product product = new Product(productName.getText().toString(), productUnitOfMeasurement.getText().toString(), Long.getLong(productMinimumQuantity.getText().toString()), productArticle.getText().toString());
                if (product.getProductName().equals("") || product.getUnitOfMeasurement().equals("")) {
                    return;
                }
                if (currentProductId == 0l) {
                    sqlHelper.saveProduct(db, product);
                } else {
                    sqlHelper.updateProduct(db, product, currentProductId);
                }
                backToProductsListFragment();
                break;
            case R.id.productCancelButton:
                backToProductsListFragment();
                break;
            default:
                break;
        }
    }

    public ProductFragment() {

    }

    private void backToProductsListFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, new ProductsListFragment(), "productsListFragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null && b.containsKey("productId")) {
            currentProductId = b.getLong("productId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        productFragment = inflater.inflate(R.layout.fragment_product, container, false);
        productOKButton = productFragment.findViewById(R.id.productOKButton);
        productCancelButton = productFragment.findViewById(R.id.productCancelButton);

        productName = productFragment.findViewById(R.id.productName);
        productUnitOfMeasurement = productFragment.findViewById(R.id.productUnitOfMeasurement);
        productMinimumQuantity = productFragment.findViewById(R.id.productMinimumQuantity);
        productArticle = productFragment.findViewById(R.id.productArticle);

        sqlHelper = new DatabaseHelper_Products(getContext());

        if (currentProductId != 0) {
            db = sqlHelper.getReadableDatabase();
            Product product = sqlHelper.getProductById(db, currentProductId);
            if (product != null) {
                productName.setText(product.getProductName());
                productUnitOfMeasurement.setText(product.getUnitOfMeasurement());
                productMinimumQuantity.setText(product.getMinimumQuantity().toString());
                productArticle.setText(product.getActicle());
            }
        }

        return productFragment;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
