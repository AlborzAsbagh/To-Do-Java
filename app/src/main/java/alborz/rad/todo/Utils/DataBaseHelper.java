package alborz.rad.todo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alborz.rad.todo.Model.ToDoModel;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "ToDoTableDataBase";
    private static final String TABLE_NAME = "todo";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String ID = "id";
    private static final String CREATE_TABLE_SCRIPT =
            "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
                    + STATUS + " INTEGER)";
    private SQLiteDatabase db;
    public DataBaseHelper(Context context) {
        super(context,NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }
    public void openDatabase() {
        db = getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS,task.getStatus());
        db.insert(TABLE_NAME,null,cv);
    }

    public List<ToDoModel> getAllTask() {
        Cursor cr = null;
        List<ToDoModel> toDoModelList = new ArrayList<>();
        db.beginTransaction();
        try {
            cr = db.query(TABLE_NAME,null,null,null,null,null,null);
            if(cr!=null) {
                if(cr.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setTask(cr.getString(Math.abs(cr.getColumnIndex(TASK))));
                        task.setStatus(cr.getInt(Math.abs(cr.getColumnIndex(STATUS))));
                        task.setId(cr.getInt(Math.abs(cr.getColumnIndex(ID))));
                        toDoModelList.add(task);
                    } while (cr.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("2121", Arrays.toString(e.getStackTrace()));
        } finally {
            db.endTransaction();
            cr.close();
        }

        return toDoModelList;
    }

    public void updateStatus(int id,int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);
        db.update(TABLE_NAME,cv,ID+"=?", new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id) {
        db.delete(TABLE_NAME,ID+"=?", new String[]{String.valueOf(id)});
    }
}
