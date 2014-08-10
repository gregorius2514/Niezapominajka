package pl.niezapominajka.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import pl.niezapominajka.app.MainActivity;
import pl.niezapominajka.app.R;

public class GuiTests extends ActivityInstrumentationTestCase2<MainActivity>{
    MainActivity activity;

    public GuiTests(Class<MainActivity> activityClass) {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    public void buttonNotNull() {
        Button b = (Button) activity.findViewById(R.id.btAddTask);
        assertNotNull(b);
    }
}
