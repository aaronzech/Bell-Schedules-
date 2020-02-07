package schoolschedules_2019.High;

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
import schoolschedules_2019.MainActivity;
import schoolschedules_2019.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Time

public class pcsh extends AppCompatActivity {

    // Global Refernece to DB
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Fetch analyzer class
    Analyzer analyzer = new Analyzer();

    // School-Class-Name preference (hour/block/period)
    public static final String PREF = "Hour";

    // School Start and School End times
    public static final String ENDOFDAY = "14:00:00";
    public static final String STARTOFDAY = "07:30:00";
    // School Schedule
    //--------------------------------
    String hour1Start = "00:00:00";
    String hour1End = "00:00:00";
    String hour2Start = "00:00:00";
    String hour2End = "00:00:00";
    String hour3Start = "00:00:00";
    String hour3End = "00:00:00";
    String hour4Start = "00:00:00";
    String hour4End = "00:00:00";
    String hour5Start = "00:00:00";
    String hour5End = "00:00:00";
    String hour6Start = "00:00:00";
    String hour6End = "00:00:00";
    String hour7Start = "00:00:00";
    String hour7End = "00:00:00";
    String lunchAStart, lunchAEnd, lunchBStart, lunchBEnd, lunchCStart, lunchCEnd;
    String dayOfTheWeek;
    String foundationStart = "00:00:00";
    String foundationsEnd = "00:00:00";
    String hour = null;


    boolean lunch = false;
    boolean adjustedSchedule;
    Date x = null;

