package cse.challenge.tictactoe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TicTacToe mGame;
	
	private Button mBoardButtons[];
	
	private TextView mInfoTextView;
	private TextView mHumanCount;
	private TextView mAndroidCount;
	private TextView mTieCount;
	
	private int mHumanCounter = 0;
	private int mTieCounter = 0;
	private int mAndroidCounter = 0;
	
	private boolean mHumanFirst = true;
	private boolean mGameOver = false;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGame = new TicTacToe();
		
		mBoardButtons = new Button[mGame.getBOARD_SIZE()];
		mBoardButtons[0] = (Button) findViewById(R.id.one);
		mBoardButtons[1] = (Button) findViewById(R.id.two);
		mBoardButtons[2] = (Button) findViewById(R.id.three);
		mBoardButtons[3] = (Button) findViewById(R.id.four);
		mBoardButtons[4] = (Button) findViewById(R.id.five);
		mBoardButtons[5] = (Button) findViewById(R.id.six);
		mBoardButtons[6] = (Button) findViewById(R.id.seven);
		mBoardButtons[7] = (Button) findViewById(R.id.eight);
		mBoardButtons[8] = (Button) findViewById(R.id.nine);
		
		mInfoTextView = (TextView) findViewById(R.id.information);
		mHumanCount = (TextView) findViewById(R.id.humanCount);
		mTieCount = (TextView) findViewById(R.id.tieCount);
		mAndroidCount = (TextView) findViewById(R.id.androidCount);
		
		
		
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		  if (preferences.contains("humanCounter")) 
		  {
			  mHumanCounter = preferences.getInt("humanCounter", 0);
			  mTieCounter = preferences.getInt("tieCounter", 0);
			  mAndroidCounter = preferences.getInt("androidCounter", 0);
			 
			}
		  else
		  {
			  editor.putInt("humanCounter",mHumanCounter);
		   		editor.apply();
			  editor.putInt("tieCounter",mTieCounter);
		   		editor.apply();
			  editor.putInt("androidCounter",mAndroidCounter);
		   		editor.apply();
		  }
		  
		  mHumanCount.setText(Integer.toString(mHumanCounter));
		  mTieCount.setText(Integer.toString(mTieCounter));
		  mAndroidCount.setText(Integer.toString(mAndroidCounter));
		startNewGame();
	
		
	}
	
	private void startNewGame()
	{
		mGame.clearBoard();
		
		for (int i = 0; i < mBoardButtons.length; i++)
		{
			mBoardButtons[i].setText(" ");
			mBoardButtons[i].setEnabled(true);
			mBoardButtons[i].setOnClickListener( new ButtonClickListener(i));
		}
		
		if(mHumanFirst)
		{
			mInfoTextView.setText(R.string.first_human);
			mHumanFirst = false;
		}
		else
		{
			mInfoTextView.setText(R.string.turn_computer);
			int move = mGame.getComputerMove();
			setMove(mGame.ANDROID_PLAYER,move);
			mHumanFirst = true;
		}
	}
	
	private class ButtonClickListener implements View.OnClickListener
	{
		int location;
		
		public ButtonClickListener(int location)
		{
			this.location = location;
		}
		@Override
		public void onClick(View view)
		{
			if(!mGameOver)
			{
				if (mBoardButtons[location].isEnabled())
				{
					setMove(mGame.HUMAN_PLAYER,location);
					
					int winner = mGame.checkForWinner();
					
					if (winner == 0)
					{
						mInfoTextView.setText(R.string.turn_computer);
						int move = mGame.getComputerMove();
						setMove(mGame.ANDROID_PLAYER,move);
						winner = mGame.checkForWinner();
					}
					
					if (winner == 0)
					{
						mInfoTextView.setText(R.string.turn_human);
					}
					else if(winner ==1){
						mInfoTextView.setText(R.string.result_tie);
						mTieCounter++;
						editor.putInt("tieCounter",mTieCounter);
						editor.apply();
						mTieCount.setText(Integer.toString(mTieCounter));
						mGameOver = true;
					}
					else if(winner == 2)
					{
						mInfoTextView.setText(R.string.result_human_wins);
						mHumanCounter++;
						editor.putInt("humanCounter",mHumanCounter);
						editor.apply();
						mHumanCount.setText(Integer.toString(mHumanCounter));
						mGameOver = true;
						
					}
					else if(winner == 3)
					{
						mInfoTextView.setText(R.string.result_android_wins);
						mAndroidCounter++;
						editor.putInt("androidCounter",mAndroidCounter);
					    editor.apply();
						mAndroidCount.setText(Integer.toString(mAndroidCounter));
						mGameOver = true;
						
					}
				}
			}
			
		}
		
	}
	
	private void setMove(char player, int location)
	{
		mGame.setMove(player, location);
		mBoardButtons[location].setEnabled(false);
		mBoardButtons[location].setText(String.valueOf(player));
		
		if(player == mGame.HUMAN_PLAYER)
			mBoardButtons[location].setTextColor(Color.GREEN);
		else
			mBoardButtons[location].setTextColor(Color.RED);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if ( id == R.id.new_game){
			startNewGame();
			mGameOver = false;
		}
		return super.onOptionsItemSelected(item);
	}
}
