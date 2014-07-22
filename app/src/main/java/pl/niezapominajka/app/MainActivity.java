package pl.niezapominajka.app;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.List;

import database.SqliteController;
import task.entity.Task;


public class MainActivity extends ActionBarActivity {
    // ------------ ATTRIBUTES
    private Button btDodaj = null;
    private DatePicker taskDatePicker = null;
    private SqliteController dbController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btDodaj = (Button) findViewById(R.id.btSaveTask);
        taskDatePicker = (DatePicker) findViewById(R.id.taskDatePicker);
        // listeners
        btDodaj.setOnClickListener(new ButtonListener());

        dbController = new SqliteController(getApplicationContext());

        // new Tasks for testing
        Task t1 = new Task(7, "Dokonczyc projekt", "Aplikacja niezapominajka", 12, 12, 2014);

        Log.d("Create task", "task name: " + t1.getTaskName());
        dbController.addTask(t1);

        Log.d("Reading from db", "Read all tasks");
        List<Task> tasks = dbController.getAllTasks();

       for (Task t : tasks) {
          String log = "Name:" + t.getTaskName() + " Desc:" + t.getTaskDesc()
                  + " Day:" + t.getDay() + " Month:" + t.getMonth()
                  + " Year:" + t.getYear();
            Log.d("Task: ", log);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            int day, month, year;

            day = taskDatePicker.getDayOfMonth();
            month = taskDatePicker.getMonth();
            year = taskDatePicker.getYear();

            System.out.println("Selected: \n\tDay: " + day +"\tMonth: " + month + "\tYear: " + year);
        }
    }


}
