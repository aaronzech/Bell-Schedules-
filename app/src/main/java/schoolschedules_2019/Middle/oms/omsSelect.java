package schoolschedules_2019.Middle.oms;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

//import com.osseo.zechaaron.schoolschedules_2019.R;
import schoolschedules_2019.MainActivity;
import schoolschedules_2019.R;

public class omsSelect extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oms_select);

        // Spinner Logic
        Spinner mySpinner = (Spinner) findViewById(R.id.spinnerTeam);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(omsSelect.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.omsTeams));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(this);

        // Displays Version Number of the App
        versionNumber(MainActivity.versionNumber);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String s = parent.getItemAtPosition(position).toString();

        if(s.equals("Bulldogs+Gophers"))
        {
            startActivity(new Intent(omsSelect.this,omsTeam1.class));
        }
        if(s.equals("Mavericks"))
        {
            startActivity(new Intent(omsSelect.this,omsTeam2.class));
        }
        if(s.equals("Guardians+Star Wars"))
        {
            startActivity(new Intent(omsSelect.this,omsTeam3.class));
        }
        if(s.equals("Incredibles"))
        {
            startActivity(new Intent(omsSelect.this,omsTeam4.class));
        }
        if(s.equals("Avengers+Teen Titans"))
        {
            startActivity(new Intent(omsSelect.this,omsTeam5.class));
        }
        if(s.equals("Justice League"))
        {
            startActivity(new Intent(omsSelect.this,omsTeam6.class));
        }
    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    // Go back button - Takes user to main app screen
    public void goHome(View view)
    {
        startActivity(new Intent(omsSelect.this,MainActivity.class));
    }

    private void versionNumber(String number)
    {
        TextView versionNumber = (TextView) findViewById(R.id.version_text_view);
        versionNumber.setText("Version: "+number);

    }

}
