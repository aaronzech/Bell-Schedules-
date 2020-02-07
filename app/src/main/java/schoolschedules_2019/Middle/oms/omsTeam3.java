package schoolschedules_2019.Middle.oms;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

// Team Guardians + Star Wars 2019SY
public class omsTeam3 extends AppCompatActivity {

    static String EXPLORE1_START = "08:10:00";
    static String EXPLORE1_END = "08:51:00";

    static String CORE_1_START = "08:55:00";
    static String CORE_1_END = "09:51:00";

    static String CORE_2_START = "09:55:00";
    static String CORE_2_END = "10:51:00";

    static String LUNCH_A_START = "10:55:00";
    static String LUNCH_A__END = "11:21:00";

    static String CORE_3_START = "11:25:00";
    static String CORE_3_END = "12:21:00";

    static String CORE_4_START = "12:25:00";
    static String CORE_4_END = "13:21:00";

    static String ADVISORY_START = "13:25:00";
    static String ADVISORY_END = "13:51:00";

    static String EXPLORE6_START = "13:55:00";
    static String EXPLORE6_END = "14:40:00";


    static String PREF = "Core: ";



    // Fetch analyzer class
    Analyzer analyzer = new Analyzer();

    // Time Start and End times
    public static final String ENDOFDAY = "14:40:00";
    public static final String STARTOFDAY = "08:10:00";
    public static final String TEAM_NAME = "Guardians+Star Wars";

    TextView dailySchedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        // notification();

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


    private void startUp()
    {
        TextView dailySchedule = (TextView) findViewById(R.id.currentDaySchedule);
        dailySchedule.setText(analyzer.getDayOfWeek()+" Schedule");

        printSchedule();

        TextView teamNameDisplay = (TextView) findViewById(R.id.teamName);
        teamNameDisplay.setText(TEAM_NAME);

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
        TextView hour8TextView = (TextView)  findViewById(R.id.hour8Display);

        hour1TextView.setText("Explore 1" + " "+STARTOFDAY.substring(1,STARTOFDAY.length()-2)+"-8:51");
        hour2TextView.setText(PREF+"1"+ " 8:55-9:51");
        hour3TextView.setText(PREF +"2"+ " 9:55-10:51");
        hour4TextView.setText("Lunch A"+ " 10:55-11:21");
        hour5TextView.setText(PREF+"3"+" 11:25-12:21");
        hour6TextView.setText(PREF +"4"+ " 12:25-1:21");
        hour7TextView.setText("Advisory"+ " 1:25-1:51");
        hour8TextView.setText("Explore 6"+ " 1:55-2:40");


        // Show only active class if preference is set
        if(MainActivity.showOnlyActiveClass==true)
        {
            hour1TextView.setText(null); hour2TextView.setText(null); hour3TextView.setText(null);
            hour4TextView.setText(null); hour5TextView.setText(null); hour6TextView.setText(null);
            hour7TextView.setText(null); hour8TextView.setText(null);
            dailySchedule = findViewById(R.id.currentDaySchedule);
            dailySchedule.setText(null);
        }
    }


    String currentTime = null;
    String hour = null;

    // Go back button - Takes user to main app screen
    public void goHome(View view)
    {
        startActivity(new Intent(omsTeam3.this,omsSelect.class));
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

    // MGSH Schedule
    // HOUR 1 7:30-8:25
    private void whatHourIsIt(String currentTime)
    {

        try {
            // Current TIME
            Date d = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendarZ = Calendar.getInstance();
            calendarZ.setTime(d);
            //calendarZ.add(Calendar.DATE, 1);

            Date x = calendarZ.getTime();


            //Hour 1 check
            if(x.after(analyzer.analizeTime(EXPLORE1_START)) && x.before(analyzer.analizeTime(EXPLORE1_END)))
            {
                hour = "Explore 1";
            }
            else if (x.after(analyzer.analizeTime(EXPLORE6_START)) && x.before(analyzer.analizeTime(EXPLORE6_END)))
            {
                hour = "Explore 6";
            }
            else if (x.after(analyzer.analizeTime(CORE_1_START)) && x.before(analyzer.analizeTime(CORE_1_END)))
            {
                hour = PREF+"1";
            }
            else if (x.after(analyzer.analizeTime(ADVISORY_START)) && x.before(analyzer.analizeTime(ADVISORY_END)))
            {
                hour = "Advisory";
            }
            else if (x.after(analyzer.analizeTime(LUNCH_A_START)) && x.before(analyzer.analizeTime(LUNCH_A__END)))
            {
                hour = "Lunch A";
            }
            else if (x.after(analyzer.analizeTime(CORE_2_START)) && x.before(analyzer.analizeTime(CORE_2_END)))
            {

                hour=PREF +"2";
            }
            else if (x.after(analyzer.analizeTime(CORE_3_START)) && x.before(analyzer.analizeTime(CORE_3_END)))
            {

                hour=PREF + "3";
            }
            else if (x.after(analyzer.analizeTime(CORE_4_START)) && x.before(analyzer.analizeTime(CORE_4_END)))
            {

                hour=PREF + "4";
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
}
