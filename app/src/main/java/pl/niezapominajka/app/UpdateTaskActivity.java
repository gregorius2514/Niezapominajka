package pl.niezapominajka.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import database.DBSingleton;
import database.SqliteController;
import task.entity.Task;

public class UpdateTaskActivity extends Activity {
    private Button btUpdate = null;
    private Button btDelete = null;
    private DatePicker taskDatePicker = null;
    private EditText etTaskName = null;
    private EditText etTaskDesc = null;
    private SqliteController dbController;
    private Task currentTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.update_task);

        btUpdate = (Button) findViewById(R.id.btUpdateTask);
        btDelete = (Button) findViewById(R.id.btDeleteTask);
        taskDatePicker = (DatePicker) findViewById(R.id.taskDatePicker_update);
        etTaskName = (EditText) findViewById(R.id.etTaskName_update);
        etTaskDesc = (EditText) findViewById(R.id.etTaskDescription_update);
        dbController = DBSingleton.getInstance(this);
        currentTask = new Task();

        getTaskInfo();
        btUpdateClickEvent();
        btDeleteClickEvent();

        etTaskName.setOnFocusChangeListener(new FocusListener(getString (R.string.task_name)));
        etTaskDesc.setOnFocusChangeListener(new FocusListener(getString (R.string.task_desc)));
    }

    private void btUpdateClickEvent() {
        btUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                int day, month, year;
                String taskName, taskDesc;

                taskName = etTaskName.getText().toString();
                taskDesc = etTaskDesc.getText().toString();
                day = taskDatePicker.getDayOfMonth();
                month = taskDatePicker.getMonth() + 1;
                year = taskDatePicker.getYear();

                if (taskName.length() == 0) {
                    String error_message_name = getString (R.string.task_name_error);
                    Toast.makeText(getApplicationContext(), error_message_name, Toast.LENGTH_SHORT).show();
                    return;
                }
                
                currentTask.setTaskName(taskName);
                currentTask.setTaskDesc(taskDesc);
                currentTask.setDay(day);
                currentTask.setMonth(month);
                currentTask.setYear(year);

                if (dbController.updateTask(currentTask) > 0)
                    finish();
            }
        });
    }

    private void btDeleteClickEvent() {
        btDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                if (dbController.deleteTask(currentTask) > 0)
                    finish();
            }
        });
    }

    // retrieve pushed (selected in listView) task from main activity
    private void getTaskInfo() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int day, month, year;
            int taskId = extras.getInt("TASK_ID");
            currentTask = dbController.getTask(taskId);

            day = currentTask.getDay();
            month = currentTask.getMonth() - 1; // in datapicker month starts of 0..11
            year = currentTask.getYear();

            etTaskName.setText(currentTask.getTaskName());
            etTaskDesc.setText(currentTask.getTaskDesc());
            taskDatePicker.updateDate(year, month, day);
        }
    }
}
