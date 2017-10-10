package com.example.android.colorup;


import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.example.android.colorup.gameboardview.GameBoardView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

//import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;


public class GameBoardViewMatcher {


    public static Matcher<View> withBoard(int[][] board) {
        return withBoard(is(board));
    }

    private static Matcher<View> withBoard(final Matcher<int[][]> boardMatcher) {
        //checkNotNull(stringMatcher);
        return new BoundedMatcher<View, GameBoardView>(GameBoardView.class) {
            @Override
            public void describeTo(Description description) {
                boardMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(GameBoardView view) {
                return boardMatcher.matches(view.getBoard());
            }
        };
    }

    public static Matcher<View> isGameOver(boolean gameOver) {
        return isGameOver(is(gameOver));
    }

    private static Matcher<View> isGameOver(final Matcher<Boolean> booleanMatcher) {
        //checkNotNull(stringMatcher);
        return new BoundedMatcher<View, GameBoardView>(GameBoardView.class) {
            @Override
            public void describeTo(Description description) {
                booleanMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(GameBoardView view) {
                return booleanMatcher.matches(view.isGameOver());
            }
        };
    }
}
