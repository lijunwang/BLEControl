package think.anew.com.bleremotecontroler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import think.anew.com.bleremotecontroler.util.XLog;
import think.anew.com.bleremotecontroler.view.TouchView;

/**
 * @author Administrator
 */
public class MouseActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_back);
        getSupportActionBar().setHomeButtonEnabled(true);
        mGestureDetector = new GestureDetector(this, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        XLog.d("MouseActivity onTouchEvent " + event.getX() + "," + event.getY());
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        XLog.d("MouseActivity onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        XLog.d("MouseActivity onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        XLog.d("MouseActivity onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        XLog.d("MouseActivity onScroll " + "," + distanceX + "," + distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        XLog.d("MouseActivity onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        XLog.d("MouseActivity onFling " + velocityX + "," + velocityY);
        return false;
    }
}
