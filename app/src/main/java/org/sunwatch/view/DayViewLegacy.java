package org.sunwatch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

import org.sunwatch.activity.MainActivity;
import org.sunwatch.model.UIValueHolder;

import java.text.ParseException;

/**
 * Created by gungor on 21/08/15.
 */
public class DayViewLegacy extends ImageView{

    public DayViewLegacy(Context context) {
        super(context);
        UIValueHolder.init(getWidth());
        doTitle();
        setState(0);
        setCurrentTime();  // initialize data
    }

        Paint p = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        UIValueHolder.init(getWidth());
        doTitle();
        setState(0);
        setCurrentTime();  // initialize data

        System.out.println("canvas.getClipBounds() : ");
        System.out.println( canvas.getClipBounds() );
        System.out.println("canvas.getWidth() : ");
        System.out.println(canvas.getWidth());
        System.out.println("canvas.getClipBounds() : ");
        System.out.println(canvas.getClipBounds());



        canvas.clipRect(0, 0, 2 * UIValueHolder.W + UIValueHolder.GAP, UIValueHolder.H1 + 2 * UIValueHolder.H2);
        if (Math.abs(UIValueHolder.K*UIValueHolder.x) < v_aksam()) { // day
            p.setColor(UIValueHolder.BLUE);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(0, 0, 2*UIValueHolder.W, UIValueHolder.H1,p);
            try { // protection against unexpected errors
                doColors();
                drawCurve(canvas);
                drawTime(canvas);
                //drawShadow(g);  skip

            } catch (Exception e) {
                System.out.printf("paint: %s \n", e);
                e.printStackTrace();
            }
        } else { // night
            int c = UIValueHolder.col[Math.abs(UIValueHolder.x)];
            //System.out.printf("%s: x=%s %s\n", VER, x, c);
            Paint p2 = new Paint();
            p2.setColor(c);
            canvas.drawRect(0, 0, 2 * UIValueHolder.W, UIValueHolder.H1, p2);
            doColors();
            drawClock(canvas, true, 0);
            drawTime(canvas);
        }


    }

    void fillColor(int x2, int c) {
        for (; UIValueHolder.x<x2; UIValueHolder.x++)
            UIValueHolder.col[UIValueHolder.x] = c;
    }
    void graded(int x2, int c1, int c2) {
        float[] f1 = {Color.alpha(c1),Color.red(c1),Color.green(c1),Color.blue(c1)};
        float[] f2 = {Color.alpha(c2),Color.red(c2),Color.green(c2),Color.blue(c2)};

        System.out.println("f1");
        System.out.println(f1[0] + "," + f1[1] + "," + f1[2] + "," + f1[3]);
        System.out.println(f2[0] + "," + f2[1] + "," + f2[2] + "," + f2[3]);

        float[] d = {0,0,0,0};
        int x1 = UIValueHolder.x;
        for (int k=0; k<3; k++)
            d[k] = (f2[k] - f1[k])/(x2 - x1);
        for (; UIValueHolder.x<x2; UIValueHolder.x++) {
            float[] c = {0,0,0};
            for (int k=0; k<3; k++)
                c[k] = f1[k] + d[k]*(UIValueHolder.x - x1);
            UIValueHolder.col[UIValueHolder.x] = Color.rgb((int)c[0], (int)c[1], (int)c[2]);
        }
    }
    public void doColors() {
        UIValueHolder.x = 0;  // field used in calculation
        fillColor(2+v_ogle()/UIValueHolder.K, UIValueHolder.RED);
        fillColor(v_ikindi()/UIValueHolder.K, UIValueHolder.NOON);
        graded((v_ikindi()+80)/UIValueHolder.K, UIValueHolder.NOON, UIValueHolder.DARK);
        fillColor((UIValueHolder.model.sunset()-40)/UIValueHolder.K, UIValueHolder.DARK);
        graded(UIValueHolder.model.sunset()/UIValueHolder.K, UIValueHolder.DARK, UIValueHolder.RED);
        fillColor(v_aksam()/UIValueHolder.K, UIValueHolder.RED);
        graded(-v_imsak()/UIValueHolder.K, UIValueHolder.BLUE, Color.BLACK);  //v_yatsi()
        //System.out.printf("%s: %s %s\n", VER, v_aksam(), v_yatsi());
        fillColor(UIValueHolder.W, Color.BLACK);
        shadowLength(); setMinute(0);  // noon
    }
    void shadowLength() {
        for (int i=0; i<UIValueHolder.W; i++) {
            //float a = model.altitude(K*i);
            int s = 0;  //Math.round(H2/model.tan(a));
            UIValueHolder.shade[i] = (s<0 || s>UIValueHolder.W ? UIValueHolder.W : s); // if too large, clip
        }
    }
    public void doTitle() {

    }
    public void setDate(String s) {
        try {
            UIValueHolder.model.setDate(UIValueHolder.DATE.parse(s));
            setState(120);
        } catch (ParseException ex) {
            setCurrentTime();
        }
    }
    public void setMinute(int m) {
        UIValueHolder.x = m/UIValueHolder.K;  // pixels
        if (UIValueHolder.x < -UIValueHolder.W) UIValueHolder.x += 2*UIValueHolder.W;
        if (UIValueHolder.x >= UIValueHolder.W) UIValueHolder.x -= 2*UIValueHolder.W;
        long t1 = 0; //Math.round((model.noonM + m + 0.5)*60);
        UIValueHolder.model.setTime(t1*1000);
        UIValueHolder.alfa = "0";  //Math.round(model.altitude(m))+"�";
//        view.repaint();
    }
    public void setCurrentTime() {
        long t = System.currentTimeMillis();
        double d = UIValueHolder.model.day;  //Timer.toJulian(t);
        if (UIValueHolder.model.day != (int)d) {
            System.out.printf("setCurrentTime: %s %s %n", UIValueHolder.model.day, d);
            UIValueHolder.model.setDate(d, true); //model.report();
        }
        int m = 0;  //(int)Math.round(t/60/1000 - model.noonM);
        setMinute(m);
//        if (menuD.getSelectedIndex() > 0)
//            menuD.setSelectedIndex(0);
    }
    public void setState(int s) {
//        if (state == s) return;
//        state = s;
//        but1.setText(s == 0 ? "\u25A0" : "\u25BA");
//        but2.setText(s < 0 ? "||" : ">>");
//        lab.setForeground(s == 0 ? Color.BLACK : Color.RED);
//        if (s == 0) setCurrentTime();
//        if (thd != null) thd.interrupt();
    }
    public void stop() {

    }


    public String toString() {
        return UIValueHolder.model.toString();
    }
    int v_imsak()  { return UIValueHolder.model.v_imsak(); }
    int v_ogle()   { return UIValueHolder.model.v_ogle(); }
    int v_ikindi() { return UIValueHolder.model.v_ikindi(); }
    int v_aksam()  { return UIValueHolder.model.v_aksam(); }


    void line(Canvas canvas, int i, int c) {
        Paint p = new Paint();

        System.out.println("DRAWLINE : " + (UIValueHolder.W + i) + "," + (UIValueHolder.H1) + "," + (UIValueHolder.W + i) + "," + (UIValueHolder.H1 + UIValueHolder.H2) + ":COLOR:" + c);
        p.setColor(c);
//        p.setColor(Color.BLACK);
        canvas.drawLine(UIValueHolder.W + i, UIValueHolder.H1, UIValueHolder.W + i, UIValueHolder.H1 + UIValueHolder.H2, p);
        canvas.drawLine(UIValueHolder.W - i, UIValueHolder.H1, UIValueHolder.W - i, UIValueHolder.H1 + UIValueHolder.H2, p);
    }
    void drawTime(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        int y = 60/UIValueHolder.K;
        if (UIValueHolder.state != 0 /*fast*/) canvas.drawText(UIValueHolder.MSG, 600 / UIValueHolder.K, y, p);
        int[] a = {UIValueHolder.W+UIValueHolder.x-UIValueHolder.DELTA, UIValueHolder.W+UIValueHolder.x, UIValueHolder.W+UIValueHolder.x+UIValueHolder.DELTA};
        int[] b = {UIValueHolder.H1+UIValueHolder.H2+UIValueHolder.DELTA+1, UIValueHolder.H1+UIValueHolder.H2+1,
                UIValueHolder.H1+UIValueHolder.H2+UIValueHolder.DELTA+1};

//        g.fillPolygon(a, b, 3);
        p.setColor(Color.YELLOW);
        canvas.drawText(UIValueHolder.model.ddMMyyyy(), UIValueHolder.GAP, y, p);
//        g.drawString(model.loc.toString(), GAP, 2*y);
        canvas.drawText(UIValueHolder.alfa, 2 * UIValueHolder.W - 90 / UIValueHolder.K, y, p);
        for (int i=0; i<UIValueHolder.W; i++)  // draw colors
            line(canvas, i, UIValueHolder.col[i]);
        canvas.drawLine(0, UIValueHolder.H1, 0, UIValueHolder.H1 + UIValueHolder.H2, p); // missing line at the left
    }
    void drawCurve(Canvas canvas) {

        System.out.println("drawCurve : W "+ UIValueHolder.W);
        int down = UIValueHolder.curve[(int)UIValueHolder.model.sunset()/UIValueHolder.K+UIValueHolder.DELTA] - UIValueHolder.DELTA - UIValueHolder.H1; //

        Paint p = new Paint();


        int W = UIValueHolder.W;

        System.out.println("W : "+ W);

        p.setColor(Color.YELLOW);
        int x1 = 0;
        int y1 = UIValueHolder.curve[x1] - down;

        while (x1+8 < W) {
            int x2 = x1+8;
            int y2 = UIValueHolder.curve[x2] - down;
            System.out.println("drawline + : "+ (W + x1) +","+y1+","+ (W + x2)+","+ y2);
            System.out.println("drawline - : "+ (W - x1) +","+y1+","+ (W - x2)+","+ y2);

            canvas.drawLine(W + x1, y1, W + x2, y2, p);
            canvas.drawLine(W - x1, y1, W - x2, y2, p);
            if (y2 > UIValueHolder.H1) {
                break;
            }
            x1 = x2;
            y1 = y2;
        }
        drawClock(canvas, false, down);
        int y = UIValueHolder.curve[Math.abs(UIValueHolder.x)] - down;


    }
    void drawClock(Canvas canvas, boolean night, int down) {
        Paint p = new Paint();

        if (night) p.setColor(Color.WHITE);
        else p.setColor(Color.BLACK);

        int min = UIValueHolder.model.twelve - 12*60; //12 hours
        int c = 0; // start at 12am
        for (int i=min; i<-min; i+=60) {
            int x = UIValueHolder.W + i/UIValueHolder.K;
            int y = UIValueHolder.H1 - 4/UIValueHolder.K;
            if (!night) {
                int k = Math.min(UIValueHolder.W-1, Math.abs(i/UIValueHolder.K));
                y = (UIValueHolder.curve[k] - down);
                if (y >= UIValueHolder.H1) y = UIValueHolder.H1 - 4/UIValueHolder.K;
            }
            canvas.drawText(UIValueHolder.d2s[c % 24], x - 12 / UIValueHolder.K, y - 10 / UIValueHolder.K, p);
            canvas.drawRect(x - 1, y - 1, (4 / UIValueHolder.K)+(x-1) , (4 / UIValueHolder.K)+(y-1), p);
            c++; //if (c==12) System.out.printf("x=%s y=%s \n", x, y);
        }
        //System.out.printf("min=%s count=%s \n", min, c);
    }
    void drawShadow(Canvas canvas) {
        int z = v_ogle()/UIValueHolder.K;
        int d = (UIValueHolder.x>z? -1 : 1);  // direction of the shadow
        int x1 = UIValueHolder.W+1 + d*z;
        int x2 = x1+ d*UIValueHolder.shade[Math.abs(UIValueHolder.x)];
        int y = UIValueHolder.H1+UIValueHolder.H2;
        Paint p = new Paint();

        canvas.drawLine(x1, y, x2, y, p);
        canvas.drawLine(x1, y-1, x2, y-1, p);
    }

}
