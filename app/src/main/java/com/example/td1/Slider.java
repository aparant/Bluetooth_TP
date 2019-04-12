package com.example.td1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Slider extends View {
    final static float DEFAULT_BAR_WIDTH=10;
    final static float DEFAULT_BAR_LENGTH=100;
    final static float DEFAULT_CURSOR_DIAMETER=20;

    //Valeur du slider
    private float mMin=0;
    private float mMax=100;
    private float mValue=0;

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

    private boolean mEnabled =true;


    private SliderChangeListener mSliderChangeListener;

    /**
     * Constructeur statique du Slider utilisant un constructeur de View
     * @param context
     */
    public Slider(Context context){
        super(context);
        init(context,null);
    }

    /**
     * Constructeur dynamique du Slider utilisant un constructeur de View
     * @param context
     * @param attrs
     */
    public Slider(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    public void setSliderChangeListener(SliderChangeListener listener){
        mSliderChangeListener=listener;
    }

    /**
     * Accesseur de la valeur du Slider
     * @return valeur du Slider
     */
    public float getValue(){
        return mValue;
    }


    /**
     * Transforme la valeur en entrée en ratio
      * @param value
     * @return Le ratio de la valeur en entrée par rapport au min max définit
     */
    private float valueToRatio(float value){
        return (value-mMin)/(mMax-mMin);
    }

    /**
     * Retourne une valeur selon le ration en entrée
     * @param ratio
     * @return La valeur selon le ration en entrée
     */
    private float ratioToValue(float ratio){
        return ratio*(mMax-mMin)+mMin;
    }

    /**
     * Permet d'avoir les coordonnées de la valeur en entrée sur le Slider
     * @param value
     * @return Coordonnées d'un point sur le Slider
     */
    private Point toPos(float value){
        int x,y;
        x=(int)Math.max(mCursorDiameter,mBarWidth)/2+getPaddingLeft();
        y=(int)((1-valueToRatio(value))*mBarLength+mCursorDiameter/2)+getPaddingTop();
        return new Point(x,y);
    }

    /**
     * Permet d'avoir la valeur du Slider selon les coordonnées du point du Slider
     * @param position du curseur du Slider
     * @return la valeur du Slider
     */
    private float toValue(Point position){
        float ratio;
        ratio=1-(position.y-getPaddingTop()-mCursorDiameter/2)/mBarLength;
        return ratioToValue(ratio);
    }

    /**
     * Permet d'avoir la taille voulue du Slider pour l'afficher en entier
     * @param widthMeasureSpec largeur voulue du Slider
     * @param heightMeasureSpec hauteur voulue du Slider
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int suggestedWidth, suggestedHeigth;
        int width,height;
        suggestedWidth=Math.max((int)DEFAULT_BAR_WIDTH,(int)DEFAULT_CURSOR_DIAMETER);
        suggestedWidth=Math.max(getSuggestedMinimumWidth(),suggestedWidth+getPaddingLeft()+getPaddingRight());
        suggestedHeigth=Math.max(getSuggestedMinimumHeight(),(int)DEFAULT_BAR_LENGTH+getPaddingTop()+getPaddingBottom());
        width= resolveSize(suggestedWidth,suggestedHeigth);
        height=resolveSize(suggestedHeigth,suggestedWidth);

        setMeasuredDimension(width,height);
    }

    /**
     * Permet de redessiner le Slider quand il y a un changement de valeur du curseur
     * @param event évènement de déplacement sur l'écran
     */
    private void updateSlider(MotionEvent event){
        float x=event.getX();
        float y=event.getY();
        Point touch=new Point((int)x,(int)y);
        mValue=toValue(touch);
        if(mValue>=100) mValue=100;
        if(mValue<=0) mValue=0;
    }

    /**
     * Permet d'effectuer les actions correspondantes à un évènement de toucher
     * @param event peut être un appui ou un déplacement de doigt sur l'écran
     * @return l'action à été effectuée
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        switch (action){
        case MotionEvent.ACTION_MOVE:
           updateSlider(event);
            invalidate();
            mSliderChangeListener.onChange();
            return true;
         case MotionEvent.ACTION_DOWN:
             return true;
         default:
            //return super.onTouchEvent(event);
            return false;
        }

    }

    /**
     * Permet de dessiner le Slider avec sa barre et son curseur
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Point p1,p2;
        Point posCircle;
        p1=toPos(mMin);
        p2=toPos(mMax);
        canvas.drawLine(p1.x,p1.y,p2.x,p2.y,mBarPaint);

        p2=toPos(mMin+10);
        canvas.drawLine(p2.x,p2.y,p1.x,p1.y,mValueBarPaint);

        posCircle=toPos(mValue);
        canvas.drawCircle(posCircle.x,posCircle.y,mCursorDiameter/2,mCursorPaint);

    }


    /**
     * Initialise le Slider(taille, couleur)
     * @param context
     * @param attrs
     */
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

        mDisableColor= ContextCompat.getColor(context,R.color.colorDisabled);
        mCursorColor=ContextCompat.getColor(context,R.color.colorAccent);
        mBarColor=ContextCompat.getColor(context,R.color.colorPrimary);
        mValueBarColor=ContextCompat.getColor(context,R.color.colorSecondary);

       if(mEnabled){
           mCursorPaint.setColor(mCursorColor);
           mBarPaint.setColor(mBarColor);
           mValueBarPaint.setColor(mValueBarColor);
       }
       else{
           mCursorPaint.setColor(mDisableColor);
       }

       mBarPaint.setStrokeWidth(mBarWidth);
       mValueBarPaint.setStrokeWidth(mBarWidth);

       //stocker longueur et largeur minimale que l'on souhaite
        setMinimumWidth((int)dpToPixel(DEFAULT_BAR_WIDTH+getPaddingLeft()+getPaddingRight()+DEFAULT_CURSOR_DIAMETER));
        setMinimumHeight((int)dpToPixel(DEFAULT_BAR_LENGTH+getPaddingTop()+getPaddingBottom()+DEFAULT_CURSOR_DIAMETER));
    }

    /**
     * Permet de passer d'une valeur en dp en piwel
     * @param valueInDp valeur en DP
     * @return valeur convertie en pixel
     */
    private float dpToPixel(float valueInDp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valueInDp,getResources().getDisplayMetrics());
    }



    @Override
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(),mValue);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)){
            super.onRestoreInstanceState(state);
            return;
        }
        mValue=((SavedState) state).sliderValue;
        super.onRestoreInstanceState(((SavedState)state).getSuperState());
        super.onRestoreInstanceState(state);
    }


    static class SavedState extends BaseSavedState{
        private float sliderValue;
        public static final  Parcelable.Creator<SavedState> CREATOR=
                new Parcelable.Creator<SavedState>(){
                    public SavedState createFromParcel(Parcel in){
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size){
                        return new SavedState[size];
                    }
                };
        private SavedState(Parcel source){
            super(source);
            sliderValue=source.readFloat();
        }
        public SavedState(Parcelable superState,float value){
            super(superState);
            sliderValue=value;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(sliderValue);
        }
    }

    public interface SliderChangeListener{
        void onChange();
    }
}
