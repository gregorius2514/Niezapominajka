package pl.niezapominajka.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import database.DBSingleton;
import database.SqliteController;
import task.entity.Task;


public class AddTaskActivity extends Activity {
    private Button btSave = null;
    private Button btDelete = null;
    private DatePicker taskDatePicker = null;
    private EditText etTaskName = null;
    private EditText etTaskDesc = null;
    private SqliteController dbController;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.add_task);

        btSave = (Button) findViewById (R.id.btSaveTask);
        btDelete = (Button) findViewById (R.id.btDeleteTask);
        taskDatePicker = (DatePicker) findViewById (R.id.taskDatePicker);
        etTaskName = (EditText) findViewById (R.id.etTaskName);
        etTaskDesc = (EditText) findViewById (R.id.etTaskDescription);
        dbController = DBSingleton.getInstance (this);

        btSaveClickEvent();
        etTaskName.setOnFocusChangeListener (new FocusListener (getString (R.string.task_name)));
        etTaskDesc.setOnFocusChangeListener (new FocusListener (getString (R.string.task_desc)));

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate (R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected (item);
    }

    private void btSaveClickEvent() {
        btSave.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                int day, month, year;
                String taskName, taskDesc;
                Task currentTask = null;

                taskName = etTaskName.getText().toString();
                taskDesc = etTaskDesc.getText().toString();
                day = taskDatePicker.getDayOfMonth();
                month = taskDatePicker.getMonth() + 1; // months starts of 0..11
                year = taskDatePicker.getYear();

                if (taskName.length() == 0) {
                    String error_message_name = getString(R.string.task_name_error);
                    Toast.makeText(getApplicationContext(), error_message_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (taskDesc.length() == 0) {
                    String error_message_desc = getString(R.string.task_desc_error);
                    Toast.makeText(getApplicationContext(), error_message_desc, Toast.LENGTH_SHORT).show();
                    return;
                }

                currentTask = new Task(0, taskName, taskDesc, day, month, year);

                if (dbController.addTask(currentTask))
                    finish();
                else {
                    String errorMessage = "Error on write to Database. \nProbably invalid parameters";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
                }
            }
        });
    }
}
