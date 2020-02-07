package schoolschedules_2019.Middle.mgms;


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


// 2019
// 6th grade 6AB
//
public class mgms_6A6B extends AppCompatActivity {

    // Time Start and End times
    public static final String STARTOFDAY = "08:10:00";
    public static final String ENDOFDAY = "14:40:00";

    static String EXPLORATORY_1_START = "08:10:00";
    static String EXPLORATORY_1_END = "08:55:00";

    static String EXPLORATORY_2_START = "08:59:00";
    static String EXPLORATORY_2_END = "09:44:00";

    static String CORE_1_START = "09:48:00";
    static String CORE_1_END = "10:33:00";

    static String ADVISORY_FLEX1_START = "11:08:00";
    static String ADVISORY_FLEX1_END = "11:48:00";

    static String ADVISORY_FLEX2_START = "11:51:00";
    static String ADVISORY_FLEX2_END = "12:13:00";

    static String LUNCH_START = "10:33:00";
    static String LUNCH_END = "11:08:00";

    static String CORE_2_START = "12:17:00";
    static String CORE_2_END = "13:02:00";

    static String CORE_3_START = "13:06:00";
    static String CORE_3_END = "13:51:00";

    static String CORE_4_START = "13:55:00";
    static String CORE_4_END = ENDOFDAY;

    static String PREF = "Core: ";

    // Fetch analyzer class
    Analyzer analyzer = new Analyzer();

    String teamName = "Pyramids of Giza - 6A\nBrandenburg Gate - 6B";

    TextView dailySchedule;
    String currentTime = null;
    String hour = null;

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

    private void setTeamName(String name)
    {
        // Set team name
        TextView teamName1 = (TextView) findViewById(R.id.teamName);
        teamName1.setText(name);
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
        TextView hour9TextView = (TextView)  findViewById(R.id.hour9Display);

        hour1TextView.setText("Exploratory 1" + " 8:10-8:55");
        hour2TextView.setText("Exploratory 2" + " 8:59-9:44");
        hour3TextView.setText(PREF +"1"+ " 9:48-10:33");
        hour4TextView.setText("Lunch "+ " 10:33-11:03");
        hour4TextView.setText("Advisory/Flex "+ " 11:08-11:48");
        hour5TextView.setText("Advisory/Flex "+ "11:51-12:13");
        hour6TextView.setText(PREF +"2"+  " 12:17-1:02");
        hour7TextView.setText(PREF +"3"+ " 1:06-1:51");
        hour8TextView.setText(PREF +"4"+ " 1:55-2:40");

        // Show only active class if preference is set
        if(MainActivity.showOnlyActiveClass==true)
        {
            hour1TextView.setText(null); hour2TextView.setText(null); hour3TextView.setText(null);
            hour4TextView.setText(null); hour5TextView.setText(null); hour6TextView.setText(null);
            hour7TextView.setText(null); hour8TextView.setText(null); hour9TextView.setText(null);
            dailySchedule = findViewById(R.id.currentDaySchedule);
            dailySchedule.setText(null);
        }
    }

    private void startUp()
    {

        // Set Team Name
        setTeamName(teamName);

        // Set School image
        ImageView headerImage = (ImageView) findViewById(R.id.imageView);
        headerImage.setImageResource(R.drawable.mgms);
        headerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        dailySchedule = (TextView) findViewById(R.id.currentDaySchedule);
        dailySchedule.setText(analyzer.getDayOfWeek()+" Schedule");

        TextView dayTextview = (TextView) findViewById(R.id.day);
        // No School in Session

        printSchedule();

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
        startActivity(new Intent(mgms_6A6B.this,mgmsSelect.class));
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

            Date x = calendarZ.getTime();


            //Hour 1 check
            if(x.after(analyzer.analizeTime(EXPLORATORY_1_START)) && x.before(analyzer.analizeTime(EXPLORATORY_1_END)))
            {
                hour = "EXPLORATORY: " + "1";
            }
            else if (x.after(analyzer.analizeTime(EXPLORATORY_2_START)) && x.before(analyzer.analizeTime(EXPLORATORY_2_END)))
            {
                hour = "EXPLORATORY: " + "2";
            }
            else if (x.after(analyzer.analizeTime(CORE_1_START)) && x.before(analyzer.analizeTime(CORE_1_END)))
            {
                hour = PREF+"1";
            }
            else if (x.after(analyzer.analizeTime(ADVISORY_FLEX1_START)) && x.before(analyzer.analizeTime(ADVISORY_FLEX1_END)))
            {
                hour = "Advisory/Flex 1";
            }
            else if (x.after(analyzer.analizeTime(ADVISORY_FLEX2_START)) && x.before(analyzer.analizeTime(ADVISORY_FLEX2_END)))
            {

                hour= "Advisory/Flex 2";
            }
            else if (x.after(analyzer.analizeTime(LUNCH_START)) && x.before(analyzer.analizeTime(LUNCH_END)))
            {

                hour="LUNCH";
            }
            else if (x.after(analyzer.analizeTime(CORE_2_START)) && x.before(analyzer.analizeTime(CORE_2_END)))
            {

                hour=PREF+"2";
            }
            else if (x.after(analyzer.analizeTime(CORE_3_START)) && x.before(analyzer.analizeTime(CORE_3_END)))
            {

                hour=PREF+"3";
            }
            else if (x.after(analyzer.analizeTime(CORE_4_START)) && x.before(analyzer.analizeTime(CORE_4_END)))
            {

                hour=PREF+"4";
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
