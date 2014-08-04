package pl.niezapominajka.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void btUpdateClickEvent () {
        btUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int day, month, year;
                String taskName, taskDesc;

                taskName = etTaskName.getText().toString();
                taskDesc = etTaskDesc.getText().toString();
                day = taskDatePicker.getDayOfMonth();
                month = taskDatePicker.getMonth() + 1;
                year = taskDatePicker.getYear();

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

    private void btDeleteClickEvent () {
        btDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (dbController.deleteTask(currentTask) > 0)
                    finish();
            }
        });
    }

    private void getTaskInfo () {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int day, month, year;
            String taskName = extras.getString("TASK_NAME");
            currentTask = dbController.getTask(taskName);

            day = currentTask.getDay();
            month = currentTask.getMonth() - 1; // in datapicker month are started from 0..11
            year = currentTask.getYear();

            etTaskName.setText(currentTask.getTaskName());
            etTaskDesc.setText(currentTask.getTaskDesc());
            taskDatePicker.updateDate(year, month, day);
        }
    }
}
