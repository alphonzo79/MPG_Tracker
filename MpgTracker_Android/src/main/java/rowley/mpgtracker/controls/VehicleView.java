package rowley.mpgtracker.controls;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import rowley.mpgtracker.R;

public class VehicleView extends LinearLayout {

    public VehicleView(Context context, String text) {
        super(context);

        inflate(context, R.layout.view_vehicle_view, null);
        ((TextView)findViewById(R.id.text_view)).setText(text);
    }

    public void setText(String text) {
        ((TextView)findViewById(R.id.text_view)).setText(text);
    }
}
