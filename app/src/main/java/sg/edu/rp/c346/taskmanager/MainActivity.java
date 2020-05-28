package sg.edu.rp.c346.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<Task> task;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);
        btnAdd = findViewById(R.id.btnAdd);

        final DBHelper db = new DBHelper(MainActivity.this);
        final ArrayList<Task> data = db.getAllTasks();
        db.close();

        task = new ArrayList<Task>();

        for (int i = 0; i < data.size(); i++){
            task.add(data.get(i));
        }

        final ArrayAdapter aa = new TaskArrayAdapter(MainActivity.this,R.layout.row,task);
        lv.setAdapter(aa);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(i,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                task.clear();
                DBHelper dbh = new DBHelper(MainActivity.this);
                task.addAll(dbh.getAllTasks());
                dbh.close();
                ArrayAdapter aa = new TaskArrayAdapter(MainActivity.this,R.layout.row,task);
                lv.setAdapter(aa);
            }
        }
    }
}
