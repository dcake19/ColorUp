package com.example.android.colorup;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.example.android.colorup.game.GameActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectGameActivity extends AppCompatActivity {

    @BindView(R.id.game_title) TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_game_activity);
        ButterKnife.bind(this);

        setTitle();
    }

    private void setTitle(){
        Spannable wordtoSpan = new SpannableString("Color Up");
        int colorCounter = 0;
        for(int i=0;i<wordtoSpan.length();i++){
            if(wordtoSpan.charAt(i)!=' ') colorCounter++;
            wordtoSpan.setSpan(new ForegroundColorSpan(getLetterColor(colorCounter)), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        mTitle.setText(wordtoSpan);
    }

    private int getLetterColor(int i){
        int color;
        int resourceId;

        String value = String.valueOf(i);

        try {
            resourceId = R.color.class.getField("colorTitle"+value).getInt(null);
        } catch (IllegalAccessException e) {
           resourceId = R.color.colorBackground;
        } catch (NoSuchFieldException e) {
            resourceId = R.color.colorBackground;
        }

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
            color = getResources().getColor(resourceId);
        }else {
            color = getApi23LetterColor(resourceId);
        }

        return color;

    }

    @TargetApi(23)
    private int getApi23LetterColor(int colorId){
        return getColor(colorId);
    }

    @OnClick({R.id.select_game_3x3,R.id.select_game_4x4,R.id.select_game_5x5,R.id.select_game_6x6,R.id.select_game_7x7,R.id.select_game_8x8})
    public void openGame(TextView textView){
        String text = (String) textView.getText();

        startActivity(GameActivity.getIntent(this,
                Integer.valueOf(text.substring(0,1)),
                Integer.valueOf(text.substring(text.length()-1,text.length()))));
    }
}
