package com.dobrynland.secretspad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.Gravity;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.graphics.RectF;
import android.widget.Toast;
import android.graphics.PathMeasure;
import android.graphics.Matrix;
import java.util.ArrayList;
import java.util.List;
import  	android.util.Log;


public class MainTouchView extends ImageView
{
    private Paint paint = new Paint();
    private Path path = new Path();
    List<Path> pathList = new ArrayList<Path>();
    private PathMeasure pathMeasure = new PathMeasure(path, false);
    private Matrix matrix = new Matrix();
    private int counter = 0;

    public MainTouchView(Context context)
    {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
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
                    path.addCircle(eventX, eventY, 20, Path.Direction.CW);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                //path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                /*float aCoordinates[] = {0f, 0f};
                float bCoordinates[] = {eventX, eventY};

                //pathMeasure.nextContour();
                pathMeasure.setPath(path, false);
                pathMeasure.nextContour();

                //get point from the middle
                pathMeasure.getPosTan(pathMeasure.getLength() * 0.5f, aCoordinates, null);
                double dist = distance(aCoordinates, bCoordinates);
                if(dist < 25)
                    ToastMsg("OK");     */
                if(counter < 3)
                {
                    //pathList.add(path);
                    //path = new Path();
                    counter++;
                }
                else
                {
                    PathMeasure pm = new PathMeasure(path, false);
                    int pathCont=0;
                    float aCoordinates[] = {0f, 0f};
                    do
                    {
                        pathCont++;
                        pm.getPosTan(pm.getLength() * 0.5f, aCoordinates, null);
                        float bCoordinates[] = {eventX, eventY};
                        double dist = distance(aCoordinates, bCoordinates);
                        ToastMsg(Double.toString(dist));
                    }
                    while(pm.nextContour());

                }
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
        msg.setGravity(Gravity.BOTTOM, msg.getXOffset() / 2, msg.getYOffset() / 2);
        msg.show();
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
