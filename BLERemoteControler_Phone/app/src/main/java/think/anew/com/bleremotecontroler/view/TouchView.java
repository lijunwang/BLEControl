package think.anew.com.bleremotecontroler.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import think.anew.com.bleremotecontroler.util.XLog;

/**
 * @author Administrator
 * @date 2018/6/12
 */

public class TouchView extends View {
    private final String TAG = "TouchView";
    private final int PADDING = 100;

    public TouchView(Context context) {
        this(context, null);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int width = getWidth();
        int height = getHeight();
        XLog.d(TAG, "TouchView ... " + width + "," + height);
    }

    private int mWidth, mHeight;
    private Rect mTouchRect;
    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        XLog.d(TAG, "onAttachedToWindow ... " + mWidth + "," + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        canvas.drawColor(Color.DKGRAY);
        int rectLeft = PADDING;
        int rectRight = mWidth - PADDING;
        int rectTop = mHeight / 2 - (mWidth - 2 * PADDING) / 2;
        int rectBottom = mHeight / 2 + (mWidth - 2 * PADDING) / 2;
        XLog.d(TAG, "onDraw ... " + rectLeft + "," + rectTop + "," + rectRight + "," + rectBottom);
        mTouchRect = new Rect(rectLeft, rectTop, rectRight, rectBottom);
        mRectPaint.setColor(Color.RED);
        canvas.drawRect(mTouchRect, mRectPaint);
    }

    private boolean inRectTouch(MotionEvent event) {
        if (event.getX() < mTouchRect.right &&
                event.getX() > mTouchRect.left &&
                event.getY() > mTouchRect.top &&
                event.getY() < mTouchRect.bottom) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (inRectTouch(event)) {
            XLog.d(TAG, "onTouchEvent aa2... " + event.getX() + "," + event.getY());
            //push touch to handle by MouseActivity;
            return false;
        }else{
            XLog.d(TAG, "onTouchEvent bb2... " + event.getX() + "," + event.getY());
            return true;
        }
    }
}
