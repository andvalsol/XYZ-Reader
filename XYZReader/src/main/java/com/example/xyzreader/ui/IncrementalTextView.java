package com.example.xyzreader.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MotionEvent;

import com.example.xyzreader.R;

/*
 * Set a custom text view widget, so that we can update the text view by chunks
 * */

public class IncrementalTextView extends AppCompatTextView {
    
    private int mIncrementalSize;
    private String mText;
    
    //For click handling
    boolean touchOn;
    boolean mDownTouch = false;
    
    public IncrementalTextView(Context context) {
        super(context);
    }
    
    public IncrementalTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        
        initView(context, attrs);
    }
    
    public IncrementalTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        
        initView(context, attrs);
    }
    
    /*
     * When a view is created, the attributes in the XML are read from a bundle resource and passed into the view's constructor
     * as the AttributeSet
     * */
    private void initView(Context context, AttributeSet attributeSet) {
        touchOn = false;
        
        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attributeSet, R.styleable.IncrementalTextView, 0, 0);
        
        try {
            //Get the incremental size set
            mIncrementalSize = typedArray.getInteger(R.styleable.IncrementalTextView_incrementalSize, 800);
            
            //Get the global text
            mText = typedArray.getString(R.styleable.IncrementalTextView_globalText);
            
        } finally {
            //Recycle the typedArray
            typedArray.recycle();
        }
    }
    
    public void setGlobalText(String globalText) {
        mText = globalText;
    }
    
    /*
     * Make an incrementer method so that it gets the desired text and append it with an incremental load set by the user
     * */
    private int textIncrementer() {
        if (((mText.length() - getText().length()) / mIncrementalSize) > 1)
            return (getText().length() + mIncrementalSize);
        
        return getText().length();
    }
    
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        
        // Listening for the down and up touch events
        switch (event.getAction()) {
            
            case MotionEvent.ACTION_DOWN:
                
                touchOn = !touchOn;
                invalidate();
                
                mDownTouch = true;
                
                return true;
            
            case MotionEvent.ACTION_UP:
                if (mDownTouch) {
                    mDownTouch = false;
                    
                    performClick();
                    
                    return true;
                }
        }
        
        return false; // Return false for other touch events
    }
    
    @Override
    public boolean performClick() {
        super.performClick();
        
        int textLength = getText().length();
        int textIncrementer = textIncrementer();
        
        Log.d("Length", "The length of the text is: " + textLength + ", and the textIncrementer() return int is: " + textIncrementer);
        
        
        //Append the text incrementally
        append(mText.substring(textLength, textIncrementer));
        
        return true;
    }
}