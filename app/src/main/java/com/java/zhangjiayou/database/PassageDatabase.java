package com.java.zhangjiayou.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.java.zhangjiayou.util.Passage;

@Database(entities = {Passage.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class PassageDatabase extends RoomDatabase {
    private static final String DB_NAME = "PassageDatabase.db";
    private static volatile PassageDatabase instance;

    public static synchronized PassageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static PassageDatabase create(final Context context) {
        return Room.databaseBuilder(context, PassageDatabase.class, DB_NAME).build();
    }

    private static PassageDatabase createInMemory(final Context context) {
        return Room.inMemoryDatabaseBuilder(context, PassageDatabase.class).build();
    }

    public abstract PassageDAO getPassageDao();
}
