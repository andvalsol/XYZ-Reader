package com.example.xyzreader.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.example.xyzreader.R;

/*
 * Set a custom text view widget, so that we can update the text view by chunks
 * */

public class IncrementalTextView extends AppCompatTextView {
    
    private int mIncrementalSize;
    private String mText;
    
    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        
        @Override
        public void afterTextChanged(Editable s) {
            //Check that the text is not empty
            if (!s.toString().equals(getContext().getString(R.string.empty_text))) {
                //Set view more logic needed
                viewMoreLogic();
            }
        }
    };
    
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
        
        //Detect when the setText has been set
        addTextChangeListener();
    }
    
    private void viewMoreLogic() {
        //Proceed to remove the text change listener
        removeTextChangeListener();
        
        //Get the text so that we can append the "... view more" string
        if (isViewMoreTextNeeded()) setSpannableViewMore();
    }
    
    private void setSpannableViewMore() {
        String text = getContext().getString(R.string.view_more);
        
        int end = text.length();
        
        Log.d("TextView", "The text that is currently in the text view is: " + getText());
        
        //Add ... view more text
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new SpannableViewMore(), 1, end, 0);
        append(spannableString);
        
        setMovementMethod(new LinkMovementMethod());
    }
    
    private void addTextChangeListener() {
        addTextChangedListener(mTextWatcher);
    }
    
    private void removeTextChangeListener() {
        removeTextChangedListener(mTextWatcher);
    }
    
    private boolean isViewMoreTextNeeded() {
        return (mText.length() > getText().length());
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
    
    private class SpannableViewMore extends ClickableSpan {
        
        @Override
        public void onClick(View widget) {
            //TODO Remove the ClickableSpan
            
            //Add the text change listener
            addTextChangeListener();
            
            //Append the text
            appendText(mText.substring(getText().length(), textIncrementer()));
        }
    }
    
    private void appendText(String text) {
        //Append the text incrementally
        append(text);
    }
}