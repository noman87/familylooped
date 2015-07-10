package com.familylooped.common.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.familylooped.R;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class AlertDialogFragment extends DialogFragment{

    private static final String MASSAGE = "massage";
    private static final String TXT_POSITIVE = "txt_positive";
    private static final String TXT_NEG = "txt_neg";
    private String mMessage, mTxtPositive, mTxtNegative;
    private  static DialogClickListener mDialogClickListener;

    public static AlertDialogFragment  newInstance(String message, String positiveButtonText, String negativeButtonTet,DialogClickListener dialogClickListener) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString(MASSAGE, message);
        args.putString(TXT_POSITIVE, positiveButtonText);
        args.putString(TXT_NEG, negativeButtonTet);
        frag.setArguments(args);
        mDialogClickListener = dialogClickListener;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mMessage = getArguments().getString(MASSAGE);
            mTxtPositive = getArguments().getString(TXT_POSITIVE);
            mTxtNegative = getArguments().getString(TXT_NEG);
        }


        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Alert!")
                .setMessage(mMessage)
                .setPositiveButton(mTxtPositive,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                mDialogClickListener.onPositiveButtonClick();
                                dismiss();

                            }
                        }
                )
                .setNegativeButton(mTxtNegative,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDialogClickListener.onDismiss();

    }
}
