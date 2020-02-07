package schoolschedules_2019.Middle;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.osseo.zechaaron.schoolschedules_2019.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import schoolschedules_2019.Analyzer;
import schoolschedules_2019.Display;
import schoolschedules_2019.MainActivity;
import schoolschedules_2019.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Time

public class bms extends AppCompatActivity{


    //Monday-Friday Schedule
    String hour1Start="08:10:00"; // Advisory
    String hour1End="08:58:00";

    String hour2Start="09:02:00";
    String hour2End="09:50:00";

    String hour3Start="09:54:00";
    String hour3End="10:42:00";

    String hour4Start="10:46:00"; // Period 4 2019-20:SY
    String hour4End="11:34:00";

    String hour5Start="11:34:00"; // Period 5 / Lunch Schedule
    String hour5End="12:56:00";

    String hour6Start="13:00:00"; // Period 6 2019-20:SY
    String hour6End="13:48:00";

    String hour7Start="13:52:00"; // Period 7 2019-20:SY
    String hour7End="14:40:00";
    String lunchAStart,lunchAEnd,lunchBStart,lunchBEnd,lunchCStart,lunchCEnd;

    String currentTime = null;
    String hour = null;

    boolean lunch=false;

    String bmsDay = "Z";

    String fullSchedule = null;

    // Fetch analyzer class
    Analyzer analyzer = new Analyzer();
    Display display = new Display();

    // Time Start and End times
    public static final String STARTOFDAY = "08:10:00";
    public static final String ENDOFDAY = "14:40:00";

    // School name preference hour/block/period
    public static final String PREF = "Period";

    TextView dailySchedule;

    Date x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        startUp();

