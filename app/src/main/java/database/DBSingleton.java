package database;

import android.content.Context;

public class DBSingleton {
    public static SqliteController instance = null;

    public DBSingleton() {}

    public static SqliteController getInstance(Context context) {
        if (instance == null) {
            instance = new SqliteController(context);
        }

        return instance;
    }
}
