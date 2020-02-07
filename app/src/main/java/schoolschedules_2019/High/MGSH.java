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

public class MGSH extends AppCompatActivity{

    // Global Refernece to DB
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Fetch analyzer class
    Analyzer analyzer = new Analyzer();

    // School class name preference (hour/block/period)
    public static final String PREF = "Hour";

    // School Start and School End times
    public static final String ENDOFDAY = "14:00:00";
    public static final String STARTOFDAY = "07:30:00";

    // School Schedule
    //--------------------------------
    // Classes (must be in 00:00:00 format)
    String hour1Start,hour1End;
    String hour2Start,hour2End;
    String hour3Start,hour3End;
    String hour4Start,hour4End;
    String hour5Start,hour5End;
    String hour6Start,hour6End;
    String activityStart,activityEnd;
    String crimsonHourStart,crimsonHourEnd;
    String foundationStart,foundationsEnd;

    // Lunches (must be in 00:00:00 format)
    String lunchAStart,lunchBStart,lunchCStart,lunchDStart,lunchAEnd,lunchBEnd,lunchCEnd,lunchDEnd;

    // Other Globals
    String hour = null;
    String dayOfTheWeek;
    Date x = null;
    boolean lunch=false;
    boolean adjustedSchedule;
    boolean scheduleOverride=false;