        // New thread // Auto update Time
        Thread t = new Thread()
        {
            public void run()
            {
                while(!isInterrupted())
                {
                    try{
                        Thread.sleep(500);
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                // Math Clock Update and Class checker
                                whatHourIsIt(analyzer.getCurrentTime());
                                // Update Display Clock
                                display(analyzer.upDateDislayTime());
                            }
                        });
                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        };
        t.start();
    }

    // Calulates the current day of the week, and returns it as a string
    private void startUp()
    {
        // Set School image
        ImageView headerImage = (ImageView) findViewById(R.id.imageView);
        headerImage.setImageResource(R.drawable.bms);
        headerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        printSchedule();

        WriteTODB();

        TextView dayTextview = (TextView) findViewById(R.id.day);
        // No School in Session
        if(analyzer.schoolInSession(analyzer.getCurrentDate())==false)
        {
            dayTextview.setText("No School");
        }
        else // School in session
        {
            dayTextview.setText(analyzer.getDayOfWeek());
        }
    }

    // Go back button - Takes user to main app screen
    public void goHome(View view)
    {
        startActivity(new Intent(bms.this,MainActivity.class));
    }

    private void printSchedule()
    {
        // Set textviews
        TextView hour1TextView = (TextView)  findViewById(R.id.hour1Dislpay);
        TextView hour2TextView = (TextView)  findViewById(R.id.hour2Display);
        TextView hour3TextView = (TextView)  findViewById(R.id.hour3Display);
        TextView hour4TextView = (TextView)  findViewById(R.id.hour4Display);
        TextView hour5TextView = (TextView)  findViewById(R.id.hour5Display);
        TextView hour6TextView = (TextView)  findViewById(R.id.hour6Display);
        TextView hour7TextView = (TextView)  findViewById(R.id.hour7Display);

        // Display on Screen text, cannot use other variables due to computer time format 00:00
        hour1TextView.setText(PREF +" 1" + " 8:10-8:58");
        hour2TextView.setText(PREF+ " 2" + " 9:02-9:50");
        hour3TextView.setText(PREF +" 3"+ " 9:54-10:42");
        hour4TextView.setText(PREF +" 4"+ " 10:46-11:34");
        hour5TextView.setText(PREF +" 5"+ " 11:34-12:56");
        hour6TextView.setText(PREF +" 6"+" 1:00-1:48");
        hour7TextView.setText(PREF +" 7"+ " 1:52-2:40");

        // Show only active class if preference is set
        if(MainActivity.showOnlyActiveClass==true)
        {
            hour1TextView.setText(null); hour2TextView.setText(null); hour3TextView.setText(null);
            hour4TextView.setText(null); hour5TextView.setText(null); hour6TextView.setText(null);
            hour7TextView.setText(null);
            dailySchedule = findViewById(R.id.currentDaySchedule);
            dailySchedule.setText(null);
           // Log.i("MAIN",""+ MainActivity.showOnlyActiveClass);
        }

    }

    // This figures out which lunch period it is.
    private void checkLunches(int period)
    {
        String dayOfWeek=analyzer.getDayOfWeek();

            // Lunches
            lunchAStart="11:34:00"; lunchAEnd="12:02:00";
            lunchBStart="12:02:00"; lunchBEnd="12:30:00";
            lunchCStart="12:30:00"; lunchCEnd="12:56:00";

        // Check Lunches
        if(x.after(analyzer.analizeTime(lunchAStart)) && x.before(analyzer.analizeTime(lunchAEnd)))
        {
            hour = PREF +" "+period+" (Lunch A)";
            lunch=true;

        }
        else if(x.after(analyzer.analizeTime(lunchBStart)) && x.before(analyzer.analizeTime(lunchBEnd)))
        {
            hour = PREF +" "+period+" (Lunch B)";
            lunch=true;
        }
        else if(x.after(analyzer.analizeTime(lunchCStart)) && x.before(analyzer.analizeTime(lunchCEnd)))
        {
            hour = PREF +" "+period+" (Lunch C)";
            lunch=true;
        }
        else
        {
            hour = PREF +" "+period;
            lunch=false;
        }
    }

    private void display(String currentTime) {
        TextView textView = (TextView) findViewById(R.id.current_time);
        textView.setText(currentTime);

        TextView currentClassView = (TextView) findViewById(R.id.display_text_view);
        currentClassView.setText(hour);



        // Adjust font if showOnlyActive is turned on
        if(MainActivity.showOnlyActiveClass==true)
        {
            currentClassView.setTextSize(70); // display text
            textView.setTextSize(70); // current time
            dailySchedule.setText("");
            if(lunch=true)
            {
                currentClassView.setTextSize(60);
            }  else currentClassView.setTextSize(70);

        } else currentClassView.setTextSize(40);

        // Adjust font size if lunch is happening
        if(lunch=true && MainActivity.showOnlyActiveClass==false)
        {
            currentClassView.setTextSize(33);
        } else if (lunch=false && MainActivity.showOnlyActiveClass==false) currentClassView.setTextSize(40);
    }

    private void whatHourIsIt(String currentTime)
    {

        try {
            // Current TIME
            Date d = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendarZ = Calendar.getInstance();
            calendarZ.setTime(d);

            x = calendarZ.getTime();

                //Hour 1 check
                if(x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End)))
                {
                    hour = " Period 1";
                }
                else if (x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End)))
                {
                    hour = " Period 2";
                }
                else if (x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End)))
                {
                    hour = " Period 3";
                }
                else if (x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End)))
                {
                    hour = " Period 4";
                }
                else if (x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End)))
                {
                    checkLunches(5);
                }
                else if (x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End)))
                {
                    hour = " Period 6";
                }
                else if (x.after(analyzer.analizeTime(hour7Start)) && x.before(analyzer.analizeTime(hour7End)))
                {
                    hour = " Period 7";
                }
                else if(x.after(analyzer.analizeTime(STARTOFDAY)) && x.before(analyzer.analizeTime(ENDOFDAY)))
                {
                    hour = "Passing Time";
                }
                else
                {
                    hour = "No Active Classes";
                }

            // Adjusted Schedule
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
        }
    }
    public void WriteTODB()
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // BMS
        final DatabaseReference bmsDB = database.getReference("BMS_DAY");

        bmsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
               // Log.d("HIII", "BMS DAY is: " + value);
               // Log.d("HIII", "BMS DAY is: " + value);
                bmsDay=value;

                // Asign Schedule from DB
                TextView dailySchedule = (TextView) findViewById(R.id.currentDaySchedule);
                //dailySchedule.setText(bmsDay+" Schedule");
                dailySchedule.setText("Today's Schedule");
               // Log.d("HIII", "BMS DAY is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w("HIII", "Failed to read value.", error.toException());
            }
        });
    }
}
