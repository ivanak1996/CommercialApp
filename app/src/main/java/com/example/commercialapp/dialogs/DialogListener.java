package com.example.commercialapp.dialogs;

import androidx.fragment.app.DialogFragment;

public interface DialogListener {
    void onDialogPositiveClick(DialogFragment dialog);
    void onDialogNegativeClick(DialogFragment dialog);
}
