package pl.niezapominajka.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.SqliteController;
import task.entity.Task;

public class MainActivity extends ActionBarActivity {
    // ------------ ATTRIBUTES
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


        // --- GUI elements
        btAddTask = (Button) findViewById(R.id.btAddTask);
        lvTasksList = (ListView) findViewById(R.id.listOfTasks);
        dbController = new SqliteController(this);

        // --- Listeners
        btAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("pl.niezapominajka.app.ADDTASK"));
//                finish();
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
//TODO refactor/cleanup code
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
            View itemView = convertView;

            if(itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.task_layout, parent, false);
            }

            Task currentTask = tasksList.get(position);

            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText(currentTask.getTaskName());

            TextView description = (TextView) itemView.findViewById(R.id.description);
            description.setText(currentTask.getTaskDesc());

            return itemView;
        }
    }
}
