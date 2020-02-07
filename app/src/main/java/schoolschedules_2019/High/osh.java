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

public class osh extends AppCompatActivity {

    // Global Refernece to DB
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Fetch analyzer class
    Analyzer analyzer = new Analyzer();

    // School class name preference (hour/block/period)
    public static final String PREF = "Hour ";

    // School Start and School End times
    public static final String ENDOFDAY = "14:00:00";
    public static final String STARTOFDAY = "07:30:00";

    // School Schedule
    //--------------------------------
    String hour0End;
    String hour0Start;
    String hour1Start;
    String hour1End;
    String hour2Start;
    String hour2End;
    String hour3Start;
    String hour3End;
    String hour4Start;
    String hour4End;
    String hour5Start;
    String hour5End;
    String hour6Start;
    String hour6End;
    String foundationStart = null;
    String foundationsEnd = null;
    String dayOfTheWeek;
    String activityStart,activityEnd;

    // Lunches (must be in 00:00:00 format)
    String lunchAStart,lunchBStart,lunchCStart,lunchDStart,lunchAEnd,lunchBEnd,lunchCEnd,lunchDEnd;

    // Other Globals
    String hour = null;

    Date x = null;
    boolean lunch=false;
    boolean adjustedSchedule;

    // Set textView to current day of the week.
    TextView dayTextview;
    TextView currentDaySchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mgsh_osh);

        startup();

        // New thread // Auto update Time
        Thread t = new Thread()
        {
            public void run()
            {
                while(!isInterrupted())
                {
                    try{
                        Thread.sleep(500); // was 1000
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                // Math Clock Update and Class checker
                                whatHourIsIt(analyzer.getCurrentTime(),dayOfTheWeek);
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

    private void startup()
    {
        // Set School image
        ImageView headerImage = (ImageView) findViewById(R.id.imageView);
        headerImage.setImageResource(R.drawable.osh);
        headerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);


        // Set TextViews
        dayTextview = (TextView) findViewById(R.id.day);
        currentDaySchedule = (TextView) findViewById(R.id.currentDaySchedule);

        dayOfTheWeek=analyzer.getDayOfWeek();

        // Check for and adjusted schedule

        printSchedule(dayOfTheWeek);
        // No School in Session
        if(analyzer.schoolInSession(analyzer.getCurrentDate())==false)
        {
            dayTextview.setText("No School");
        }
        else // School in session
        {
            dayTextview.setText(dayOfTheWeek);
        }

        // Check for and adjusted schedule
        checkAlternateSchedule();
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

    // Calulates the current day of the week, and returns it as a string
    private void printSchedule(String dayOfTheWeek)
    {

        // Set textviews
        TextView hour1TextView = (TextView)  findViewById(R.id.hour1Dislpay);
        TextView hour2TextView = (TextView)  findViewById(R.id.hour2Display);
        TextView hour3TextView = (TextView)  findViewById(R.id.hour3Display);
        TextView hour4TextView = (TextView)  findViewById(R.id.hour4Display);
        TextView hour5TextView = (TextView)  findViewById(R.id.hour5Display);
        TextView hour6TextView = (TextView)  findViewById(R.id.hour6Display);

        // Lunches
        lunchAStart="10:48:00"; lunchAEnd="11:15:00";
        lunchBStart="11:15:00"; lunchBEnd="11:42:00";
        lunchCStart="11:42:00"; lunchCEnd="12:09:00";
        lunchDStart="12:09:00"; lunchDEnd="12:36:00";

        // Select the schedule based on the day of the week
        if(dayOfTheWeek.equals("Tuesday"))
        {
            //Tuesday Scheudle
            hour0Start="07:30:00"; // zero hour
            hour0End="08:00:00";
            hour1TextView.setText("Zero Hour: " + "7:30-8:00");

            hour1Start="08:07:00"; // hour 1
            hour1End="09:24:00";
            hour2TextView.setText(PREF+"1 " + "8:07-9:24");

            hour2Start="09:31:00"; // hour 2
            hour2End="10:48:00";
            hour3TextView.setText(PREF+"2 " + "9:31-10:48");

            hour4Start="10:55:00"; // hour 4
            hour4End="12:36:00";
            hour4TextView.setText(PREF+"4 " + "10:55-12:36");

            hour5Start="12:43:00";
            hour5End="14:00:00";
            hour5TextView.setText(PREF+"5 " + "12:43-2:00");


        }
        else if(dayOfTheWeek.equals("Alternate"))
        {
            //Monday-Friday Scheudle
            hour1Start="07:30:00";
            hour1End="08:13:00";
            hour1TextView.setText(PREF+":1 " + "7:30-8:13");
            hour2Start="08:20:00";
            hour2End="09:03:00";
            hour2TextView.setText(PREF+":2 " + "8:20-9:03");
            hour3Start="9:10:00";
            hour3End="09:53:00";
            hour3TextView.setText(PREF+":3 " + "9:10-09:53");
            hour4Start="10:00:00";
            hour4End="11:39:00";
            hour4TextView.setText(PREF+":4 " + "9:53-11:34");
            hour5Start="11:41:00";
            hour5End="12:23:00"; // 24 hour time here?
            hour5TextView.setText(PREF+":5 " + "11:41-12:23");
            hour6Start="12:30:00";
            hour6End="13:13:00";
            hour6TextView.setText(PREF+":6 " + "12:30-1:13"+"\n\nActivity 1:20-2:00");
            activityStart="13:20:00";
            activityEnd="14:00:00";
        }
        else if(dayOfTheWeek.equals("Monday") || dayOfTheWeek.equals("Friday"))
        {
            //Monday-Friday Scheudle
            hour1Start="07:30:00";
            hour1End="08:21:00";
            hour1TextView.setText(PREF+ "1 " + "7:30-8:21");

            hour2Start="08:28:00";
            hour2End="09:19:00";
            hour2TextView.setText(PREF+ "2 " + "8:28-9:19");

            hour3Start="9:26:00";
            hour3End="10:17:00";
            hour3TextView.setText(PREF+ "3 " + "9:26-10:17");

            hour4Start="10:24:00";
            hour4End="12:05:00";
            hour4TextView.setText(PREF+ "4 " + "10:24-12:05");

            hour5Start="12:12:00";
            hour5End="13:03:00"; // 24 hour time here?
            hour5TextView.setText(PREF+ "5 " + "12:12-1:03");

            hour6Start="13:10:00";
            hour6End="14:00:00";
            hour6TextView.setText(PREF+ "6 " + "1:10-2:00");


        }
        else if(dayOfTheWeek.equals("Wednesday"))
        {
            hour1Start="07:30:00";
            hour1End="08:47:00";
            hour1TextView.setText(PREF+ "1 " + "7:30-8:47");

            foundationStart="8:54:00";
            foundationsEnd="9:24:00";
            hour2TextView.setText("Advisory " + "8:54-9:24");

            hour3Start="9:31:00";
            hour3End="10:48:00";
            hour3TextView.setText(PREF+ "3 " + "9:31-10:48");

            hour4Start="10:55:00";
            hour4End="12:36:00";
            hour4TextView.setText(PREF+ "4 " + "10:55-12:36");

            hour6Start="12:43:00";
            hour6End="14:00:00";
            hour5TextView.setText(PREF+ "6 " + "12:43-2:00");

        }
        else
        {
            //Thrusday
            hour0Start="07:30:00";
            hour0End="08:00:00";
            hour1TextView.setText("Zero Hour " + "7:30-8:00");

            hour2Start="08:07:00";
            hour2End="09:24:00";
            hour2TextView.setText(PREF+"2 " + "8:07-9:24");

            hour3Start="09:31:00";
            hour3End="10:48:00";
            hour3TextView.setText(PREF+ "3 " + "9:31-10:48");

            hour5Start="10:55:00";
            hour5End="12:36:00"; // 24 hour time here?
            hour4TextView.setText(PREF+ "5 " + "10:55-12:36");

            hour6Start="12:43:00";
            hour6End="14:00:00"; // 24 hour time here?
            hour5TextView.setText(PREF+ "6 " + "12:43-2:00");
        }

        // Show only active class if preference is set
        if(MainActivity.showOnlyActiveClass==true)
        {
            hour1TextView.setText(null); hour2TextView.setText(null); hour3TextView.setText(null);
            hour4TextView.setText(null); hour5TextView.setText(null); hour6TextView.setText(null);
            currentDaySchedule.setText(null);
        }
    }

    // This figures out which lunch period it is.
    private void checkLunches(int period)
    {


        if(dayOfTheWeek.equals("Monday") || (dayOfTheWeek.equals("Friday")))
        {
            // Lunches
            lunchAStart="10:17:00"; lunchAEnd="10:44:00";
            lunchBStart="10:44:00"; lunchBEnd="11:11:00";
            lunchCStart="11:11:00"; lunchCEnd="11:38:00";
            lunchDStart="11:38:00"; lunchDEnd="12:05:00";
        }
        else if (dayOfTheWeek.equals("Alternate"))
        {
            lunchAStart="09:53:00"; lunchAEnd="10:20:00";
            lunchBStart="10:20:00"; lunchBEnd="10:47:00";
            lunchCStart="10:47:00"; lunchCEnd="11:19:00";
            lunchDStart="11:21:00"; lunchDEnd="11:41:00";
        }
        else
        {
            // Lunches
            lunchAStart="10:48:00"; lunchAEnd="11:15:00";
            lunchBStart="11:15:00"; lunchBEnd="11:42:00";
            lunchCStart="11:42:00"; lunchCEnd="12:09:00";
            lunchDStart="12:09:00"; lunchDEnd="12:36:00";
        }

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
        else if(x.after(analyzer.analizeTime(lunchDStart)) && x.before(analyzer.analizeTime(lunchDEnd)))
        {
            hour = PREF +" "+period+" (Lunch D)";
            lunch=true;
        }
        else
        {
            hour = PREF +" "+period;
            lunch=false;
        }
    }

    private void whatHourIsIt(String currentTime, String dayOfTheWeek)
    {
        try {
            // Current TIME
            Date d = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendarZ = Calendar.getInstance();
            calendarZ.setTime(d);
            //calendarZ.add(Calendar.DATE, 1);

            x = calendarZ.getTime();

            if (dayOfTheWeek.equals("Monday") || dayOfTheWeek.equals("Friday"))
            {
                //Hour 1 check
                if(x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End)))
                {
                    hour = PREF +"1";
                }
                else if (x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End)))
                {
                    hour = PREF +"2";
                }
                else if (x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End)))
                {
                    hour = PREF +"3";
                }
                else if (x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End)))
                {
                    checkLunches(4);
                }
                else if (x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End)))
                {
                    hour = PREF +"5";
                }
                else if (x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End)))
                {
                    hour = PREF +"6";
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
            if (dayOfTheWeek.equals("Alternate"))
            {
                //Hour 1 check
                if(x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End)))
                {
                    hour = PREF +" 1";
                }
                else if (x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End)))
                {
                    hour = PREF +" 2";
                }
                else if (x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End)))
                {
                    hour = PREF +" 3";
                }
                else if (x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End)))
                {
                    checkLunches(4);
                }
                else if (x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End)))
                {
                    lunch=false;
                    hour = PREF +" 5";
                }
                else if (x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End)))
                {
                    hour = PREF +" 6";
                }
                else if(x.after(analyzer.analizeTime(activityStart)) && x.before(analyzer.analizeTime(activityEnd)))
                {
                    hour = "Activity";
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

            // Tuesday, Wednesday, Thrusday Scheudle
            if((dayOfTheWeek.equals("Tuesday")))
            {
                // 1 Hour
                if(x.after(analyzer.analizeTime(hour0Start)) && x.before(analyzer.analizeTime(hour0End)))
                {
                    hour = PREF +"0";
                }
                //2nd Hour
                else if(x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End)))
                {
                    hour = PREF +"1";
                }
                // 4th hour
                else if(x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End)))
                {
                    hour = PREF +"2";
                }
                // 5th hour
                else if(x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End)))
                {
                    checkLunches(4);
                }
                // 6th hour
                else if(x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End)))
                {
                    hour = PREF +"5";
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

            if((dayOfTheWeek.equals("Wednesday")))
            {
                //1st Hour
                if(x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End)))
                {
                    hour = PREF +"1";
                }
                // Foundaitons
                else if(x.after(analyzer.analizeTime(foundationStart)) && x.before(analyzer.analizeTime(foundationsEnd)))
                {
                    hour = "Advisory";
                }
                //3nd Hour
                else if(x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End)))
                {
                    hour = PREF +"3 ";
                }

                // 4th hour
                else if(x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End)))
                {
                   // hour = PREF +"4";
                    checkLunches(4);
                }

                // 6th hour
                else if(x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End)))
                {
                    hour = PREF +"6";
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

            if((dayOfTheWeek.equals("Thursday")))
            {
                // Crimson Hour
                if(x.after(analyzer.analizeTime(hour0Start)) && x.before(analyzer.analizeTime(hour0End)))
                {
                    hour = PREF +"0";
                }
                // 2nd hour
                else if(x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End)))
                {
                    hour = PREF +"2";
                }
                //3nd Hour
                else if(x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End)))
                {
                    hour = PREF +"3";
                }
                // 5th hour
                else if(x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End)) )
                {
                    checkLunches(5);
                }
                // 6th hour
                else if(x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End)))
                {
                    hour = PREF +"6";
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
            Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
        }

    }


    // Go back button - Takes user to main app screen
    public void goHome(View view)
    {
        startActivity(new Intent(osh.this,MainActivity.class));
    }

    // Checks the Day in the DB
    // Not sure if this is still needed?
    public void AlternateSchedule()
    {
        // Reference DB object
        final DatabaseReference myDB = database.getReference("OSH").child("DAY");

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
               // Log.w("HIII", "Failed to read value.", error.toException());
            }
        });
    }

    // Checks the Database to see if it needs to switch the schedule over to an adjusted one.
    // IE day of the week is monday and it needs to be a tuesday scheudle, or and activity is
    // planned for the end of the day
    public void checkAlternateSchedule()
    {
        // Reference DB object
        final DatabaseReference myDB = database.getReference("OSH").child("Adjusted");

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
                    currentDaySchedule = (TextView) findViewById(R.id.currentDaySchedule);
                    currentDaySchedule.setText("Today's Schedule");

                    // Reprint the current day's schedule
                    printSchedule(analyzer.getDayOfWeek());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
              //  Log.w("HIII", "Failed to read value.", error.toException());
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
        final DatabaseReference myDB = database.getReference("OSH").child("DAY");

        // Listen for changes
        myDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String adjustedDay = dataSnapshot.getValue(String.class);

                //Log.i("myTagz","adjustedSchedule:"+adjustedDay);

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
               // Log.w("HIII", "Failed to read value.", error.toException());
            }
        });
    }

    // Call back function
    private interface FirebaseCallback
    {
        void onCallBack(String t);
    }
}
