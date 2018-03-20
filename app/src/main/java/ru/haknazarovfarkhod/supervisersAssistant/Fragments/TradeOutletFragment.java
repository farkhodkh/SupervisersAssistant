package ru.haknazarovfarkhod.supervisersAssistant.Fragments;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.DatabaseHelper_TradeOutlets;
import ru.haknazarovfarkhod.supervisersAssistant.R;

public class TradeOutletFragment extends Fragment {
    private View tradeOutletFragment;
    private EditText tradeOutletNamePlainText;
    private EditText tradeOutletAddresPlainText;
    private EditText tradeOutletPhone;
    private long currentItemId;

    DatabaseHelper_TradeOutlets sqlHelper;
    SQLiteDatabase db;

    public TradeOutletFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null && b.containsKey("tradeOutletId")) {
            currentItemId = b.getLong("tradeOutletId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tradeOutletFragment = inflater.inflate(R.layout.fragment_trade_outlet, container, false);
        sqlHelper = new DatabaseHelper_TradeOutlets(getContext());
        tradeOutletNamePlainText = tradeOutletFragment.findViewById(R.id.tradeOutletNamePlainText);
        tradeOutletAddresPlainText = tradeOutletFragment.findViewById(R.id.tradeOutletAddresPlainText);
        tradeOutletPhone = tradeOutletFragment.findViewById(R.id.tradeOutletPhone);
        if (currentItemId != 0) {
            readTradeOutletById(currentItemId);
        }
        return tradeOutletFragment;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);


    }

    private void readTradeOutletById(long id) {
        db = sqlHelper.getReadableDatabase();
        HashMap tradeOutletParams = sqlHelper.readTradeOutletById(db, id);
        if (!tradeOutletParams.isEmpty()) {
            tradeOutletNamePlainText.setText((String) tradeOutletParams.get("title"));
            tradeOutletAddresPlainText.setText((String) tradeOutletParams.get("phone"));
            tradeOutletPhone.setText((String) tradeOutletParams.get("address"));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button tradeOutletFragmentOKButton = tradeOutletFragment.findViewById(R.id.tradeOutletFragmentOKButton);
        tradeOutletFragmentOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnButtonClicked(v);
            }
        });

        Button tradeOutletFragmentCancelButton = tradeOutletFragment.findViewById(R.id.tradeOutletFragmentCancelButton);
        tradeOutletFragmentCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnButtonClicked(v);
            }
        });
    }

    private void OnButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.tradeOutletFragmentOKButton:
                if (tradeOutletNamePlainText.getText().toString().length() == 0 || tradeOutletAddresPlainText.getText().toString().length() == 0 || tradeOutletPhone.getText().toString().length() == 0) {
                    return;
                }
                db = sqlHelper.getWritableDatabase();
                if (currentItemId == 0) {
                    sqlHelper.saveTradeOutlet(db, (String) tradeOutletNamePlainText.getText().toString(), (String) tradeOutletAddresPlainText.getText().toString(), (String) tradeOutletPhone.getText().toString());
                } else {
                    sqlHelper.overWriteTradeOutletById(db, currentItemId, (String) tradeOutletNamePlainText.getText().toString(), (String) tradeOutletAddresPlainText.getText().toString(), (String) tradeOutletPhone.getText().toString());
                }
                returnToTradeOutletsFragment();
                break;
            case R.id.tradeOutletFragmentCancelButton:
                returnToTradeOutletsFragment();
                break;
            default:

        }
    }

    private void returnToTradeOutletsFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, new TradeOutletsFragment(), "tradeOutletsFragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
