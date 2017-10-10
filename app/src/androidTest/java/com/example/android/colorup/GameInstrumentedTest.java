package com.example.android.colorup;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.android.colorup.game.GameActivity;
import com.example.android.colorup.game.GameContract;
import com.example.android.colorup.game.GameModule;
import com.example.android.colorup.idlingResource.SimpleIdlingResource;
import com.example.android.colorup.model.Coordinates;
import com.example.android.colorup.model.GameBoard;
import com.example.android.colorup.model.UpdateSquare;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

//import static org.powermock.api.support.membermodification.MemberMatcher.method;
//import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class GameInstrumentedTest {

    private GameContract.Presenter mMockPresenter;

    @Rule
    public ActivityTestRule<GameActivity> mActivityTestRule =
            new ActivityTestRule<GameActivity>(GameActivity.class,false,false);

    private IdlingResource mIdlingResource;

    @Before
    public void setUp(){
        GameModule mockGameModule = mock(GameModule.class);
        mMockPresenter = mock(GameContract.Presenter.class);

        when(mockGameModule.provideGamePresenter(any(GameBoard.class)))
                .thenReturn(mMockPresenter);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestColorUpApplication app = (TestColorUpApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setGameModule(mockGameModule);

    }

    @Test
    public void testSwipe() {
        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.game_board_view)).perform(swipeLeft());
        onView(withId(R.id.game_board_view)).perform(swipeRight());
        onView(withId(R.id.game_board_view)).perform(swipeUp());
        onView(withId(R.id.game_board_view)).perform(swipeDown());
        // there should have been no interactions with the presenter at this point
        verify(mMockPresenter, Mockito.times(0)).swipe(any(Integer.class));

        GameActivity gameActivity = (GameActivity) mActivityTestRule.getActivity();
        gameActivity.allowFling();
        onView(withId(R.id.game_board_view)).perform(swipeLeft());
        verify(mMockPresenter).swipe(GameBoard.DIRECTION_LEFT);
        gameActivity.allowFling();
        onView(withId(R.id.game_board_view)).perform(swipeRight());
        verify(mMockPresenter).swipe(GameBoard.DIRECTION_RIGHT);
        gameActivity.allowFling();
        onView(withId(R.id.game_board_view)).perform(swipeUp());
        verify(mMockPresenter).swipe(GameBoard.DIRECTION_UP);
        gameActivity.allowFling();
        onView(withId(R.id.game_board_view)).perform(swipeDown());
        verify(mMockPresenter).swipe(GameBoard.DIRECTION_DOWN);

        onView(withId(R.id.game_board_view)).perform(swipeLeft());
        onView(withId(R.id.game_board_view)).perform(swipeRight());
        onView(withId(R.id.game_board_view)).perform(swipeUp());
        onView(withId(R.id.game_board_view)).perform(swipeDown());
        // should only record 4 swipes as fling has not been allowed
        verify(mMockPresenter, Mockito.times(4)).swipe(any(Integer.class));


    }

    @Test
    public void testClickUndoButton()throws InterruptedException{
        mActivityTestRule.launchActivity(new Intent());
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
        //onView(withId(R.id.btn_undo)).check(matches(isClickable()));
        onView(withId(R.id.btn_undo)).perform(click());
        verify(mMockPresenter, Mockito.times(0)).undo();

        final int[][] savedBoard = {{1,3,3,0},{0,4,1,0},{0,0,3,0},{1,2,0,8}};

        final int[][] newBoard = {{0,0,1,4},{0,0,4,1},{0,0,0,3},{0,1,2,8}};

        final ArrayList<UpdateSquare> updates = new ArrayList<>();
        updates.add(new UpdateSquare(new Coordinates(0,2),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(0,1),2,GameBoard.DIRECTION_RIGHT,true));
        updates.add(new UpdateSquare(new Coordinates(0,0),2,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(1,2),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(2,2),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(3,1),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(3,0),1,GameBoard.DIRECTION_RIGHT,false));

        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        mActivityTestRule.getActivity().setNotIdle();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivityTestRule.getActivity().setSavedBoard(savedBoard);
                mActivityTestRule.getActivity().update(updates,newBoard,GameBoard.DIRECTION_RIGHT);
            }});
        onView(withId(R.id.btn_undo)).perform(click());
        verify(mMockPresenter).undo();

    }

    @Test
    public void testClickRestartButton(){
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.btn_restart)).perform(click());
        verify(mMockPresenter).restart();
        onView(withId(R.id.score)).check(matches(withText("0")));
    }

    @Test
    public void testRestartUpdate(){
        mActivityTestRule.launchActivity(new Intent());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivityTestRule.getActivity().restartUpdate(new Coordinates(2,3),new Coordinates(1,0));
            }});
        int[][] board = {{0,0,0,0},{1,0,0,0},{0,0,0,1},{0,0,0,0}};
        onView(withId(R.id.game_board_view)).check(matches(GameBoardViewMatcher.withBoard(board)));
    }

    @Test
    public void testAddNewSquare(){
        mActivityTestRule.launchActivity(new Intent());
        final int[][] savedBoard = {{1,2,3,0},{0,4,1,0},{0,0,3,0},{1,2,0,8}};

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivityTestRule.getActivity().setSavedBoard(savedBoard);
                mActivityTestRule.getActivity().addNewSquare(2, 3);
            }});

        int[][] newBoard = {{1,2,3,0},{0,4,1,0},{0,0,3,1},{1,2,0,8}};
        onView(withId(R.id.game_board_view)).check(matches(GameBoardViewMatcher.withBoard(newBoard)));
    }

    @Test
    public void testSetSavedBoard(){
        mActivityTestRule.launchActivity(new Intent());
        final int[][] savedBoard = {{1,2,3,0},{0,4,1,0},{0,0,3,0},{1,2,0,8}};
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivityTestRule.getActivity().setSavedBoard(savedBoard);
            }});
        onView(withId(R.id.game_board_view)).check(matches(GameBoardViewMatcher.withBoard(savedBoard)));
    }


    @Test
    public void testUpdates()throws InterruptedException{
        mActivityTestRule.launchActivity(new Intent());

        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);

        final int[][] savedBoard = {{1,3,3,0},{0,4,1,0},{0,0,3,0},{1,2,0,8}};

        final int[][] newBoard = {{0,0,1,4},{0,0,4,1},{0,0,0,3},{0,1,2,8}};

        final ArrayList<UpdateSquare> updates = new ArrayList<>();
        updates.add(new UpdateSquare(new Coordinates(0,2),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(0,1),2,GameBoard.DIRECTION_RIGHT,true));
        updates.add(new UpdateSquare(new Coordinates(0,0),2,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(1,2),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(1,1),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(2,2),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(3,1),1,GameBoard.DIRECTION_RIGHT,false));
        updates.add(new UpdateSquare(new Coordinates(3,0),1,GameBoard.DIRECTION_RIGHT,false));

        mActivityTestRule.getActivity().setNotIdle();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mActivityTestRule.getActivity().setSavedBoard(savedBoard);
                mActivityTestRule.getActivity().update(updates,newBoard,GameBoard.DIRECTION_RIGHT);
            }};

       InstrumentationRegistry.getInstrumentation().runOnMainSync(runnable);

       onView(withId(R.id.game_board_view)).check(matches(GameBoardViewMatcher.withBoard(newBoard)));
    }

    @Test
    public void testDisplayScores(){
        mActivityTestRule.launchActivity(new Intent());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivityTestRule.getActivity().setHighScore("300");
                mActivityTestRule.getActivity().setScore("50");
            }});
        onView(withId(R.id.high_score)).check(matches(withText("300")));
        onView(withId(R.id.score)).check(matches(withText("50")));
    }

    @Test
    public void testGameOver(){
        mActivityTestRule.launchActivity(new Intent());
        final int[][] savedBoard = {{1,2,3,0},{0,4,1,0},{0,0,3,0},{1,2,0,8}};

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivityTestRule.getActivity().setSavedBoard(savedBoard);
                mActivityTestRule.getActivity().gameOver();
            }});

        onView(withId(R.id.game_board_view)).check(matches(GameBoardViewMatcher.withBoard(savedBoard)));
        onView(withId(R.id.game_board_view)).check(matches(GameBoardViewMatcher.isGameOver(true)));
    }

    @Test
    public void testUndoBoard() {
        mActivityTestRule.launchActivity(new Intent());
        final int[][] savedBoard = {{0,0,1,4},{0,0,4,1},{0,0,0,3},{0,1,2,8}};
        final int[][] newBoard = {{1,2,3,0},{0,4,1,0},{0,0,3,0},{1,2,0,8}};

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {

                mActivityTestRule.getActivity().setSavedBoard(savedBoard);
                mActivityTestRule.getActivity().undoUpdate(newBoard);
            }});

        onView(withId(R.id.game_board_view)).check(matches(GameBoardViewMatcher.withBoard(newBoard)));
        onView(withId(R.id.game_board_view)).check(matches(GameBoardViewMatcher.isGameOver(false)));
    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
