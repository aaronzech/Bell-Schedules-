package schoolschedules_2019;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Analyzer {



    public Date analizeTime(String time) {


        Date x=null;

        try {
            // Hour 1

            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(time);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            x = calendar1.getTime();
            return x;

        } catch (ParseException e) {
            e.printStackTrace();

        }
        return x;

    }


    /**
     *  Method Formats a String into 24 hour time format for the math clock to compare times
     *
     * @return currentTime in 24 format
     */
    public String getCurrentTime()  {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss"); // Math clock
        String currentTime =  mdformat.format(calendar.getTime());
        return currentTime;
    }

    public String upDateDislayTime()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss"); // Math clock

        // Display Current time
        SimpleDateFormat dislaymdformat = new SimpleDateFormat("hh:mm:ss a"); // Display clock
        String currentTimeAM_PM =  dislaymdformat.format(calendar.getTime());

        return currentTimeAM_PM;
        //display(strDate);

    }

    /**
     * Method figures out the day of the week (Monday,Tuesday...)
     * @return dayOfWeek
     */
    public String getDayOfWeek() {
        // Get day of the week
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();
        String dayOfWeek = dayFormat.format(calendar.getTime());
        return dayOfWeek;
    }

    public String getCurrentDate()  {
        Calendar calendar = Calendar.getInstance();
       // SimpleDateFormat mdformat = new SimpleDateFormat("MM/d/YY"); // Math clock
        SimpleDateFormat mdformat = new SimpleDateFormat("MM/d/YY"); // Math clock
        String currentDate =  mdformat.format(calendar.getTime());
        return currentDate;
    }

    // Check for no School
    public boolean schoolInSession(String date)
    {
        boolean schoolinSession = true;

        // Hold no school dates
        ArrayList<String> noSchoolDates = new ArrayList<>();
        // Add dates
            // Test date
            noSchoolDates.add("06/26/18");
            // SY 18-19
            noSchoolDates.add("09/03/18");
            noSchoolDates.add("10/18/18");
            noSchoolDates.add("10/19/18");
            noSchoolDates.add("11/06/18");
            noSchoolDates.add("11/22/18");
            noSchoolDates.add("11/23/18");
            noSchoolDates.add("11/30/18");
            noSchoolDates.add("12/24/18");
            noSchoolDates.add("12/25/18");
            noSchoolDates.add("12/26/18");
            noSchoolDates.add("12/27/18");
            noSchoolDates.add("12/28/18");
            noSchoolDates.add("12/29/18");
            noSchoolDates.add("12/30/18");
            noSchoolDates.add("12/31/18");
            noSchoolDates.add("01/01/19");
            noSchoolDates.add("01/02/19");
            noSchoolDates.add("02/04/19");
            noSchoolDates.add("02/15/19");
            noSchoolDates.add("02/18/19");
            noSchoolDates.add("03/08/19");
            noSchoolDates.add("03/25/19");
            noSchoolDates.add("03/26/19");
            noSchoolDates.add("03/27/19");
            noSchoolDates.add("03/28/19");
            noSchoolDates.add("03/29/19");
            noSchoolDates.add("04/19/19");
            noSchoolDates.add("04/29/19");

        // check for dates
        for (String i:noSchoolDates)
        {
            if(i.equals(date))
            {
                schoolinSession=false;
            }

        }
        return schoolinSession;
    }



    public String plannedScheudlesMGSH()
    {
        String d="";
        String date = getCurrentDate();

        // Planned adjusted schedules
        if(date.equals("10/15/18")) d="Tuesday";
        if(date.equals("10/16/18")) d="Wednesday";
        if(date.equals("10/17/18")) d="Thursday";

        if(date.equals("11/19/18")) d="Tuesday";
        if(date.equals("11/20/18")) d="Wednesday";
        if(date.equals("11/21/18")) d="Thursday";

        // there is an issue with the date format 01/2/19
        if(date.equals("01/2/19")) d="Friday";
        if(date.equals("01/3/19")) d="Monday";
        if(date.equals("01/4/19")) d="Monday";

      //  Log.i("day",getCurrentDate()+"");
        return d;
    }

    public boolean adjustedSchedule()
    {
        boolean d = false;
        String date = getCurrentDate();

        if(date.equals("10/15/18")) d=true;
        if(date.equals("10/16/18")) d=true;
        if(date.equals("10/17/18")) d=true;

        if(date.equals("11/19/18")) d=true;
        if(date.equals("11/20/18")) d=true;
        if(date.equals("11/21/18")) d=true;

        // there is an issue with the date format 01/2/19
        if(date.equals("01/2/19")) d=true;
        if(date.equals("01/3/19")) d=true;

        return d;
    }


    public boolean firstWeek()
    {
        boolean d=false;
        String date = getCurrentDate();

        // Planned adjusted schedules
        if(date.equals("09/4/18")) d=true;
        if(date.equals("09/5/18")) d=true;
        if(date.equals("09/6/18")) d=true;
        if(date.equals("09/7/18")) d=true;

        return d;
    }



    public String currentMonth(String school)
    {
        String result=null;

        String d1 = "07/9/18";
        String d2 = "06/30/18";
        String d3 = "07/01/18";
        String d4 = "07/02/18";
        String d5 = "07/03/18";


        String date = getCurrentDate(); // gets date in 07/X/18 format
       // Log.i("AZ","Current Date "+date);
        //Log.i("AZ","da "+d1);

        if(school.equals("NVMS"))
        {

            if(date.equals(d1))
            {
                result="A";
                return result;
            }
        }
        return result;
    }

    public String nvmsDay()
    {
        String day = null;
        String date = getCurrentDate(); // gets date in 07/X/18 format
        String dayOfweek = getDayOfWeek(); // get current day (monday,tuesday...)

        // Array with Day A, Day B values

        String[] nvmsday={"A","B"};

        // Only do stuff on weekdays
        if(!dayOfweek.equals("Saturday")||!dayOfweek.equals("Sunday"))
        {
           // Check for date change
            if(date!=date)
            {
                if(day=="A") day="B";
                else day ="A";
            }
        }
        return day;
    }

}
