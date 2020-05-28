package sg.edu.rp.c346.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    int reqCode = 12345;
    Button btnAddTask,btnCancel;
    EditText etName, etDesc, etRemind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        btnAddTask = findViewById(R.id.btnAddTask);
        btnCancel = findViewById(R.id.btnCancel);
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        etRemind = findViewById(R.id.etRemind);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send EditText Data to Broadcast
                Intent i = new Intent(AddTaskActivity.this, ScheduledNotificationReceiver.class);
                i.putExtra("name", etName.getText().toString());
                i.putExtra("desc", etDesc.getText().toString());

                //Display Notification
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, Integer.parseInt(etRemind.getText().toString()));

                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTaskActivity.this,reqCode,i,PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);

                //Vibrate
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                }else{
                    v.vibrate(500);
                }

                //Insert to Database
                DBHelper db = new DBHelper(AddTaskActivity.this);
                String newName = etName.getText().toString();
                String newDesc = etDesc.getText().toString();
                db.insertTask(newName,newDesc);
                setResult(RESULT_OK);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
