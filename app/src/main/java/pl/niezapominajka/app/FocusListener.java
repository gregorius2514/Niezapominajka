package pl.niezapominajka.app;

import android.view.View;
import android.widget.EditText;

class FocusListener implements View.OnFocusChangeListener {
    private String defaultText = null;

    FocusListener(String defaultText) {
        this.defaultText = defaultText;
    }

    /* Method clean EditText field on click and restore previosly write value to EditText
         if new write text was empty */
    @Override
    public void onFocusChange (View view, boolean hasFocus) {
        EditText et = (EditText) view;

        if (et.getText().toString().equalsIgnoreCase(defaultText))
            et.getText().clear();
    }
}