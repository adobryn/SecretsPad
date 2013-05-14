package com.dobrynland.secretspad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class MainTouchView extends View
{
    private Paint paint = new Paint();
    private Path path = new Path();

    public MainTouchView(Context context)
    {
        super(context);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                // nothing to do
                break;
            default:
                return false;
        }

        // Schedules a repaint.
        invalidate();

        return true;
    }

    public void ToastMsg(String str)
    {
        Toast msg = Toast.makeText(MainTouchView.this.getContext(), str, Toast.LENGTH_SHORT);
        msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
        msg.show();
    }
}
