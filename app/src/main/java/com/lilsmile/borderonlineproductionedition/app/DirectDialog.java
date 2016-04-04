package com.lilsmile.borderonlineproductionedition.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Smile on 16.01.16.
 */
public class DirectDialog extends DialogFragment {

    public interface OnDialogFinished {
        void onLeftArrowClick();
        void onRightArrowClick();
    }

    String[] countries;

    OnDialogFinished listener;

    DirectDialog(String[] countries)
    {
       this.countries = countries;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction


        listener = (OnDialogFinished)getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.direct_dialog, null);
        TextView forwardFrom, forwardTo, backwardFrom, backwardTo;
        forwardFrom = (TextView) view.findViewById(R.id.textViewForwardFrom);
        forwardTo = (TextView) view.findViewById(R.id.textViewForwardTo);
        backwardFrom = (TextView)view.findViewById(R.id.textViewBackwardFrom);
        backwardTo = (TextView) view.findViewById(R.id.textViewBackwardTo);
        forwardFrom.setText(countries[0]);
        forwardTo.setText(countries[1]);
        backwardFrom.setText(countries[1]);
        backwardTo.setText(countries[0]);


        LinearLayout forwardll, backwardll;
        forwardll = (LinearLayout) view.findViewById(R.id.llForward);
        backwardll = (LinearLayout) view.findViewById(R.id.llBackward);
        forwardll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.onRightArrowClick();
            }
        });
        backwardll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.onLeftArrowClick();
            }
        });

        builder.setView(view);

        return builder.create();
    }
}