    // Set textView to current day of the week.
    TextView dayTextview;
    TextView currentDaySchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mgsh_osh);

        startUp();

        // New thread // Auto update Time
        Thread t = new Thread() {
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Math Clock Update and Class checker
                                whatHourIsIt(analyzer.getCurrentTime(),dayOfTheWeek);
                                // Update Display Clock
                                display(analyzer.upDateDislayTime());
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        t.start();
    }

    private void startUp()
    {
        // Set School image
        ImageView headerImage = (ImageView) findViewById(R.id.imageView);
        headerImage.setImageResource(R.drawable.pcsh);
        headerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // Sets Textview
        dayTextview = (TextView) findViewById(R.id.day);
        currentDaySchedule = (TextView) findViewById(R.id.currentDaySchedule);

        // Sets the specifc day scheudle
        printSchedule(analyzer.getDayOfWeek());

        dayOfTheWeek=analyzer.getDayOfWeek();

        // No School in Session
        if(analyzer.schoolInSession(analyzer.getCurrentDate())==false)
        {
            dayTextview.setText("No School");
        }
        else // School in session
        {
            dayTextview.setText(analyzer.getDayOfWeek());
        }
        // Check for and adjusted schedule
        checkAlternateSchedule();
    }

    private void printSchedule(String dayOfWeek)
    {
        // Set textviews for daily scheudle
        TextView hour1TextView = (TextView) findViewById(R.id.hour1Dislpay);
        TextView hour2TextView = (TextView) findViewById(R.id.hour2Display);
        TextView hour3TextView = (TextView) findViewById(R.id.hour3Display);
        TextView hour4TextView = (TextView) findViewById(R.id.hour4Display);
        TextView hour5TextView = (TextView) findViewById(R.id.hour5Display);
        TextView hour6TextView = (TextView) findViewById(R.id.hour6Display);


        // Select the schedule based on the day of the week
        if (dayOfWeek.equals("Tuesday")) {
            //Tuesday Scheudle
            hour1Start = "07:30:00";
            hour1End = "08:38:00";
            hour1TextView.setText(PREF + " 1" + " 7:30-8:38");

            hour2Start = "08:43:00";
            hour2End = "09:51:00";
            hour2TextView.setText(PREF + " 2" + " 8:43-9:51");

            hour3Start = "09:56:00";
            hour3End = "11:04:00";
            hour3TextView.setText(PREF + " 3" + " 9:56-11:04");

            hour5Start = "11:09:00";
            hour5End = "12:47:00";
            hour4TextView.setText(PREF + " 5" + " 11:09-12:47");

            hour6Start = "12:52:00";
            hour6End = "14:00:00";
            hour5TextView.setText(PREF + " 6" + " 12:52-2:00");
            hour6TextView.setText(null);

        } else if (dayOfWeek.equals("Monday") || dayOfWeek.equals("Friday")) {
            //Monday-Friday Scheudle
            hour1Start = "07:30:00";
            hour1End = "08:17:00";
            hour1TextView.setText(PREF + " 1" + " 7:30-8:17");

            hour2Start = "08:22:00";
            hour2End = "09:09:00";
            hour2TextView.setText(PREF + " 2" + " 8:22-9:09");

            hour3Start = "09:14:00";
            hour3End = "10:01:00";
            hour3TextView.setText(PREF + " 3" + " 9:14-10:01");

            hour4Start = "10:06:00";
            hour4End = "10:53:00";
            hour4TextView.setText(PREF + " 4" + " 10:06-10:53");

            hour5Start = "10:58:00";
            hour5End = "12:16:00";
            hour5TextView.setText(PREF + " 5" + " 10:58-12:16");

            hour6Start = "12:21:00";
            hour6End = "13:08:00";
            hour6TextView.setText(PREF + " 6" + " 12:21-1:08\n\n"+PREF + " 7" + " 1:13-2:00");

            hour7Start = "13:13:00";
            hour7End = "14:00:00";


        } else if (dayOfWeek.equals("Wednesday")) {
            hour1Start = "07:30:00";
            hour1End = "08:38:00";
            hour1TextView.setText(PREF + " 1" + " 7:30-8:38");

            foundationStart = "08:43:00";
            foundationsEnd = "09:51:00";
            hour2TextView.setText("Advisory" + " 8:43-9:51");

            hour4Start = "09:56:00";
            hour4End = "11:04:00";
            hour3TextView.setText(PREF + " 4" + " 9:56-11:04");

            hour5Start = "11:09:00";
            hour5End = "12:47:00";
            hour4TextView.setText(PREF + " 5" + " 11:09-12:47");

            hour7Start = "12:52:00";
            hour7End = "14:00:00";
            hour5TextView.setText(PREF + " 7" + " 12:52-2:00");

            hour6TextView.setText(null);

        } else {
            //Thrusday
            hour2Start = "07:30:00";
            hour2End = "08:38:00";
            hour1TextView.setText(PREF + " 2" +" 7:30-8:38");

            hour3Start = "08:43:00";
            hour3End = "09:51:00";
            hour2TextView.setText(PREF + " 3" + " 8:43-9:51");

            hour4Start = "09:56:00";
            hour4End = "11:04:00";
            hour3TextView.setText(PREF + " 4" + " 9:56-11:04");

            hour6Start = "11:09:00";
            hour6End = "12:47:00";
            hour4TextView.setText(PREF + " 6" + " 11:09-12:47");

            hour7Start = "12:52:00";
            hour7End = "14:00:00";
            hour5TextView.setText(PREF + " 5" + " 12:52-2:00");
            hour6TextView.setText(null);
        }

        // Show only active class if preference is set
        if(MainActivity.showOnlyActiveClass==true)
        {
            hour1TextView.setText(null); hour2TextView.setText(null); hour3TextView.setText(null);
            hour4TextView.setText(null); hour5TextView.setText(null); hour6TextView.setText(null);
            currentDaySchedule.setText(null);
        }
    }

    // Go back button - Takes user to main app screen
    public void goHome(View view) {
        startActivity(new Intent(pcsh.this, MainActivity.class));
    }

    // Display info to the screen
    private void display(String num) {

        // dislay somthing?
        TextView textView = findViewById(R.id.current_hour);
        textView.setText(num);

        // Dislay current class to the screen
        TextView textViewNew = findViewById(R.id.display_hour);
        textViewNew.setText(hour);


        // Adjust font if showOnlyActive is turned on
        if(MainActivity.showOnlyActiveClass==true)
        {
            textViewNew.setTextSize(70); // display text
            textView.setTextSize(70); // current time

            if(lunch=true)
            {
                textViewNew.setTextSize(60);
            }  else textViewNew.setTextSize(70);

        } else textViewNew.setTextSize(40);

        // Adjust font size if lunch is happening
        if(lunch=true && MainActivity.showOnlyActiveClass==false)
        {
            textViewNew.setTextSize(33);
        }  else if (lunch=false && MainActivity.showOnlyActiveClass==false) textViewNew.setTextSize(40);
    }

    private void whatHourIsIt(String currentTime, String dayOfTheWeek) {
        try {
            // Current TIME
            Date d = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendarZ = Calendar.getInstance();
            calendarZ.setTime(d);
            //calendarZ.add(Calendar.DATE, 1);

            x = calendarZ.getTime();

            if (dayOfTheWeek.equals("Monday") || dayOfTheWeek.equals("Friday")) {
                //Hour 1 check

                if (x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End))) {
                    hour = PREF + " 1";
                } else if (x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End))) {
                    hour = PREF + " 2";
                } else if (x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End))) {
                    hour = PREF + " 3";
                } else if (x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End))) {
                    hour = PREF + " 4";
                } else if (x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End))) {
                    checkLunches(5);
                } else if (x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End))) {
                    hour = PREF + " 6";
                } else if (x.after(analyzer.analizeTime(hour7Start)) && x.before(analyzer.analizeTime(hour7End))) {
                    hour = PREF + " 7";
                } else if(x.after(analyzer.analizeTime(STARTOFDAY)) && x.before(analyzer.analizeTime(ENDOFDAY)))
                {
                    hour = "Passing Time";
                }
                else
                {
                    hour = "No Active Classes";
                }
            }

            // Tuesday, Wednesday, Thrusday Scheudle
            if ((dayOfTheWeek.equals("Tuesday"))) {

                if (x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End))) {
                    hour = PREF + " 1";
                }
                //2nd Hour
                else if (x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End))) {
                    hour = PREF + " 2";
                }
                // 4th hour
                else if (x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End))) {
                    hour = PREF + " 3";
                }
                // 5th hour
                else if (x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End))) {
                    checkLunches(5);
                }
                //Crimson Hour
                else if (x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End))) {
                    lunch = false;
                    hour = PREF + " 6";
                }
                else if(x.after(analyzer.analizeTime(STARTOFDAY)) && x.before(analyzer.analizeTime(ENDOFDAY)))
                {
                    hour = "Passing Time";
                }
                else
                {
                    hour = "No Active Classes";
                }
            }

            if (dayOfTheWeek.equals("Wednesday")) {
                //1st Hour
                if (x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End))) {
                    hour = PREF + " 1";
                }
                // Foundaitons
                else if (x.after(analyzer.analizeTime(foundationStart)) && x.before(analyzer.analizeTime(foundationsEnd))) {
                    hour = "Advisory";
                }
                // 4th hour
                else if (x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End))) {
                    hour = PREF + " 4";
                }
                // 6th hour
                else if (x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End))) {
                    checkLunches(5);
                }
                // 4th hour
                else if (x.after(analyzer.analizeTime(hour7Start)) && x.before(analyzer.analizeTime(hour7End))) {
                    lunch = false;
                    hour = PREF + " 7";
                }
                else if(x.after(analyzer.analizeTime(STARTOFDAY)) && x.before(analyzer.analizeTime(ENDOFDAY)))
                {
                    hour = "Passing Time";
                }
                else
                {
                    hour = "No Active Classes";
                }
            }

            if ((dayOfTheWeek.equals("Thursday"))) {
                // Crimson Hour
                if (x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End))) {
                    hour = PREF + " 2";
                }
                // 2nd hour
                else if (x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End))) {
                    hour = PREF + " 3";
                }
                //3nd Hour
                else if (x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End))) {
                    hour = PREF + " 4";
                }
                // 5th hour
                else if (x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End))) {
                    checkLunches(6);
                }
                // 6th hour
                else if (x.after(analyzer.analizeTime(hour7Start)) && x.before(analyzer.analizeTime(hour7End))) {
                    hour = PREF + " 7";
                }
                else if(x.after(analyzer.analizeTime(STARTOFDAY)) && x.before(analyzer.analizeTime(ENDOFDAY)))
                {
                    hour = "Passing Time";
                }
                else
                {
                    hour = "No Active Classes";
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
        }

    }


    // This figures out which lunch period it is.
    private void checkLunches(int period) {


        if (dayOfTheWeek.equals("Monday") || (dayOfTheWeek.equals("Friday"))) {
            lunchAStart = "10:53:00";
            lunchAEnd = "11:18:00";
            lunchBStart = "11:23:00";
            lunchBEnd = "11:48:00";
            lunchCStart = "11:51:00";
            lunchCEnd = "12:16:00";

        } else {
            // Lunches
            lunchAStart = "11:04:00";
            lunchAEnd = "11:34:00";
            lunchBStart = "11:39:00";
            lunchBEnd = "12:09:00";
            lunchCStart = "12:17:00";
            lunchCEnd = "12:47:00";
        }

        // Check Lunches
        if (x.after(analyzer.analizeTime(lunchAStart)) && x.before(analyzer.analizeTime(lunchAEnd))) {
            hour = PREF + " " + period + " (Lunch A)";
            lunch = true;

        } else if (x.after(analyzer.analizeTime(lunchBStart)) && x.before(analyzer.analizeTime(lunchBEnd))) {
            hour = PREF + " " + period + " (Lunch B)";
            lunch = true;
        } else if (x.after(analyzer.analizeTime(lunchCStart)) && x.before(analyzer.analizeTime(lunchCEnd))) {
            hour = PREF + " " + period + " (Lunch C)";
            lunch = true;
        } else {
            hour = PREF + " " + period;
            lunch = false;
        }
    }

    // Checks the Day in the DB
    // Not sure if this is still needed?
    public void AlternateSchedule()
    {
        // Reference DB object
        final DatabaseReference myDB = database.getReference("PCSH").child("DAY");

        // Listen for changes
        myDB.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String adjustedDay = dataSnapshot.getValue(String.class);

                if(adjustedSchedule==true)
                {
                    // Print a new schedule
                    updateAdjustedSchedule(adjustedDay);
                }
                else
                {
                    // Run original schedule
                    printSchedule(analyzer.getDayOfWeek());
                }

            }
            @Override
            public void onCancelled(DatabaseError error)
            {
                // Failed to read value
                //Log.w("HIII", "Failed to read value.", error.toException());
            }
        });
    }

    // Checks the Database to see if it needs to switch the schedule over to an adjusted one.
    // IE day of the week is monday and it needs to be a tuesday scheudle, or and activity is
    // planned for the end of the day
    public void checkAlternateSchedule()
    {
        // Reference DB object
        final DatabaseReference myDB = database.getReference("PCSH").child("Adjusted");

        // Listen for changes
        myDB.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                // Asign the DB value to this global variable
                adjustedSchedule=dataSnapshot.getValue(Boolean.class);

                // Run Alternate Schedule
                if(adjustedSchedule==true)
                {
                    AlternateSchedule();

                }
                // Run the regular schedule
                else
                {
                    // Write "today's schedule" instead of a Monday, Tuesday schedule ect
                  //  currentDaySchedule = (TextView) findViewById(R.id.currentDaySchedule);
                    currentDaySchedule.setText("Today's Schedule");

                    // Reprint the current day's schedule
                    printSchedule(analyzer.getDayOfWeek());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w("HIII", "Failed to read value.", error.toException());
            }
        });

    }

    // Updates the printed schedule to reflect changes
    private void updateAdjustedSchedule(String day)
    {
        // Callback Function to get data out of the database private thread.
        readData(new FirebaseCallback() {
            @Override
            public void onCallBack(String value) {
                dayOfTheWeek=value;
            }
        });

        // Updates the title of the scheudle for the day
        currentDaySchedule = (TextView) findViewById(R.id.currentDaySchedule);
        currentDaySchedule.setText(dayOfTheWeek + " Schedule");

        // Reprint the Schedule for the adjusted day
        printSchedule(day);
    }

    // 2nd part of the call back function
    private void readData(final FirebaseCallback firebackCallBack)
    {
        // Reference DB object
        final DatabaseReference myDB = database.getReference("PCSH").child("DAY");

        // Listen for changes
        myDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String adjustedDay = dataSnapshot.getValue(String.class);

               // Log.i("myTagz","adjustedSchedule:"+adjustedDay);

                if(adjustedSchedule==true)
                {
                    // Run function and pass the "day" grabed from the DB
                    updateAdjustedSchedule(adjustedDay);

                    // Pass the "day" outside of the async function
                    firebackCallBack.onCallBack(adjustedDay);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w("HIII", "Failed to read value.", error.toException());
            }
        });
    }

    // Call back function
    private interface FirebaseCallback
    {
        void onCallBack(String t);
    }

}