    // Set textView to current day of the week.
    TextView dayTextview;
    TextView currentDaySchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mgsh_osh);

        // Startup methods
        startUp();

        // Main Thread for calculations
        Thread t = new Thread()
        {
            public void run()
            {
                while(!isInterrupted())
                {
                    try{
                        Thread.sleep(500); // Runs every half second
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                    // Math Clock Update and Class checker
                                    if(scheduleOverride==false)whatHourIsIt(analyzer.getCurrentTime(),dayOfTheWeek);


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

    // Runs startup UI methods
    private void startUp()
    {
        // Set School image
        setSchoolImage();

        // Assign Textviews to variables
        dayTextview = (TextView) findViewById(R.id.day);
        currentDaySchedule = (TextView) findViewById(R.id.currentDaySchedule);

        // Current Date for school session
        boolean result = analyzer.schoolInSession(analyzer.getCurrentDate());

        // No School in Session
        if(analyzer.schoolInSession(analyzer.getCurrentDate())==false)
        {
            // Display no schoool
            dayTextview.setText("No School");
        }
        else // School in session
        {
            // Display the day of the week
            dayTextview.setText(analyzer.getDayOfWeek());
        }

        // get current Day of the week through the analyzer and cache it to this class
        dayOfTheWeek=analyzer.getDayOfWeek();

        // Display printed schedule
        printSchedule(dayOfTheWeek);

        // Check for alternate schedule
        checkAlternateSchedule();

    }

    // Sets the school image to the layout file
    private void setSchoolImage()
    {
        // Set School image
        ImageView headerImage = (ImageView) findViewById(R.id.imageView);
        headerImage.setImageResource(R.drawable.mgsh);
        headerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    // Calulates the current day of the week, and returns it as a string
    private void printSchedule(String dayOfTheWeek)
    {
        // Set textviews for daily scheudle
        TextView hour1TextView = (TextView)  findViewById(R.id.hour1Dislpay);
        TextView hour2TextView = (TextView)  findViewById(R.id.hour2Display);
        TextView hour3TextView = (TextView)  findViewById(R.id.hour3Display);
        TextView hour4TextView = (TextView)  findViewById(R.id.hour4Display);
        TextView hour5TextView = (TextView)  findViewById(R.id.hour5Display);
        TextView hour6TextView = (TextView)  findViewById(R.id.hour6Display);
        TextView hour7TextView = (TextView)  findViewById(R.id.hour7Display);

         String format_1 = ": 1 "; String format_2 = ": 2 "; String format_3 = ": 3 ";
         String format_4 = ": 4 "; String format_5 = ": 5 "; String format_6 = ": 6 ";


        // Select the schedule based on the day of the week
        if(dayOfTheWeek.equals("Tuesday"))
        {
            //Tuesday Scheudle
            crimsonHourStart="07:30:00";
            crimsonHourEnd="07:55:00";
            hour1TextView.setText("Crimsion Hour: " + "7:30 - 7:55");
            hour1Start="08:00:00";
            hour1End="09:20:00";
            hour2TextView.setText(PREF+" 1: " + "8:00 - 9:20");
            //Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            //hour2TextView.setTypeface(boldTypeface);
            hour2Start="09:25:00";
            hour2End="10:45:00";
            hour3TextView.setText(PREF+" 2: " + "9:25 - 10:45");
            hour4Start="10:50:00";
            hour4End="12:35:00";
            hour4TextView.setText(PREF+" 4: " + "10:50 - 12:35");
            hour5Start="12:40:00";
            hour5End="14:00:00";
            hour5TextView.setText(PREF+" 5: " + "12:40 - 2:00");
            hour6TextView.setText(null);

        }
        else if(dayOfTheWeek.equals("Monday") || dayOfTheWeek.equals("Friday"))
        {
            //Monday-Friday Scheudle
            hour1Start="07:30:00";
            hour1End="08:25:00";
            hour1TextView.setText(PREF+format_1 + "7:30 - 8:25");
            hour2Start="08:30:00";
            hour2End="09:25:00";
            hour2TextView.setText(PREF+format_2 + "8:30 - 9:25");
            hour3Start="9:30:00";
            hour3End="10:25:00";
            hour3TextView.setText(PREF+format_3 + "9:30 - 10:25");
            hour4Start="10:30:00";
            hour4End="12:00:00";
            hour4TextView.setText(PREF+format_4 + "10:30 - 12:00");
            hour5Start="12:05:00";
            hour5End="13:00:00"; // 24 hour time here?
            hour5TextView.setText(PREF+format_5 + "12:05 - 1:00");
            hour6Start="13:05:00";
            hour6End="14:00:00";
            hour6TextView.setText(PREF+format_6 + "1:05 - 2:00");

            // Lunches MON/FRI
            lunchAStart="10:25:00"; lunchAEnd="10:55:00";
            lunchBStart="10:48:00"; lunchBEnd="11:18:00";
            lunchCStart="11:11:00"; lunchCEnd="11:41:00";
            lunchDStart="11:35:00"; lunchDEnd="12:05:00";

        }
        else if(dayOfTheWeek.equals("Wednesday"))
        {
            hour1Start="07:30:00";
            hour1End="08:50:00";
            hour1TextView.setText(PREF +format_1+" 7:30 - 8:50");
            foundationStart="8:55:00";
            foundationsEnd="9:20:00";
            hour2TextView.setText("Foundations: " + " 8:55 - 9:20");
            hour3Start="9:25:00";
            hour3End="10:45:00";
            hour3TextView.setText(PREF+format_3 + " 9:25 - 10:45");
            hour4Start="10:50:00";
            hour4End="12:35:00";
            hour4TextView.setText(PREF+format_4 + " 10:50 - 12:35");
            hour6Start="12:40:00";
            hour6End="14:00:00";
            hour5TextView.setText(PREF+format_6 + " 12:40 - 2:00");
            hour6TextView.setText(null);
        }
        else if(dayOfTheWeek.equals("Alternate"))
        {
            //Monday-Friday Scheudle
            hour1Start="07:30:00";
            hour1End="08:14:00";
            hour1TextView.setText(PREF+format_1 + "7:30 - 8:14");
            hour2Start="08:19:00";
            hour2End="09:03:00";
            hour2TextView.setText(PREF+format_2 + "8:19 - 9:03");
            hour3Start="9:08:00";
            hour3End="09:51:00";
            hour3TextView.setText(PREF+format_3 + "9:08 - 09:51");
            hour4Start="09:56:00";
            hour4End="10:39:00";
            hour4TextView.setText(PREF+format_4 + "9:56 - 10:39");
            hour5Start="10:44:00";
            hour5End="12:14:00"; // 24 hour time here?
            hour5TextView.setText(PREF+format_5 + "10:44 - 12:44");
            hour6Start="12:19:00";
            hour6End="13:03:00";
            hour6TextView.setText(PREF+format_6 + "12:19 - 1:03"+"\n\nActivity: 1:10 - 2:00");
            activityStart="13:10:00";
            activityEnd="14:00:00";
        }
        else if (dayOfTheWeek.equals("NULL"))
        {
            hour1TextView.setText(null);
            hour2TextView.setText(null);
            hour3TextView.setText(null);
            hour4TextView.setText(null);
            hour5TextView.setText(null);
            hour6TextView.setText(null);

        }
        else
        {
            //Thrusday
            crimsonHourStart="07:30:00";
            crimsonHourEnd="07:55:00";
            hour1TextView.setText("Crimsion Hour: " + "7:30 - 7:55");
            hour2Start="08:00:00";
            hour2End="09:20:00";
            hour2TextView.setText(PREF+format_2 + "8:00 - 9:20");
            hour3Start="09:25:00";
            hour3End="10:45:00";
            hour3TextView.setText(PREF+format_3 + "9:25 - 10:45");
            hour5Start="10:50:00";
            hour5End="12:35:00";
            hour4TextView.setText(PREF+format_5 + "10:50 - 12:35");
            hour6Start="12:40:00";
            hour6End="14:00:00"; // 24 hour time here?
            hour5TextView.setText(PREF+format_6 + "12:40 - 2:00");
            hour6TextView.setText(null);
        }

        // Show only active class if preference is set
        if(MainActivity.showOnlyActiveClass==true)
        {
            // Hide Textview
            hour1TextView.setText(null); hour2TextView.setText(null); hour3TextView.setText(null);
            hour4TextView.setText(null); hour5TextView.setText(null); hour6TextView.setText(null);
            currentDaySchedule.setText(null);

            // Enlarge textviews

        }
    }

    // Go back button - Takes user to main app screen
    public void goHome(View view)
    {
        startActivity(new Intent(MGSH.this,MainActivity.class));
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
                    hour = PREF +" 4";
                }
                else if (x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End)))
                {
                    checkLunches(5);
                }
                else if (x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End)))
                {
                    lunch=false;
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


            if((dayOfTheWeek.equals("Tuesday")))
            {
                //Crimson Hour
                if(x.after(analyzer.analizeTime(crimsonHourStart)) && x.before(analyzer.analizeTime(crimsonHourEnd)))
                {
                    hour = "Crimson Hour";
                }
                // 1 Hour
                else if(x.after(analyzer.analizeTime(hour1Start)) && x.before(analyzer.analizeTime(hour1End)))
                {
                    hour = PREF +" 1";
                }
                //2nd Hour
                else if(x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End)))
                {
                    hour = PREF +" 2";
                }
                // 4th hour
                else if(x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End)))
                {
                    checkLunches(4);
                }
                // 5th hour
                else if(x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End)))
                {
                    lunch=false;
                    hour = PREF +" 5";
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
                    hour = PREF +" 1";
                }
                // Foundaitons
                else if(x.after(analyzer.analizeTime(foundationStart)) && x.before(analyzer.analizeTime(foundationsEnd)))
                {
                    hour = "Foundations";
                }
                //3nd Hour
                else if(x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End)))
                {
                    hour = PREF +" 3";
                }

                // 4th hour
                else if(x.after(analyzer.analizeTime(hour4Start)) && x.before(analyzer.analizeTime(hour4End)))
                {
                    checkLunches(4);
                }

                // 6th hour
                else if(x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End)))
                {
                    lunch=false;
                    hour = PREF +" 6";
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
                if(x.after(analyzer.analizeTime(crimsonHourStart)) && x.before(analyzer.analizeTime(crimsonHourEnd)))
                {
                    hour = "Crimson Hour";
                }
                // 2nd hour
                else if(x.after(analyzer.analizeTime(hour2Start)) && x.before(analyzer.analizeTime(hour2End)))
                {
                    hour = PREF +" 2";
                }
                //3nd Hour
                else if(x.after(analyzer.analizeTime(hour3Start)) && x.before(analyzer.analizeTime(hour3End)))
                {
                    hour = PREF +" 3";
                }
                // 5th hour
                else if(x.after(analyzer.analizeTime(hour5Start)) && x.before(analyzer.analizeTime(hour5End)) )
                {

                    checkLunches(5);
                }
                // 6th hour
                else if(x.after(analyzer.analizeTime(hour6Start)) && x.before(analyzer.analizeTime(hour6End)))
                {
                    lunch=false;
                    hour = "6th Hour";
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

    // This figures out which lunch period it is.
    private void checkLunches(int period)
    {
      // Log.i("LUNCH",dayOfTheWeek);

        if(dayOfTheWeek.equals("Monday") || (dayOfTheWeek.equals("Friday")))
        {
            lunchAStart="10:25:00"; lunchAEnd="10:55:00";
            lunchBStart="10:48:00"; lunchBEnd="11:18:00";
            lunchCStart="11:11:00"; lunchCEnd="11:41:00";
            lunchDStart="11:35:00"; lunchDEnd="12:05:00";
        }
        else if (dayOfTheWeek.equals("Alternate"))
        {
            lunchAStart="10:39:00"; lunchAEnd="11:02:00";
            lunchBStart="11:02:00"; lunchBEnd="11:25:00";
            lunchCStart="11:25:00"; lunchCEnd="11:49:00";
            lunchDStart="11:49:00"; lunchDEnd="12:19:00";
        }
        else
        {
            // Lunches
            lunchAStart="10:45:00"; lunchAEnd="11:15:00";
            lunchBStart="11:15:00"; lunchBEnd="11:45:00";
            lunchCStart="11:43:00"; lunchCEnd="12:13:00";
            lunchDStart="12:10:00"; lunchDEnd="12:40:00";
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

    // Checks the Day in the DB
    // Not sure if this is still needed?
    public void AlternateSchedule()
    {
        // Reference DB object
        final DatabaseReference myDB = database.getReference("MGSH").child("DAY");

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
        final DatabaseReference myDB = database.getReference("MGSH").child("Adjusted");

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
                       currentDaySchedule.setText("Today's Schedule");



                       // Reprint the current day's schedule
                      printSchedule(analyzer.getDayOfWeek());

                      // Planned adjustments
                       if(analyzer.adjustedSchedule()==true)
                       {
                           printSchedule(analyzer.plannedScheudlesMGSH());
                           // Display adjusted scheudle
                           dayTextview.setText("Adjusted Schedule");
                       }

                       if(analyzer.firstWeek()==true)
                       {
                           dayTextview.setText("No Available Schedule");
                           printSchedule("NULL");
                           currentDaySchedule.setText("");
                           scheduleOverride=true;
                       }
                       else
                       {
                           scheduleOverride=false;
                       }

               }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w("HIII", "Failed to read value.", error.toException());
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
        final DatabaseReference myDB = database.getReference("MGSH").child("DAY");

        // Listen for changes
        myDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String adjustedDay = dataSnapshot.getValue(String.class);

               // Log.i("myTagz","adjustedSchedule:"+adjustedSchedule);

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
