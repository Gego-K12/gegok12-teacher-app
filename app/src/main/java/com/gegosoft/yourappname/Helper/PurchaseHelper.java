package com.gegosoft.yourappname.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gegosoft.yourappname.R;

public class PurchaseHelper {
    public static View createPurchaseCard(Context context, ViewGroup parent, String moduleName, View.OnClickListener onPurchaseClick) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_purchase_required, parent, false);
        TextView message = view.findViewById(R.id.purchase_message);
        message.setText("Please purchase " + moduleName + " module to access it.");
        return view;
    }
}
