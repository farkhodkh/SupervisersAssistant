package ru.haknazarovfarkhod.supervisersAssistant.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses.OrderLine;
import ru.haknazarovfarkhod.supervisersAssistant.R;

public class OrderLineFragment extends Fragment {
    private static View orderLineFragment;
    private TextView lineNumberTextView;
    private TextView productNameTextView;
    private EditText quantityEditText;
    private OrderLine orderLine;
    private Button OKButton;
    private Button CancelButton;

    private OnFragmentInteractionListener mListener;

    public OrderLineFragment() {
    }

    public static OrderLineFragment newInstance(String param1, String param2) {
        OrderLineFragment fragment = new OrderLineFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("orderLine")) {
                orderLine = (OrderLine) args.getSerializable("orderLine");
                try {
                    productNameTextView.setText(orderLine.getProductName());
                    Integer quantity = orderLine.getQuantity();
                    quantityEditText.setText(String.valueOf(quantity));
                    lineNumberTextView.setText(orderLine.getLineNumber());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        orderLineFragment = inflater.inflate(R.layout.fragment_order_line, container, false);
        lineNumberTextView = orderLineFragment.findViewById(R.id.lineNumberTextView);
        productNameTextView = orderLineFragment.findViewById(R.id.productNameTextView);
        quantityEditText = orderLineFragment.findViewById(R.id.quantityEditText);
        CancelButton = orderLineFragment.findViewById(R.id.orderLineFragmentCancelButton);
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFragment orderFragment = (OrderFragment) getFragmentManager().findFragmentByTag("orderFragment");

                if (orderFragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, orderFragment, "orderFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
            }
        });
        OKButton = orderLineFragment.findViewById(R.id.orderLineFragmentOKButton);
        OKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = Integer.parseInt(quantityEditText.getText().toString());
                orderLine.setQuantity(quantity);

                OrderFragment orderFragment = (OrderFragment) getFragmentManager().findFragmentByTag("orderFragment");

                if (orderFragment != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderLine", orderLine);
                    orderFragment.setArguments(bundle);

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, orderFragment, "orderFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
            }
        });
        return orderLineFragment;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
