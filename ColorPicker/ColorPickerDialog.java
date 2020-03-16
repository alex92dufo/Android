//Auteur: Jules Cohen (p1102520), Jean-François Blanchette (20030091), Alexandre Dufour (p1054564)

package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.colorpicker.Views.AreaPicker;
import com.example.colorpicker.Views.GradientSeekBar;
import com.example.colorpicker.Views.HSeekBar;

class ColorPickerDialog extends AlertDialog {
    private final static int MAX_RGB_VALUE = 255;
    private final static int MAX_SV_VALUE = 100;
    private final static int MAX_H_VALUE = 360;
    private static int cMax, cMin;
    private static int r, g, b = 1;
    private AreaPicker seekSV;
    private SaturationValueGradient saturationValueGradient;
    private HSeekBar seekH;
    private GradientSeekBar seekRed;
    private GradientSeekBar seekBlue;
    private GradientSeekBar seekGreen;
    private Button ok;
    private Button cancel;
    private OnColorPickedListener onPicked;
    private ColorPickerDialog colorPicker;
    private boolean rMax = false;
    private boolean gMax = false;
    private boolean bMax = false;

    ColorPickerDialog(Context context) {
        super(context);
        init(context);
    }

    ColorPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    ColorPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context){

        // Initialize dialog
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_picker, null);
        setView(view);

        // Initialize SV gradient
        seekSV = view.findViewById(R.id.seekSV);
        saturationValueGradient = new SaturationValueGradient();
        seekSV.setInsetDrawable(saturationValueGradient);


        // Exemple pour afficher un gradient SV centré sur du rouge pur.
        saturationValueGradient.setColor(Color.RED);


        //Modification des couleurs des gradients.
        seekH = view.findViewById(R.id.seekH);

        seekRed = view.findViewById(R.id.seekR);
        seekRed.setGradient(new int[] {
                0xff000000,
                0xFFff0000 });

        seekGreen = view.findViewById(R.id.seekG);
        seekGreen.setGradient(new int[] {
                0xff000000,
                0xff00ff00 });

        seekBlue = view.findViewById(R.id.seekB);
        seekBlue.setGradient(new int[] {
                0xff000000,
                0xff0000ff });

        setColor(getContext().getColor(R.color.defaultColor));
        seekH.setOnSeekBarChangeListener(listenerH);
        seekRed.setOnSeekBarChangeListener(listenerRed);
        seekGreen.setOnSeekBarChangeListener(listenerGreen);
        seekBlue.setOnSeekBarChangeListener(listenerBlue);

        seekSV.setOnPickedListener(listenerAP);

        colorPicker = this;
        onPicked = null;

        ok = view.findViewById(R.id.ok_but);
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if (onPicked != null) {
                    onPicked.onColorPicked(colorPicker, colorPicker.getColor());
                }
                dismiss();
            }
        });

        cancel = view.findViewById(R.id.cancel_but);
        cancel.setOnClickListener ( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                seekRed.setProgress(0);
                seekBlue.setProgress(0);
                seekGreen.setProgress(0);
                seekH.setProgress(0);

                dismiss();
            }
        });

        //Position initiale du curseur dans le AreaPicker
        seekSV.setPickedX(0);
        seekSV.setPickedY(MAX_SV_VALUE);

        // Default color
        setColor(getContext().getColor(R.color.defaultColor));

    }


    //Écoute les changements de S et V du Area Picker
    AreaPicker.OnPickedListener listenerAP = new AreaPicker.OnPickedListener() {
        @Override
        public void onPicked(AreaPicker areaPicker, int x, int y, boolean fromUser) {

            updateRGB(HSVtoRGB(getH(), x, Math.abs(y-MAX_SV_VALUE)));

        }
    };


    //Écoute les changements de la barre H
    SeekBar.OnSeekBarChangeListener listenerH = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//            setH(progress);
            saturationValueGradient.setColor(Color.HSVToColor(new float[]{progress, MAX_SV_VALUE, MAX_SV_VALUE}));

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };


    //Écoute les changements de la barre rouge
    SeekBar.OnSeekBarChangeListener listenerRed = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            setColor(Color.rgb(progress, g, b));

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };



    //Écoute les changements de la barre verte
    SeekBar.OnSeekBarChangeListener listenerGreen = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            setColor(Color.rgb(r ,progress, b));

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };


    //Écoute les changements de la barre bleue
    SeekBar.OnSeekBarChangeListener listenerBlue = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            setColor(Color.rgb(r, g, progress));

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };


    @ColorInt
    int getColor(){
        return Color.rgb(r, g, b);
    }

    public int getH(){
        return RGBtoHSV(r, g, b)[0];
    }

    public void setH(int h){
        int[] hsv = RGBtoHSV(r, g, b);
        updateRGB(HSVtoRGB(h, hsv[1], hsv[2] ));
    }

    public void setColor(@ColorInt int newColor){

        updateRGB(new int[]{Color.red(newColor), Color.green(newColor), Color.blue(newColor)});

    }

    //mise à jour des valeur de r, g et b
    public void updateRGB (int[]rgb){
        r = rgb[0];
        g = rgb[1];
        b = rgb[2];

        updateSBAP();
    }

    //mise à jour des seekbar et du area picker
    public void updateSBAP(){


        int [] hsv = RGBtoHSV(r, g, b);

        seekH.setProgress(hsv[0]);

        saturationValueGradient.setColor(Color.HSVToColor(new float[]{hsv[0], MAX_SV_VALUE, MAX_SV_VALUE}));
        seekSV.setPickedX(hsv[1]);
        seekSV.setPickedY((Math.abs(hsv[2]-MAX_SV_VALUE)));


        seekRed.setProgress(r);
        seekRed.setGradient(new int[]{
                Color.rgb(0, g, b),
                Color.rgb(MAX_RGB_VALUE, g, b)
        });

        seekGreen.setProgress(g);
        seekGreen.setGradient(new int[]{
                Color.rgb(r, 0,b),
                Color.rgb(r, MAX_RGB_VALUE, b)
        });

        seekBlue.setProgress(b);
        seekBlue.setGradient(new int[]{
                Color.rgb(r, g,0),
                Color.rgb(r, g, MAX_RGB_VALUE)
        });
    }


    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        this.onPicked = onColorPickedListener;
    }


    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);

    }



    private int[] HSVtoRGB(int h, int s, int v){

        int hp,sp,vp,c,d;
        float x,rp,bp,gp;
        int [] tableau = new int[3];

        hp=(100*h)/60;
        sp=(100*s)/100;
        vp=(100*v)/100;
        c = (sp*vp)/100;
        d = vp-c;
        x = 1-Math.abs((hp%2)-1);
        rp = getRP(hp,x);
        gp = getGP(hp,x);
        bp = getBP(hp,x);

        int red = (int)(((c) * rp +d)*2.55);
        int green = (int)(((c)*gp +d)*2.55);
        int blue = (int)(((c)*bp+d)*2.55);


        tableau[0]=red; tableau[1]=green; tableau[2]=blue;

        return tableau;
    }

    private int[] RGBtoHSV(int r, int g, int b){
        int h,s,v;
        int [] tableau = new int[3];
        int hp = hPrime(r,g,b);
        int d = cMax-cMin;

        if (hp >= 0){
            h = (60*hp);
        }
        else {
            h = (6000*(hp + 6))/100;
        }

        v = (100*cMax)/255;

        if (cMax != 0) {
            s = (100 * d) / cMax;
        }
        else
            s=0;

        tableau[0]=h; tableau[1]=s; tableau[2]=v;

        return tableau;
    }

    private int hPrime(int r, int g, int b){
        int hp = 0 ;
        cMax=max(r,g,b);
        cMin=min(r,g,b);

        int d = cMax-cMin;

        if (d == 0)
            hp=0;

        else if (cMax == r)
            hp = (g-b)/d;
        else if (cMax == g)
            hp = 2 + (b-r)/d;

        else if (cMax == b)
            hp = 4 + (r-g)/d;

        return hp;
    }

    private int max(int r,int g, int b){
        int x = Math.max(r,g);
        int y = Math.max(x,b);

        return y;
    }

    private static int min(int r,int g,int b){
        int x = Math.min(r,g);
        int y = Math.min(x,b);

        return y;
    }


    private static float getRP(int hp, float x){
        float rp=0;

        if (hp >= 0 && hp <= 100 || hp > 500 && hp <= 600)
            rp=1;

        else if (hp > 100 && hp <= 200 || hp > 400 && hp <= 500)
            rp=x;

        return rp;
    }
    private static float getGP(int hp, float x){
        float gp=0;

        if (hp >= 0 && hp <= 100 || hp > 300 && hp <= 400)
            gp=x;

        else if (hp > 100 && hp <= 300)
            gp=x;

        return gp;
    }

    private static float getBP(int hp, float x){
        float bp=0;

        if (hp >200 && hp <= 300 || hp > 500 && hp <= 600)
            bp=x;

        else if (hp > 300 && hp <= 500)
            bp=1;

        return bp;
    }


}
