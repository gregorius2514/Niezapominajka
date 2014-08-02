package Tests;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

import java.util.List;

import database.SqliteController;
import task.entity.Task;

public class DBTestSuite extends InstrumentationTestCase {
     /*    SqliteController dbController = new SqliteController(getInstrumentation().getContext().getResources().getXml());

        // new Tasks for testing
        Task t1 = new Task (7, "Dokonczyc projekt", "Aplikacja niezapominajka", 12, 12, 2014);

        dbController.add (t1);

        List<Task> tasks = dbController.getAllTasks ();

       for  (Task t : tasks) {
          String log = "Name:" + t.getTaskName () + " Desc:" + t.getTaskDesc ()
                  + " Day:" + t.getDay () + " Month:" + t.getMonth ()
                  + " Year:" + t.getYear ();
        }*/

}
