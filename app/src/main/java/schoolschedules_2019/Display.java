package schoolschedules_2019;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

//import com.osseo.zechaaron.schoolschedules_2019.R;


public class Display extends AppCompatActivity
{


    public void display(String currentTime, String hour, boolean lunch)
    {
        TextView textView = (TextView) findViewById(R.id.current_time);
        textView.setText(currentTime);

        TextView textViewNew = (TextView) findViewById(R.id.display_text_view);
        textViewNew.setText(hour);


        if(MainActivity.showOnlyActiveClass==true)
        {
            textViewNew.setTextSize(2); // display text
            textView.setTextSize(2); // current time
        } else textViewNew.setTextSize(4000);


        // Adjust font size if lunch is happening
        if(lunch=true)
        {
            textViewNew.setTextSize(33);
        }
        else textViewNew.setTextSize(40);


    }

    // Set team name for middle schools - MGMS for example
    public void setTeamName(String name, TextView textView)
    {
        // Set team name
        textView.setText(name);
    }
}
