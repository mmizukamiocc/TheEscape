package edu.orangecoastcollege.escapethecatcher;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class GameActivity extends Activity  implements GestureDetector.OnGestureListener{

    private GestureDetector aGesture;

    //FLING THRESHOLD VELOCITY
    final int FLING_THRESHOLD = 500;

    //BOARD INFORMATION
    final int SQUARE = 150;
    final int OFFSET = 5;
    final int COLUMNS = 7;
    final int ROWS = 8;

    final int gameBoard1[][] = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 2, 1, 2, 2, 1},
            {1, 2, 1, 1, 1, 2, 1},
            {1, 2, 2, 1, 2, 2, 3},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1}
    };

    final int gameBoard2[][] = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 2, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 2, 3},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1}
    };

    final int gameBoard3[][] = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 1, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 2, 1},
            {1, 2, 2, 2, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 3},
            {1, 2, 1, 2, 1, 2, 1},
            {1, 1, 1, 1, 1, 1, 1}
    };

    private Player player;
    private Zombie zombie;

    //LAYOUT AND INTERACTIVE INFORMATION
    private ArrayList<ImageView> visualObjects;
    private RelativeLayout activityGameRelativeLayout;
    private ImageView zombieImageView;
    private ImageView playerImageView;
    private ImageView obstacleImageView;
    private ImageView exitImageView;
    private int exitRow;
    private int exitCol;

    private int stage = 1;
    //  WINS AND LOSSES
    private int wins;
    private int losses;
    private TextView winsTextView;
    private TextView lossesTextView;

    private LayoutInflater layoutInflater;
    private Resources resources;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return aGesture.onTouchEvent(event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        activityGameRelativeLayout = (RelativeLayout) findViewById(R.id.activity_game);
        winsTextView = (TextView) findViewById(R.id.winsTextView);
        lossesTextView = (TextView) findViewById(R.id.lossesTextView);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resources = getResources();

        visualObjects = new ArrayList<ImageView>();

        wins = 0;
        losses = 0;
        winsTextView.setText(resources.getString(R.string.win) + wins);
        lossesTextView.setText(resources.getString(R.string.losses) + losses);

        aGesture = new GestureDetector(this,this);

        startNewGame();
    }

    private void startNewGame() {
        //TASK 1:  CLEAR THE BOARD (ALL IMAGE VIEWS)
        for (int i = 0; i < visualObjects.size(); i++) {
            ImageView visualObj = visualObjects.get(i);
            activityGameRelativeLayout.removeView(visualObj);
        }
        visualObjects.clear();

        //TASK 2:  BUILD THE  BOARD
        buildGameBoard();

        //TASK 3:  ADD THE CHARACTERS
        createZombie();
        createPlayer();


    }

    private void buildGameBoard() {
        // TODO: Inflate the entire game board (obstacles and exit)
     int gameBoard[][] = gameBoard1;
        if(stage%3 == 1)
            gameBoard = gameBoard1;
        else if(stage%3 == 2)
            gameBoard = gameBoard2;
        else if(stage%3 == 0)
            gameBoard = gameBoard3;

        for(int c = 0;c<COLUMNS;c++) {
            for (int r = 0; r < ROWS; r++) {
                if (gameBoard[r][c] == 1) {
                    obstacleImageView = (ImageView) layoutInflater.inflate(R.layout.obstacle_layout, null);
                    obstacleImageView.setX(c * SQUARE + OFFSET);
                    obstacleImageView.setY(r * SQUARE + OFFSET);
                    activityGameRelativeLayout.addView(obstacleImageView);
                    visualObjects.add(obstacleImageView);

                } else if (gameBoard[r][c] == 3) {
                    exitImageView = (ImageView) layoutInflater.inflate(R.layout.exit_layout, null);
                    exitImageView.setX(c * SQUARE + OFFSET);
                    exitImageView.setY(r * SQUARE + OFFSET);
                    activityGameRelativeLayout.addView(exitImageView);
                    visualObjects.add(exitImageView);
                    exitRow = r;
                    exitCol = c;
                }
            }
        }
    }

    private void createZombie() {
        // TODO: Determine where to place the Zombie (at game start)
        int row = 5;
        int col = 5;
        zombie = new Zombie();
        zombie.setRow(row);
        zombie.setCol(col);

        // TODO: Then, inflate the zombie layout
        zombieImageView = (ImageView) layoutInflater.inflate(R.layout.zombie_layout,null);
        zombieImageView.setX(col * SQUARE + OFFSET);
        zombieImageView.setY(row * SQUARE + OFFSET);

        activityGameRelativeLayout.addView(zombieImageView);

        visualObjects.add(zombieImageView);
    }

    private void createPlayer() {
        // TODO: Determine where to place the Player (at game start)
        int row = 2;
        int col = 2;

       if(stage%3 == 0) {
            row = 5;
            col = 4;
       }
        player = new Player();
        player.setRow(row);
        player.setCol(col);

        // TODO: Then, inflate the player layout
        playerImageView =(ImageView) layoutInflater.inflate(R.layout.player_layout,null);
        playerImageView.setX(col * SQUARE + OFFSET);
        playerImageView.setY(row * SQUARE + OFFSET);

        activityGameRelativeLayout.addView(playerImageView);

        visualObjects.add(playerImageView);

    }



    private void movePlayer(float velocityX, float velocityY) {
        int gameBoard[][] = gameBoard1;
        if(stage%3 == 1)
            gameBoard = gameBoard1;
        else if(stage%3 == 2)
            gameBoard = gameBoard2;
        else if(stage%3 == 0)
            gameBoard = gameBoard3;

        String direction = "";

        if(Math.abs(velocityX) > Math.abs(velocityY))
        {
            if(velocityX < -FLING_THRESHOLD)
            direction = "LEFT";
            else if (velocityX > FLING_THRESHOLD)
            direction = "RIGHT";

        }
        else
        {
            if(velocityY > FLING_THRESHOLD)
                direction = "DOWN";
            else if (velocityY < -FLING_THRESHOLD)
                direction = "UP";

        }

        if(!direction.equals(""))
        {
            player.move(gameBoard, direction);
            playerImageView.setX(player.getCol()* SQUARE + OFFSET);
            playerImageView.setY(player.getRow()* SQUARE + OFFSET);
        }


        zombie.move(gameBoard,player.getCol(),player.getRow());
        zombieImageView.setX(zombie.getCol() *SQUARE +OFFSET);
        zombieImageView.setY(zombie.getRow() *SQUARE +OFFSET);

        //Determine if the game is won or lost

        if(player.getRow() == exitRow && player.getCol() == exitCol)
        {
            winsTextView.setText(resources.getString(R.string.win) + (++wins));
            stage++;
            startNewGame();
        }

        if(player.getRow() == zombie.getRow() && player.getCol() == zombie.getCol())
        {
            lossesTextView.setText(resources.getString(R.string.losses) + (++losses));
            stage = 1;
            startNewGame();

        }


        // TODO: This method gets called in the onFling event

        // TODO: Determine which absolute velocity is greater (x or y)
        // TODO: If x is negative, move player left.  Else if x is positive, move player right.
        // TODO: If y is negative, move player down.  Else if y is positive, move player up.

        // TODO: Then move the zombie, using the player's row and column position.
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

       movePlayer(v,v1);
        return true;
    }
}
