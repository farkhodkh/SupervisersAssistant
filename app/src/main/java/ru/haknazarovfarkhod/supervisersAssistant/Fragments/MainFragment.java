package ru.haknazarovfarkhod.supervisersAssistant.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.haknazarovfarkhod.supervisersAssistant.Fragments.Presenters.MainFragmentPresenter;
import ru.haknazarovfarkhod.supervisersAssistant.R;

public class MainFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private View mainFragment;
    public TextView ordersQuantityTextView;
    public TextView productMatrixQuantityTextView;
    public TextView tradeOutletsQuantityTextView;
    private MainFragmentPresenter mainFragmentPresenter;

    public MainFragment() {

    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainFragmentPresenter.refreshMainMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainFragment = inflater.inflate(R.layout.fragment_main, container, false);

        ordersQuantityTextView = mainFragment.findViewById(R.id.ordersQuantityTextView);
        productMatrixQuantityTextView = mainFragment.findViewById(R.id.productMatrixQuantityTextView);
        tradeOutletsQuantityTextView = mainFragment.findViewById(R.id.tradeOutletsQuantytiTextView);
        mainFragmentPresenter = new MainFragmentPresenter(this);

        return mainFragment;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
