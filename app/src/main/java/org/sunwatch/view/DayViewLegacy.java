package org.sunwatch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.location.Location;
import android.view.View;

import org.sunwatch.model.Vakit;

import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by gungor on 21/08/15.
 */
public class DayViewLegacy extends View{

    static final String
            MSG = "Günümüz büyle geçiyor",
            FILE = "Vakit.txt",
            TITLE = " için güneş saati -- ", VER = "Va0",
            TIP = "<HTML>Mavi: sabah-akşam <BR>Sarı: Öğle-ikindi "
                    +"<BR>Siyah: yatsı <BR>Kırmızı: kerahat";

    static final boolean WIDE = false;
    static final int
            K = WIDE? 1 : 2,
            W = 720/K,  //half width
            GAP = WIDE? 15 : 10,
            M = WIDE? 135 : 90, //used in cosine curve
            H1 = WIDE? 300 : 200,
            H2 = WIDE? 45 : 30,
            DELTA = WIDE? 12 : 8,
            SIZE = WIDE? 18 : 12;

    static final int
            BLACK = Color.BLACK,
            BLUE  = Color.BLUE,
            RED   = Color.RED,
            NOON  = Color.YELLOW, //yellow
            DARK  = Color.GRAY;  //dark yellow
    static final int[] curve = new int[W];
    static final String[] d2s = new String[24];
    static final SimpleDateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");
    static {
        // String[] used in drawClock()
        for (int i=0; i<10; i++) d2s[i] = "0"+i;
        for (int i=10; i<24; i++) d2s[i] = ""+i;
        // cosine curve used in drawCurve()
        for (int d=0; d<W; d++)
            curve[d] = H1 - (int)Math.round(M*Math.cos(Math.PI*d/W));
        // for Java 6
        //java.util.TimeZone.setDefault(Location.DEFAULT.zone);
        DATE.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    final int[] col = new int[W];
    final int[] shade = new int[W];
    final Vakit model = new Vakit(this);
    Thread thd;
    int state = 1;  // s<0 -> fast,  s=0 -> clock,  s>0 count down
    int x;  // pixels since noon  -W<x<W  -- K*x in minutes
    String alfa; // altitude -- uses C0, C1


    public DayViewLegacy(Context context) {
        super(context);
        doTitle();
        setState(0);
        setCurrentTime();  // initialize data
    }

    @Override
    protected void onDraw(Canvas canvas) {

        System.out.println("canvas.getClipBounds() : ");
        System.out.println( canvas.getClipBounds() );
        System.out.println("canvas.getWidth() : ");
        System.out.println( canvas.getWidth() );
        System.out.println("canvas.getClipBounds() : ");
        System.out.println(canvas.getClipBounds());

        float scale = canvas.getWidth() / 720f;

        System.out.println("scale :" + scale);

        canvas.save();
        canvas.scale(scale, 1f);

        Paint p = new Paint();

        canvas.clipRect(0, 0, 2 * W + GAP, H1 + 2 * H2);
        if (Math.abs(K*x) < v_aksam()) { // day
            p.setColor(BLUE);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(0, 0, 2*W, H1,p);
            try { // protection against unexpected errors
                drawCurve(canvas); drawTime(canvas);
                //drawShadow(g);  skip
            } catch (Exception e) {
                System.out.printf("paint: %s \n", e);
                e.printStackTrace();
            }
        } else { // night
            int c = col[Math.abs(x)];
            //System.out.printf("%s: x=%s %s\n", VER, x, c);
            Paint p2 = new Paint();
            p2.setColor(c);
            canvas.drawRect(0, 0, 2 * W, H1, p2);
            drawClock(canvas, true, 0);
            drawTime(canvas);
        }

        canvas.restore();



    }

    void fillColor(int x2, int c) {
        for (; x<x2; x++) col[x] = c;
    }
    void graded(int x2, int c1, int c2) {
        float[] f1 = {Color.alpha(c1),Color.red(c1),Color.green(c1),Color.blue(c1)};
        float[] f2 = {Color.alpha(c2),Color.red(c2),Color.green(c2),Color.blue(c2)};

        System.out.println("f1");
        System.out.println(f1);

        float[] d = {0,0,0,0};
        int x1 = x;
        for (int k=0; k<3; k++)
            d[k] = (f2[k] - f1[k])/(x2 - x1);
        for (; x<x2; x++) {
            float[] c = {0,0,0};
            for (int k=0; k<3; k++)
                c[k] = f1[k] + d[k]*(x - x1);
            col[x] = Color.rgb((int)c[0], (int)c[1], (int)c[2]);
        }
    }
    public void doColors() {
        x = 0;  // field used in calculation
        fillColor(2+v_ogle()/K, RED);
        fillColor(v_ikindi()/K, NOON);
        graded((v_ikindi()+80)/K, NOON, DARK);
        fillColor((model.sunset()-40)/K, DARK);
        graded(model.sunset()/K, DARK, RED);
        fillColor(v_aksam()/K, RED);
        graded(-v_imsak()/K, BLUE, Color.BLACK);  //v_yatsi()
        //System.out.printf("%s: %s %s\n", VER, v_aksam(), v_yatsi());
        fillColor(W, Color.BLACK);
        shadowLength(); setMinute(0);  // noon
    }
    void shadowLength() {
        for (int i=0; i<W; i++) {
            //float a = model.altitude(K*i);
            int s = 0;  //Math.round(H2/model.tan(a));
            shade[i] = (s<0 || s>W ? W : s); // if too large, clip
        }
    }
    public void doTitle() {

    }
    public void setDate(String s) {
        try {
            model.setDate(DATE.parse(s));
            setState(120);
        } catch (ParseException ex) {
            setCurrentTime();
        }
    }
    public void setMinute(int m) {
        x = m/K;  // pixels
        if (x < -W) x += 2*W;
        if (x >= W) x -= 2*W;
        long t1 = 0; //Math.round((model.noonM + m + 0.5)*60);
        model.setTime(t1*1000);
        alfa = "0�";  //Math.round(model.altitude(m))+"�";
//        view.repaint();
    }
    public void setCurrentTime() {
        long t = System.currentTimeMillis();
        double d = model.day;  //Timer.toJulian(t);
        if (model.day != (int)d) {
            System.out.printf("setCurrentTime: %s %s %n", model.day, d);
            model.setDate(d, true); //model.report();
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
        thd = null;
    }


    public String toString() {
        return model.toString();
    }
    int v_imsak()  { return model.v_imsak(); }
    int v_ogle()   { return model.v_ogle(); }
    int v_ikindi() { return model.v_ikindi(); }
    int v_aksam()  { return model.v_aksam(); }


    void line(Canvas canvas, int i, int c) {
        Paint p = new Paint();
        p.setColor(c);
        canvas.drawLine(W + i, H1, W + i, H1 + H2, p);
        canvas.drawLine(W-i, H1, W-i, H1+H2,p);
    }
    void drawTime(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        int y = 60/K;
        if (state != 0 /*fast*/) canvas.drawText(MSG, 600 / K, y, p);
        int[] a = {W+x-DELTA, W+x, W+x+DELTA};
        int[] b = {H1+H2+DELTA+1, H1+H2+1, H1+H2+DELTA+1};

//        g.fillPolygon(a, b, 3);
        p.setColor(Color.YELLOW);
        canvas.drawText(model.ddMMyyyy(), GAP, y, p);
//        g.drawString(model.loc.toString(), GAP, 2*y);
        canvas.drawText(alfa, 2 * W - 90 / K, y, p);
        for (int i=0; i<W; i++)  // draw colors
            line(canvas, i, col[i]);
        canvas.drawLine(0, H1, 0, H1 + H2, p); // missing line at the left
    }
    void drawCurve(Canvas canvas) {
        int down = curve[(int)model.sunset()/K+DELTA] - DELTA - H1; //

        Paint p = new Paint();

        p.setColor(Color.GRAY);
        int x1 = 0; int y1 = curve[x1] - down;
        while (x1 < W) {
            int x2 = x1+8; int y2 = curve[x2] - down;
            canvas.drawLine(W + x1, y1, W + x2, y2, p);
            canvas.drawLine(W - x1, y1, W - x2, y2, p);
            if (y2 > H1) break;
            x1 = x2; y1 = y2;
        }
        drawClock(canvas, false, down);
        int y = curve[Math.abs(x)] - down;
        //System.out.printf("x=%s y=%s \n", x, y);
        Paint p2 = new Paint();
        p2.setColor(Color.YELLOW);
        p2.setStyle(Paint.Style.FILL_AND_STROKE);

        RectF rectF = new RectF( W + x - DELTA / 2, y - DELTA / 2, DELTA+(W + x - DELTA / 2), DELTA+(y - DELTA / 2));
        canvas.drawOval(rectF, p2);

    }
    void drawClock(Canvas canvas, boolean night, int down) {
        Paint p = new Paint();

        if (night) p.setColor(Color.WHITE);
        else p.setColor(Color.BLACK);

        int min = model.twelve - 12*60; //12 hours
        int c = 0; // start at 12am
        for (int i=min; i<-min; i+=60) {
            int x = W + i/K;
            int y = H1 - 4/K;
            if (!night) {
                int k = Math.min(W-1, Math.abs(i/K));
                y = (curve[k] - down);
                if (y >= H1) y = H1 - 4/K;
            }
            canvas.drawText(d2s[c % 24], x - 12 / K, y - 10 / K, p);
            canvas.drawRect(x - 1, y - 1, (4 / K)+(x-1) , (4 / K)+(y-1), p);
            c++; //if (c==12) System.out.printf("x=%s y=%s \n", x, y);
        }
        //System.out.printf("min=%s count=%s \n", min, c);
    }
    void drawShadow(Canvas canvas) {
        int z = v_ogle()/K;
        int d = (x>z? -1 : 1);  // direction of the shadow
        int x1 = W+1 + d*z;
        int x2 = x1+ d*shade[Math.abs(x)];
        int y = H1+H2;
        Paint p = new Paint();

        canvas.drawLine(x1, y, x2, y, p);
        canvas.drawLine(x1, y-1, x2, y-1, p);
    }

}
