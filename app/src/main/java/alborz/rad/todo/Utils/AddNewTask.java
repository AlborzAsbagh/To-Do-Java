package alborz.rad.todo.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import alborz.rad.todo.Model.ToDoModel;
import alborz.rad.todo.R;

public class AddNewTask extends AppCompatDialogFragment {
    private DataBaseHelper db;
    private EditText editText;
    private ToDoModel task;

    public static AddNewTask newObject() {
        return new AddNewTask();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.new_task_addition,null,false);
        db = new DataBaseHelper(getActivity());
        db.openDatabase();
        editText = view.findViewById(R.id.newTaskInput);
        builder.setView(view);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog!=null) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(!editText.getText().toString().equals("")) {
                       task = new ToDoModel();
                       task.setTask(editText.getText().toString());
                       task.setStatus(0);
                       db.insertTask(task);
                       dialog.dismiss();
                   }
                   else {
                       Toast.makeText(getActivity(),"Field Is Empty",Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogClose)
            ((DialogClose)activity).handleDialogClose(dialog);
    }
}

