package alborz.rad.todo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import alborz.rad.todo.Adapter.ToDoAdapter;
import alborz.rad.todo.Model.ToDoModel;
import alborz.rad.todo.R;
import alborz.rad.todo.Utils.AddNewTask;
import alborz.rad.todo.Utils.DataBaseHelper;
import alborz.rad.todo.Utils.DialogClose;

public class MainActivity extends AppCompatActivity implements DialogClose {

    private RecyclerView recyclerView;
    private ToDoAdapter adapter;
    private List<ToDoModel> toDoList;
    private FloatingActionButton addNewTaskBt;
    private DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recyclerView = findViewById(R.id.recyclerview);
        toDoList = new ArrayList<>();
        db = new DataBaseHelper(this);
        db.openDatabase();
        adapter = new ToDoAdapter(this,db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        addNewTaskBt = findViewById(R.id.AddNewTaskButton);
        toDoList = db.getAllTask();
        adapter.setToDoList(toDoList);
        addNewTaskBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newObject().show(getSupportFragmentManager(),"");
            }
        });
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if(direction == ItemTouchHelper.RIGHT) {
                    adapter.deleteItem(position);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        toDoList = db.getAllTask();
        adapter.setToDoList(toDoList);
        adapter.notifyDataSetChanged();
    }
}