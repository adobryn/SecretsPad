package com.dobrynland.secretspad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.Gravity;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.widget.Toast;
import android.graphics.PathMeasure;
import android.util.DisplayMetrics;
import java.lang.Math;

public class MainTouchView extends ImageView
{
    private Paint paint = new Paint();
    private Path path = new Path();
    private Path comp_path = new Path();
    private int counter = 0;
    private int rad = 40;
    int screen_width;
    int screen_height;


    public MainTouchView(Context context)
    {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;

        drawThreeRandomCircles();
    }

    void drawThreeRandomCircles()
    {
        comp_path.reset();
        comp_path.addCircle((float)Math.random() * (screen_width - rad), (float)Math.random() * (screen_height - rad), rad, Path.Direction.CW);
        comp_path.addCircle((float)Math.random() * (screen_width - rad - 5), (float)Math.random() * (screen_height - rad - 5), rad + 5, Path.Direction.CW);
        comp_path.addCircle((float)Math.random() * (screen_width - rad - 10), (float)Math.random() * (screen_height - rad - 10), rad + 10, Path.Direction.CW);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        canvas.drawPath(comp_path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(counter < 3)
                {
                    path.moveTo(eventX, eventY);
                    path.addCircle(eventX, eventY, rad, Path.Direction.CW);
                    rad += 5;
                }
                return true;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if(counter < 3)
                {
                    counter++;
                }
                else
                {
                    if(comparePaths(path, comp_path))
                        ToastMsg("Auth OK");
                    else
                        ToastMsg("Auth NOK");

                    rad = 20;
                    counter = 0;
                    path.reset();
                    drawThreeRandomCircles();
                }
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void ToastMsg(String str)
    {
        Toast msg = Toast.makeText(MainTouchView.this.getContext(), str, Toast.LENGTH_SHORT);
        msg.setGravity(Gravity.BOTTOM, msg.getXOffset() / 2, msg.getYOffset() / 2);
        msg.show();
    }

    boolean comparePaths(Path path1, Path path2)
    {
        PathMeasure pm = new PathMeasure(path1, false);
        PathMeasure pm2 = new PathMeasure(path2, false);
        float aCoordinates[] = {0f, 0f};
        float bCoordinates[] = {0f, 0f};
        do
        {

            pm.getPosTan(pm.getLength() * 0.5f, aCoordinates, null);
            pm2.getPosTan(pm2.getLength() * 0.5f, bCoordinates, null);
            double dist = distance(aCoordinates, bCoordinates);
            if(dist > 10)
                return false;
            pm2.nextContour();
        }
        while(pm.nextContour());

        return true;
    }

    static float[] computeCentroid(float[] points)
    {
        float centerX = 0;
        float centerY = 0;
        int count = points.length;
        for (int i = 0; i < count; i++) {
            centerX += points[i];
            i++;
            centerY += points[i];
        }
        float[] center = new float[2];
        center[0] = 2 * centerX / count;
        center[1] = 2 * centerY / count;

        return center;
    }

    public double distance(float[] p, float[] q)
    {
        double dx   = p[0] - q[0];         //horizontal difference
        double dy   = p[1] - q[1];         //vertical difference
        double dist = Math.sqrt( dx*dx + dy*dy ); //distance using Pythagoras theorem
        return dist;
    }
}
