package schoolschedules_2019;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

//import com.osseo.zechaaron.schoolschedules_2019.R;

//import com.osseo.zechaaron.schoolschedules_2019.R;


public class Settings extends PreferenceActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SetFrag()).commit();

    }

    public static class SetFrag extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.listpref);

        }
    }

}