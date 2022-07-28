package alborz.rad.todo.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alborz.rad.todo.Model.ToDoModel;
import alborz.rad.todo.R;
import alborz.rad.todo.Utils.DataBaseHelper;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> toDoList;
    private Activity activity;
    private DataBaseHelper db;
    public ToDoAdapter(Activity activity,DataBaseHelper db) {
        this.activity = activity;
        this.db = db;
    }
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_task,parent,false);
        return new ToDoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoAdapter.ViewHolder holder, int position) {
        ToDoModel task = toDoList.get(position);
        holder.task.setText(task.getTask());
        holder.task.setChecked(isChecked(task.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    db.updateStatus(task.getId(),1);
                } else {
                    db.updateStatus(task.getId(),0);
                }
            }
        });
    }

    public boolean isChecked(int n) {
        return n!=0;
    }
    public void deleteItem(int position) {
        ToDoModel task = toDoList.get(position);
        db.deleteTask(task.getId());
        toDoList.remove(position);
        notifyItemRemoved(position);
    }
    public void setToDoList(List<ToDoModel> toDoList) {
        this.toDoList = toDoList;
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        public ViewHolder(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.checkbox);
        }
    }
}

