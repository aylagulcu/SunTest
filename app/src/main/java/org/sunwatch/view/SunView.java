package org.sunwatch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import org.sunwatch.model.UIValueHolder;

/**
 * Created by gungor on 22/08/15.
 */
public class SunView extends View {

    private int progress = 50;

    public SunView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        UIValueHolder.x = (int)(UIValueHolder.W/50f)*(progress-50);

        int x = UIValueHolder.W + UIValueHolder.x - UIValueHolder.DELTA / 2;

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.YELLOW);






        int down = UIValueHolder.curve[(int)(UIValueHolder.model.sunset()/UIValueHolder.K)+UIValueHolder.DELTA] - UIValueHolder.DELTA - UIValueHolder.H1; //
        int y = UIValueHolder.curve[Math.abs(UIValueHolder.x)] - down;

        RectF rectF = new RectF( x ,
                y - UIValueHolder.DELTA / 2, x + UIValueHolder.DELTA,
                UIValueHolder.DELTA+(y - UIValueHolder.DELTA / 2));

        canvas.drawOval(rectF, p);
    }

    public void redraw(int progress){
        this.progress = progress;
        invalidate();
    }

    public void redraw() {
        invalidate();
    }
}
