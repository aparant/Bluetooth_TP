package com.example.td1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class Slider extends View {
    final static float DEFAULT_BAR_WIDTH=10;
    final static float DEFAULT_BAR_LENGTH=100;
    final static float DEFAULT_CURSOR_DIAMETER=10;

    //Valeur du slider
    private float mValue=0;
    private float mMin=0;
    private float mMax=100;

    // attribut de dimension
    private float mBarLength;
    private float mBarWidth;
    private float mCursorDiameter;

    private Paint mCursorPaint=null;
    private Paint mValueBarPaint=null;
    private Paint mBarPaint=null;

    //colorie des différents éléments graphiques

    private int mDisableColor;
    private int mCursorColor;
    private int mBarColor;
    private int mValueBarColor;

    private boolean mEnable=true;

    public Slider(Context context){
        super(context);
        init(context,null);
    }

    public Slider(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    // /** + espace pour avoir la javadoc
    /**
     * Transforme la valeur du slider en un ratio
     * @param value : valeur du slider
     * @return ratio entre 0 et 1
     */
    private float valueToRatio(float value){
        return (value-mMin)/(mMax-mMin);
    }

    private float ratioToValue(float ratio){
        return ratio*(mMax-mMin)+mMin;
    }

    private Point toPos(float value){
        int x,y;
        x=(int)Math.max(mCursorDiameter,mBarWidth)/2+getPaddingLeft();
        y=(int)(valueToRatio(value)*mBarLength+mCursorDiameter/2)+getPaddingTop();
        return new Point(x,y);
    }

    private float toValue(Point position){
        float ratio;
        ratio=1-(position.y-getPaddingTop()-mCursorDiameter/2)/mBarLength;
        return ratioToValue(ratio);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void init(Context context, AttributeSet attrs){
        mBarLength=dpToPixel(DEFAULT_BAR_LENGTH);
        mBarWidth=dpToPixel(DEFAULT_BAR_WIDTH);
        mCursorDiameter=dpToPixel(DEFAULT_CURSOR_DIAMETER);
        mCursorPaint=new Paint();
        mBarPaint=new Paint();
        mValueBarPaint=new Paint();

        //suppression de l'alliasing
        mCursorPaint.setAntiAlias(true);
        mBarPaint.setAntiAlias(true);
        mValueBarPaint.setAntiAlias(true);

       //remplissage
       mCursorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
       mBarPaint.setStyle(Paint.Style.STROKE);
       mValueBarPaint.setStyle(Paint.Style.STROKE);

       //arrondi angle
        mBarPaint.setStrokeCap(Paint.Cap.ROUND);

        mDisableColor= ContextCompat.getColor(context,R.color.colorAccent);
        mCursorColor=ContextCompat.getColor(context,R.color.colorAccent);
        mBarColor=ContextCompat.getColor(context,R.color.colorAccent);
        mValueBarColor=ContextCompat.getColor(context,R.color.colorAccent);

       if(mEnable){
           mCursorPaint.setColor(mCursorColor);
           mBarPaint.setColor(mCursorColor);
           mValueBarPaint.setColor(mValueBarColor);
       }
       else{
           mCursorPaint.setColor(mDisableColor);
       }

       mBarPaint.setStrokeWidth(mBarWidth);
       mValueBarPaint.setStrokeWidth(mBarWidth);
    }

    private float dpToPixel(float valueInDp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valueInDp,getResources().getDisplayMetrics());
    }
}
