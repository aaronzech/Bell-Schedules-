package schoolschedules_2019.Middle.mgms;

import schoolschedules_2019.Analyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class mgmsHelper
{
    public String whatHourIsIt(String currentTime, String EXPLORATORY_1_START, String EXPLORATORY_1_END, String EXPLORATORY_2_START, String EXPLORATORY_2_END,
                               String CORE_1_START, String CORE_1_END, String CORE_2_START, String CORE_2_END, String CORE_3_START, String CORE_3_END,
                               String CORE_4_START, String CORE_4_END, String CORE_5_START, String CORE_5_END, String LUNCH_START, String LUNCH_END, Analyzer analyzer, String STARTOFDAY, String ENDOFDAY, String PREF)
    {
        String hour=null;
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
            else if (x.after(analyzer.analizeTime(CORE_2_START)) && x.before(analyzer.analizeTime(CORE_2_END)))
            {
                hour = PREF+"2";
            }
            else if (x.after(analyzer.analizeTime(CORE_3_START)) && x.before(analyzer.analizeTime(CORE_3_END)))
            {

                hour= PREF+"3";
            }
            else if (x.after(analyzer.analizeTime(LUNCH_START)) && x.before(analyzer.analizeTime(LUNCH_END)))
            {

                hour="LUNCH";
            }
            else if (x.after(analyzer.analizeTime(CORE_4_START)) && x.before(analyzer.analizeTime(CORE_4_END)))
            {

                hour=PREF+"4";
            }
            else if (x.after(analyzer.analizeTime(CORE_5_START)) && x.before(analyzer.analizeTime(CORE_5_END)))
            {

                hour=PREF+"5";
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
           // Log.i("myTAG", "WHATHOURISIT ERROR");
           // Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
        }

        return hour;

    }
}
