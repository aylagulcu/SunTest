package org.sunwatch.model;

import android.graphics.Color;

import java.text.SimpleDateFormat;

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
    public static final int
            K = WIDE? 1 : 2,
            W = 720/K,  //half width
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
    public static final int[] curve = new int[W];
    public static final String[] d2s = new String[24];
    public static final SimpleDateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");

    public final static int[] col = new int[UIValueHolder.W];
    public final static int[] shade = new int[UIValueHolder.W];
    public final static Vakit model = new Vakit();

    public static int state = 1;  // s<0 -> fast,  s=0 -> clock,  s>0 count down
    public static int x;  // pixels since noon  -W<x<W  -- K*x in minutes
    public static String alfa; // altitude -- uses C0, C1

}
