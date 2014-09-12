package pl.niezapominajka.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import database.DBSingleton;
import database.SqliteController;
import task.entity.Task;

public class MainActivity extends Activity {
    // constant use in date comparator
    private final int TASK_DONE = -1;
    private final int TASK_ENDS_TODAY = 0;
    private final int TASK_NOT_DONE = 1;

    private Button btAddTask = null;
    private ListView lvTasksList = null;
    private SqliteController dbController = null;
    private  List<Task> tasksList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        btAddTask = (Button) findViewById(R.id.btAddTask);
        lvTasksList = (ListView) findViewById(R.id.listOfTasks);
        dbController = DBSingleton.getInstance(this);

        btAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("pl.niezapominajka.app.ADD_TASK"));
            }
        });

        lvTasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = tasksList.get(position);

                String text = task.getDay() + "/" + task.getMonth() + "/" + task.getYear();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        lvTasksList.setLongClickable(true);
        lvTasksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedTaskId = tasksList.get(position).getTaskId();

                Intent updateActivity = new Intent("pl.niezapominajka.app.UPDATE_TASK");
                updateActivity.putExtra("TASK_ID", selectedTaskId);

                startActivity(updateActivity);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // refresh listView
        tasksList = dbController.getAllTasks();
        ArrayAdapter adapter = new TasksAdapter();
        lvTasksList.setAdapter(adapter);
    }

    private class TasksAdapter extends ArrayAdapter {
        public TasksAdapter() {
            super(MainActivity.this, R.layout.task_layout, tasksList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int checkedDate;
            View itemView = convertView;

            if(itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.task_layout, parent, false);
            }

            Task currentTask = tasksList.get(position);
            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText(currentTask.getTaskName());

            TextView description = (TextView) itemView.findViewById(R.id.description);
            description.setText(currentTask.getTaskDesc());

            checkedDate = compareDate(currentTask);
            switch (checkedDate) {
                case TASK_ENDS_TODAY : // red color
                    title.setTextColor(Color.RED);
                    title.setPaintFlags(0);
                    description.setTextColor(Color.RED);
                    description.setPaintFlags(0);
                    itemView.setBackgroundColor(Color.WHITE);
                    break;
                case TASK_NOT_DONE : // white color
                    title.setTextColor(Color.BLACK);
                    title.setPaintFlags(0);
                    description.setTextColor(Color.GRAY);
                    description.setPaintFlags(0);
                    itemView.setBackgroundColor(Color.WHITE);
                    break;
                case TASK_DONE : // grey color
                    title.setTextColor(Color.GRAY);
                    title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    description.setTextColor(Color.GRAY);
                    description.setPaintFlags(description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    itemView.setBackgroundColor(Color.LTGRAY);
                    break;
            }

            return itemView;
        }
    }

    private int compareDate(Task t) {
        int day, month, year;
        int tday, tmonth, tyear;
        Calendar c = Calendar.getInstance();

        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1; // months are count from 0..11
        year = c.get(Calendar.YEAR);

        tday = t.getDay();
        tmonth = t.getMonth();
        tyear = t.getYear();

        if (year < tyear)
            return TASK_NOT_DONE;
        else if(year > tyear)
            return TASK_DONE;
        else {
            if (month < tmonth)
                return TASK_NOT_DONE;
            else if (month > tmonth)
                return TASK_DONE;
            else {
                if (day < tday)
                    return TASK_NOT_DONE;
                else if (day > tday)
                    return TASK_DONE;
                else
                    return TASK_ENDS_TODAY;
            }
        }
    }
}
