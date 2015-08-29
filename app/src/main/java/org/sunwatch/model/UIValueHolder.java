package org.sunwatch.model;

import android.content.res.Configuration;
import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by gungor on 22/08/15.
 */
public class UIValueHolder {

    public static final String
            MSG = "Günümüz büyle geçiyor",
            FILE = "Vakit.txt",
            TITLE = " için güneş saati -- ", VER = "Va0",
            TIP = "<HTML>Mavi: sabah-akşam <BR>Sarı: Öğle-ikindi "
                    +"<BR>Siyah: yatsı <BR>Kırmızı: kerahat";

    public static final boolean WIDE = false;
    public static int
            K = WIDE? 1 : 2,
            W ,  //half width
            GAP = WIDE? 15 : 10,
            M = WIDE? 135 : 90, //used in cosine curve
            H1 = WIDE? 300 : 200,
            H2 = WIDE? 45 : 30,
            DELTA = WIDE? 12 : 8,
            SIZE = WIDE? 18 : 12;

    public static final int
            BLACK = Color.BLACK,
            BLUE  = Color.BLUE,
            RED   = Color.RED,
            NOON  = Color.YELLOW, //yellow
            DARK  = Color.GRAY;  //dark yellow
    public static int[] curve;
    public static final String[] d2s = new String[24];
    public static final SimpleDateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");

    public static int[] col;
    public static int[] shade;
    public final static Vakit model = new Vakit();

    public static int state = 1;  // s<0 -> fast,  s=0 -> clock,  s>0 count down
    public static int x;  // pixels since noon  -W<x<W  -- K*x in minutes
    public static String alfa; // altitude -- uses C0, C1

    public static void init(int width){


        W = width/2;
        curve = new int[W];
        col = new int[W];
        shade = new int[W];

        float scale = W/360f;


        K= (int)( 2 /scale);
        GAP = (int)(10*scale);
        M = (int)(90*scale); //used in cosine curve
        H1 = (int)(200*scale);
        H2 = (int)(30*scale);
        DELTA = (int)(8*scale);
        SIZE = (int)(12*scale);

        // String[] used in drawClock()
        for (int i=0; i<10; i++)
            UIValueHolder.d2s[i] = "0"+i;
        for (int i=10; i<24; i++)
            UIValueHolder.d2s[i] = ""+i;
        // cosine curve used in drawCurve()
        for (int d=0; d<W; d++)
            UIValueHolder.curve[d] = UIValueHolder.H1 - (int)Math.round(UIValueHolder.M*Math.cos(Math.PI*d/UIValueHolder.W));

        UIValueHolder.DATE.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

}
