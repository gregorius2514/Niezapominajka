package pl.niezapominajka.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
    private Button btDelete = null;
    private DatePicker taskDatePicker = null;
    private EditText etTaskName = null;
    private EditText etTaskDesc = null;
    private SqliteController dbController;
    private boolean saving = true;
    private Task currentTask = null;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.add_task);

        // Objects and GUI elements
        btSave = (Button) findViewById (R.id.btSaveTask);
        btDelete = (Button) findViewById (R.id.btDeleteTask);
        taskDatePicker = (DatePicker) findViewById (R.id.taskDatePicker);
        etTaskName = (EditText) findViewById (R.id.etTaskName);
        etTaskDesc = (EditText) findViewById (R.id.etTaskDescription);
        dbController = new SqliteController (getApplicationContext ());

        currentTask = new Task();
//        dbController = new SqliteController (this);

        Bundle extras = getIntent().getExtras();
        if( extras != null ) {
            int day, month, year;
            String taskName = extras.getString("TASK_NAME");
            currentTask = dbController.getTask(taskName);

            day = currentTask.getDay();
            month = currentTask.getMonth() - 1; // Monts start of 0..11
            year = currentTask.getYear();

            etTaskName.setText(currentTask.getTaskName());
            etTaskDesc.setText(currentTask.getTaskDesc());
            taskDatePicker.updateDate(year, month, day);

            btSave.setText("Updating");
            // set updating
            saving = false;
        } else {
            btDelete.setVisibility(View.GONE);
        }


        // listeners
        btSave.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick (View view) {
                int day, month, year;

                day = taskDatePicker.getDayOfMonth();
                month = taskDatePicker.getMonth();
                year = taskDatePicker.getYear();

                currentTask.setTaskName(etTaskName.getText().toString());
                currentTask.setTaskDesc(etTaskDesc.getText().toString());
                currentTask.setDay(day);
                currentTask.setMonth(month);
                currentTask.setYear(year);

                if(saving) {
                    currentTask.setId(0);

                    if (dbController.addTask(currentTask))
                        finish();
                     else {
                        String errorMessage = "Error on write to Database.\nProbably invalid parameters";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
                    }
                } else {
                    if(dbController.updateTask(currentTask) > 0)
                        finish();
                    else {
                        String errorMessage = "Error updating task: "+ currentTask.getTaskName() +"to Database.\nProbably invalid parameters";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
                    }
                }
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dbController.deleteTask(currentTask) > 0)
                    finish();
                else {
                    String errorMessage = "Error on deleting task: "+ currentTask.getTaskName();
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
