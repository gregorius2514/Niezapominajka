package pl.niezapominajka.app;

import android.view.View;
import android.widget.EditText;

class FocusListener implements View.OnFocusChangeListener {
    private String oldText = null;

    /*
    metoda jest wywolywana gdy view wchodzi w stan focus oraz jest wywolywana
    gdy opuszcza ten stan.

    1. poprzedni tekst jest przechowywany i czyszczone pole tekstowe
    2. podaczas utraty focus (i pusty tekst) zmienna oldText byla od nowa inicjalizowana
    pusta wartoscia i to byl problem
     */
    @Override
    public void onFocusChange (View view, boolean hasFocus) {
        EditText et = (EditText) view;

        if (!hasFocus ) {

            if (et.getText().toString().length() == 0)
                et.setText (oldText);

        } else {
            oldText = et.getText().toString();
            et.getText().clear();
        }
    }
}