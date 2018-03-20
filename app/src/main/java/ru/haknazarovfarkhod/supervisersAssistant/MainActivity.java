package ru.haknazarovfarkhod.supervisersAssistant;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.haknazarovfarkhod.supervisersAssistant.Fragments.MainFragment;
import ru.haknazarovfarkhod.supervisersAssistant.Fragments.OrderFragment;
import ru.haknazarovfarkhod.supervisersAssistant.Fragments.OrdersListFragment;
import ru.haknazarovfarkhod.supervisersAssistant.Fragments.ProductsListFragment;
import ru.haknazarovfarkhod.supervisersAssistant.Fragments.TradeOutletsFragment;
import ru.haknazarovfarkhod.supervisersAssistant.ResideMenuComponents.ResideMenu;
import ru.haknazarovfarkhod.supervisersAssistant.ResideMenuComponents.ResideMenuItem;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSION_WRITE = 1001;
    public ResideMenu resideMenu;
    private MainActivity mContext;
    public static final int SCHEMA = 1;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemNewOrders;
    private ResideMenuItem itemOrders;
    private ResideMenuItem itemTradeOutlets;
    private ResideMenuItem itemProductMatrix;
    private boolean permissionGranted;
    private int uiOptions;
    public static TextView mainActivityHeaderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        setUpMenu();

        mainActivityHeaderTextView = findViewById(R.id.mainActivityHeaderTextView);

        if (savedInstanceState == null) {
            changeFragment(new MainFragment(), "mainFragment", R.string.homeFragmentHeader);
        }
        View decorView = decorView = getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

    }

    public void changeFragment(Fragment targetFragment, String fragmentTag, Integer header) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, fragmentTag)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        Resources res = getResources();
        mainActivityHeaderTextView.setText(res.getString(header));
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.icon_home, getString(R.string.menuItem_Home));
        itemNewOrders = new ResideMenuItem(this, R.drawable.icon_order, getString(R.string.menuItem_NewOrder));
        itemOrders = new ResideMenuItem(this, R.drawable.icon_order, getString(R.string.menuItem_OrdersList));
        itemTradeOutlets = new ResideMenuItem(this, R.drawable.icon_customers, getString(R.string.meniItem_TradeOutlets));
        itemProductMatrix = new ResideMenuItem(this, R.drawable.icon_product_matrix, getString(R.string.menuItem_ProductMatrix));

        itemHome.setOnClickListener(this);
        itemNewOrders.setOnClickListener(this);
        itemOrders.setOnClickListener(this);
        itemTradeOutlets.setOnClickListener(this);
        itemProductMatrix.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemNewOrders, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemOrders, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemTradeOutlets, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemProductMatrix, ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome) {
            changeFragment(new MainFragment(), "mainFragment", R.string.homeFragmentHeader);
        } else if (view == itemNewOrders) {
            changeFragment(new OrderFragment(), "orderFragment", R.string.orderFragmentHeader);
        } else if (view == itemOrders) {
            changeFragment(new OrdersListFragment(), "ordersListFragment", R.string.ordersListHeader);
        } else if (view == itemTradeOutlets) {
            changeFragment(new TradeOutletsFragment(), "tradeOutletsFragment", R.string.tradeOutletsHeader);
        } else if (view == itemProductMatrix) {
            changeFragment(new ProductsListFragment(), "productsListFragment", R.string.productsListHeader);
        } else if (view.getId() == R.id.addItemButton) {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
        }
        resideMenu.setSystemUiVisibility(uiOptions);
        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            //Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageReadable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
//                    Toast.makeText(this, "External storage permission granted",
//                            Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
