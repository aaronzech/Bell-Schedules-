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
import schoolschedules_2019.Analyzer;
import schoolschedules_2019.MainActivity;
import schoolschedules_2019.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NVMS extends AppCompatActivity{


    // Constants
    static String ADVISORY_START = "08:10:00";
    static String ADVISORY_END = "08:42:00";

    static String BLOCK_1_START = "08:46:00";
    static String BLOCK_1_END = "09:36:00";

    static String BLOCK_2_START = "09:40:00";
    static String BLOCK_2_END = "10:30:00";

    static String BLOCK_3_START = "10:34:00";
    static String BLOCK_3_END = "11:24:00";

    static String BLOCK_4_START = "11:28:00";
    static String BLOCK_4_END = "12:52:00";

    static String BLOCK_5_START = "12:56:00";
    static String BLOCK_5_END = "13:46:00";

    static String BLOCK_6_START = "13:50:00";
    static String BLOCK_6_END = "14:40:00";

    // Time Start and End times
    public static final String STARTOFDAY = "08:10:00";
    public static final String ENDOFDAY = "14:40:00";

    // Prefences
    static String PREF = "Period: ";

    // Lunches
    String lunchAStart,lunchAEnd,lunchBStart,lunchBEnd;
    boolean lunch = false;

    // Fetch analyzer class
    Analyzer analyzer = new Analyzer();

    Date x=null;

    // Other TextViews
    TextView dailyScheduleFull;
    TextView dailySchedule;
    TextView hour1TextView;
    TextView hour2TextView;
    TextView hour3TextView;
    TextView hour4TextView;
    TextView hour5TextView;
    TextView hour6TextView;
    TextView hour7TextView;

    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        // Builds inital UI
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
                                // Check Date change for Day A, Day B
                                checkDateChange();
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

    /**
     * Initialize the textivews, and check to see if school is in session.
     * Then print out the proper scheudle accordingly
     */
    public void startUp()
    {
        // Initalize textviews
        TextView dayTextview = findViewById(R.id.day);
        dailyScheduleFull = findViewById(R.id.currentDayScheduleFull);
        dailySchedule = findViewById(R.id.currentDaySchedule);
        dailyScheduleFull = findViewById(R.id.currentDayScheduleFull);

        currentDate=analyzer.getCurrentDate();

        // No School in Session
        if(analyzer.schoolInSession(analyzer.getCurrentDate())==false)
        {
            dayTextview.setText("No School");
        }
        else // School in session
        {
            dayTextview.setText(analyzer.getDayOfWeek());
        }

        // Set School image
        ImageView headerImage = (ImageView) findViewById(R.id.imageView);
        headerImage.setImageResource(R.drawable.nvms_new);
        headerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // Print Schedule
        setSchedule();

        dailySchedule.setText(analyzer.getDayOfWeek()+" Schedule");

    }

    // Calulates the current day of the week, and returns it as a string
    private void setSchedule()
    {
        // Set textviews
        hour1TextView = findViewById(R.id.hour1Dislpay);
        hour2TextView = findViewById(R.id.hour2Display);
        hour3TextView = findViewById(R.id.hour3Display);
        hour4TextView = findViewById(R.id.hour4Display);
        hour5TextView = findViewById(R.id.hour5Display);
        hour6TextView = findViewById(R.id.hour6Display);
        hour7TextView = findViewById(R.id.hour7Display);

        hour1TextView.setText("Advisory" + " 8:10-8:42");
        hour2TextView.setText(PREF +"1"+ " 8:46-9:36");
        hour3TextView.setText(PREF +"2"+ " 9:40-10:30");
        hour4TextView.setText(PREF +"3"+ " 10:34-11:24");
        hour5TextView.setText(PREF +"4"+ " 11:28-12:52");
        hour6TextView.setText(PREF +"5"+ " 12:56-1:46");
        hour7TextView.setText(PREF +"6" + " 1:50-2:40");


        // Show only active class if preference is set
        if(MainActivity.showOnlyActiveClass==true)
        {
            hour1TextView.setText(null); hour2TextView.setText(null); hour3TextView.setText(null);
            hour4TextView.setText(null); hour5TextView.setText(null); hour6TextView.setText(null);
            hour7TextView.setText(null);
            dailySchedule.setText(null);

        }

    }
    String hour = null;

    // Go back button - Takes user to main app screen
    public void goHome(View view)
    {
        startActivity(new Intent(NVMS.this,MainActivity.class));
    }

    private void display(String currentTime) {
        TextView textView = (TextView) findViewById(R.id.current_time);
        textView.setText(currentTime);

        TextView textViewNew = (TextView) findViewById(R.id.display_text_view);
        textViewNew.setText(hour);

        // Adjust font if showOnlyActive is turned on
        if(MainActivity.showOnlyActiveClass==true)
        {
            textViewNew.setTextSize(70); // display text
            textView.setTextSize(70); // current time
            dailySchedule.setText("");

        } else textViewNew.setTextSize(40);

    }

    private void whatHourIsIt(String currentTime)
    {
        try {
            // Current TIME
            Date d = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendarZ = Calendar.getInstance();
            calendarZ.setTime(d);
            //calendarZ.add(Calendar.DATE, 1);

            x = calendarZ.getTime();

            //Hour 1 check
            if(x.after(analyzer.analizeTime(ADVISORY_START)) && x.before(analyzer.analizeTime(ADVISORY_END)))
            {
                hour = "Advisory";
            }
            else if (x.after(analyzer.analizeTime(BLOCK_1_START)) && x.before(analyzer.analizeTime(BLOCK_1_END)))
            {

                    hour = PREF+"1";

            }
            else if (x.after(analyzer.analizeTime(BLOCK_2_START)) && x.before(analyzer.analizeTime(BLOCK_2_END)))
            {
                    hour = PREF+"2";
            }
            else if (x.after(analyzer.analizeTime(BLOCK_3_START)) && x.before(analyzer.analizeTime(BLOCK_3_END)))
            {
                    checkLunches(3);
            }
            else if (x.after(analyzer.analizeTime(BLOCK_4_START)) && x.before(analyzer.analizeTime(BLOCK_4_END)))
            {

                    hour = PREF+"4";

            }
            else if (x.after(analyzer.analizeTime(BLOCK_5_START)) && x.before(analyzer.analizeTime(BLOCK_5_END)))
            {

                hour = PREF+"5";

            }
            else if (x.after(analyzer.analizeTime(BLOCK_6_START)) && x.before(analyzer.analizeTime(BLOCK_6_END)))
            {

                hour = PREF+"6";

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

    // This figures out which lunch period it is.
    private void checkLunches(int period)
    {
            lunchAStart="11:28:00"; lunchAEnd="11:58:00";
            lunchBStart="12:22:00"; lunchBEnd="12:52:00";

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
        else
        {
            hour = PREF +" "+period;
            lunch=false;
        }
    }

    public void checkDateChange()
    {
        // Only do stuff on weekdays
        if(!analyzer.getDayOfWeek().equals("Saturday")||!analyzer.getDayOfWeek().equals("Sunday"))
        {
            // Check for date change
           // Log.i("debug","Current Date in main thread is"+currentDate + "current date from analayzer is "+analyzer.getCurrentDate());
            if(!currentDate.equals(analyzer.getCurrentDate()))
            {
                currentDate=analyzer.getCurrentDate();
         ;
            }
        }
    }
}
