package think.anew.com.bleremotecontroler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import think.anew.com.bleremotecontroler.util.XToast;

/**
 * @author Administrator
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_control_main_layout_new_ui);
    }

    @Override
    public void onClick(View v) {
        XToast.show(this, "Power clicked ... ");
    }
}
