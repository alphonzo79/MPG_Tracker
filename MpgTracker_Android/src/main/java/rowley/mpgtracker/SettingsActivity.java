package rowley.mpgtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SettingsActivity extends Activity implements View.OnClickListener {
    private LinearLayout vehicleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        findViewById(R.id.my_vehicles).setOnClickListener(this);

        vehicleLayout = (LinearLayout)findViewById(R.id.vehicle_container);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.my_vehicles:
                if(vehicleLayout.getVisibility() != View.VISIBLE) {
                    vehicleLayout.setVisibility(View.VISIBLE);
                } else {
                    vehicleLayout.setVisibility(View.GONE);
                }
                break;
        }
    }
}
