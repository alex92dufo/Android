package com.example.colorpicker.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class GradientSeekBar extends AppCompatSeekBar {

    public ShapeDrawable.ShaderFactory sf;
    GradientDrawable gd;

    public GradientSeekBar(Context context) {
        super(context);
        init();
    }

    public GradientSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){

        setMax(255);
        setOnSeekBarChangeListener(listener);


    }

    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateColor();
            System.out.println("progress = " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    void updateColor(){
        int progress = getProgress();
        gd.setColor(Color.rgb(progress, progress, progress));
    }

    //TO DO
    public void changeColor(int r, int g, int b){

    }

    public void setGradient(int[] colors){

        sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient rbGradient = new LinearGradient(0, 0, width, height,
                        colors,
                        new float[] {
                                0,1 },
                        Shader.TileMode.REPEAT);
                return rbGradient;
            }
        };

        PaintDrawable color = new PaintDrawable();
        color.setShape(new RectShape());
        color.setShaderFactory(sf);
        this.setProgressDrawable(color);
    }
}

