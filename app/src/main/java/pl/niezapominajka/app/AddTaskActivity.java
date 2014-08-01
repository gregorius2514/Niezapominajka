package pl.niezapominajka.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import database.SqliteController;
import task.entity.Task;


public class AddTaskActivity extends Activity {
    // ------------ ATTRIBUTES
    private Button btSave = null;
    private DatePicker taskDatePicker = null;
    private EditText etTaskName = null;
    private EditText etTaskDesc = null;
    private SqliteController dbController;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.add_task);

        // Objects and GUI elements
        btSave = (Button) findViewById (R.id.btSaveTask);
        taskDatePicker = (DatePicker) findViewById (R.id.taskDatePicker);
        etTaskName = (EditText) findViewById (R.id.etTaskName);
        etTaskDesc = (EditText) findViewById (R.id.etTaskDescription);
        dbController = new SqliteController (getApplicationContext ());
//        dbController = new SqliteController (this);

        // listeners
        btSave.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick (View view) {
                Task newTask = null;
                int day, month, year;

                day = taskDatePicker.getDayOfMonth ();
                month = taskDatePicker.getMonth ();
                year = taskDatePicker.getYear ();

                newTask = new Task (0, etTaskName.getText ().toString ()
                        , etTaskDesc.getText ().toString (), day, month, year);

                if ( dbController.addTask (newTask) ) {
//                    startActivity (new Intent (getApplicationContext (), pl.niezapominajka.app.MainActivity.class));
                    finish();
                } else {
                    String errorMessage = "Error on write to Database.\nProbably invalid parameters";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
                }
            }
        });

        etTaskName.setOnFocusChangeListener (new FocusListener());
        etTaskDesc.setOnFocusChangeListener (new FocusListener());

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();
        if  (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected (item);
    }

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

} // AddTaskActivity
