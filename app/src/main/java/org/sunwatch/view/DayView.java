package org.sunwatch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import org.sunwatch.model.Vakit;

import java.text.SimpleDateFormat;


/**
 * Created by gungor on 19/08/15.
 */
public class DayView extends View {

    final String
            MSG = "G�n�m�z b�yle ge�iyor", FILE = "Vakit.txt",
            TITLE = " i�in g�ne� saati -- ", VER = "Va0",
            TIP = "<HTML>Mavi: sabah-ak�am <BR>Sar�: ��le-ikindi "
                    +"<BR>Siyah: yats� <BR>K�rm�z�: kerahat";
    final boolean
            WIDE = false;
    int
            K = WIDE? 1 : 2,
            W,
            GAP = WIDE? 15 : 10,
            M = WIDE? 135 : 90, //used in cosine curve
            H1 = WIDE? 300 : 200,
            H2 = WIDE? 45 : 30,
            DELTA = WIDE? 6 : 4,
            SIZE = WIDE? 18 : 12;


    final int
            BLACK = Color.BLACK,
            BLUE  = Color.BLUE,
            RED   = Color.RED,
            NOON  = Color.YELLOW,
            DARK  = Color.GRAY;

    int x = 0;

    int[] curve ;
    static final String[] d2s = new String[24];
    static final SimpleDateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");
    final Vakit model = new Vakit(this);


    public DayView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }

    @Override
    protected void onDraw(Canvas canvas) {

//        W =  getLayoutParams().width/K;  //half width

        System.out.println("getLayoutParams().width : "+ getLayoutParams().width ) ;
        System.out.println("getWidth() : "+ getWidth() ) ;

        W = (int)(getWidth() / 2);



        K = WIDE? 1 : 2;
        GAP = WIDE? 15 : 10;
        M = WIDE? 135 : 90; //used in cosine curve
        H1 = WIDE? 300 : 200;
        H2 = WIDE? 45 : 30;
        DELTA = WIDE? 12 : 8;
        SIZE = WIDE? 18 : 12;

        curve = new int[W];
        for (int d=0; d<W; d++)
            curve[d] = H1 - (int)Math.round(M*Math.cos(Math.PI*d/W));

        // TODO Auto-generated method stub
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius;
        radius = 100;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        drawCurve(canvas);
        drawTicks(canvas);

    }

    public void doTitle() {
    }

    public void doColors() {
    }

//    private void drawCurve(Canvas canvas,Paint paint){
//
//        System.out.println("DRAW CURVE...");
//
//        int down = curve[(int) adaptSunset(model.sunset(),W) /K+DELTA] - DELTA - H1; //
//        paint.setColor(Color.RED);
//        int x1 = 0; int y1 = curve[x1] - down;
//        while (x1 < W) {
//            int x2 = x1+8;
//
//            if( x2 >= curve.length )
//                break;
//
//            int y2 = curve[x2] - down;
//            System.out.println("DRAW LINE : "+ (W+x1) +","+ (y1)+","+(W+x2)+","+(y2) );
//
//            canvas.drawLine(W+x1, y1, W+x2, y2,paint);
//            canvas.drawLine(W-x1, y1, W-x2, y2, paint);
//
////            if (y2 > H1) break;
//
//            x1 = x2; y1 = y2;
//        }
////        drawClock(g, false, down);
//        int y = curve[Math.abs(x)] - down;
//        //System.out.printf("x=%s y=%s \n", x, y);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(NOON);
//
//        System.out.println("drawOval : " + (W + x - DELTA / 2f) + "," + (y - DELTA / 2f) + "," + ((float) DELTA) + "," + ((float) DELTA));
//
//        RectF oval = new RectF(W+x-DELTA/2f, y-DELTA/2f, 16f, 16f);
//
//
////        canvas.drawOval( W+x-DELTA/2f, y-DELTA/2f, (float)DELTA, (float)DELTA,paint);
////        canvas.drawOval( W+x-DELTA/2f, y-DELTA/2f, 16f, 16f,paint);
//
//        canvas.drawOval(oval,paint);
//
////        canvas.drawOval( 0, 0 , 16, 16,paint);
//
//    }

    private void drawCurve(Canvas canvas){
        System.out.println("DRAW CURVE...");

        System.out.println( "imsak : "+ model.v_imsak() );
        System.out.println( "yatsi : "+ model.v_yatsi() );
        int halfWidth = getWidth()/2;
        int beforeNoonCurveStartX = halfWidth +((halfWidth* model.v_imsak())/720);
        int afterNoonCurveStartX = halfWidth -((halfWidth* model.v_yatsi())/720);

        Paint p = new Paint();

        System.out.println( beforeNoonCurveStartX + "," +(getHeight()-50) + ","+ halfWidth +","+(getHeight()-1));
        RectF rectF1 = new RectF( beforeNoonCurveStartX , getHeight()-201 , getWidth()-beforeNoonCurveStartX, getHeight()-1);
        RectF rectF2 = new RectF( afterNoonCurveStartX , getHeight()-201 , getWidth()-afterNoonCurveStartX, getHeight()-1);

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF1, 180, 90, false, p);
        canvas.drawArc(rectF2, -90, 90, false, p);
    }

    private void drawTicks(Canvas canvas){
        System.out.println("DRAW HOURS...");
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setTextSize(9f);

        int halfWidth = getWidth()/2;
        int twelve = halfWidth+ (getWidth()* model.getTwelve())/720 ;
        int hour = getWidth()/24;

        System.out.println("twelve : "+ twelve);
        System.out.println("hour : "+ hour );

        for(int i=twelve,h=12; i>-5; i=i-hour,h--){
//            System.out.println("Tick "+i+" : "+ (i-1) +","+ (getHeight()- 20) +","+ (i+1) +"," + (getHeight()-4));
//            RectF hourTick = new RectF( i-1 , getHeight()- 20 , i+1, getHeight()-4);
            canvas.drawLine(i,getHeight()- 30, i, getHeight()-14, p);
            canvas.drawText(""+(h%24),i-3,getHeight()-12,p);
        }

        for(int i=twelve+hour,h=13; i<getWidth()+5; i=i+hour,h++){
//            System.out.println("Tick "+i+" : "+ (i-1) +","+ (getHeight()- 20) +","+ (i+1) +"," + (getHeight()-4));
//            RectF hourTick = new RectF( i-1 , getHeight()- 20 , i+1, getHeight()-4);
            canvas.drawLine(i,getHeight()- 30, i, getHeight()-14, p);
            canvas.drawText(""+(h%24),i-3,getHeight()-12,p);
        }
    }

    private int adaptSunset(int sunset,int W){
        return(int)(sunset*W/720);
    }
}