import java.awt.*; //for main gui components like frame and buttons
import javax.swing.*;// for ActionListener, ActionEvent

public class Tics{ 
    protected JFrame frame;
    protected JButton[][] button=new JButton[3][3];
    protected JButton button1,button2;
    protected boolean player1turn=true;//true for player 1,false for player 2/AI
    protected boolean vscomputer=false;
    protected int player1_Score=0;
    protected int player2_Score=0;

    protected JLabel statusLabel;
    protected JLabel scoreLabel;
    protected JLabel modeLabel;

    protected JPanel modeTitlePanel;
    protected JPanel modeButtonPanel;
    protected JPanel wrap;// container for mode + top status

    //logical board for ai to test
    private final char[][] board=new char[3][3];

    public Tics(){
        this.frame = new JFrame("Tic Tac Toe");//referring to the current frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,800);
        frame.setLayout(new BorderLayout());

        //make logical board empty
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = ' ';
            }
        }
        

        //Mode Title
        modeLabel = new JLabel("Choose Game Mode",SwingConstants.CENTER);
        modeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        modeLabel.setForeground(Color.BLACK);

        modeTitlePanel = new JPanel(new BorderLayout());
        modeTitlePanel.setBackground(Color.WHITE);
        modeTitlePanel.add(modeLabel,BorderLayout.CENTER);

        //Mode Button
        modeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));//horizontal and vertical px gap
        modeButtonPanel.setBackground(Color.WHITE);


        //Game Grid
        JPanel gridP = new JPanel(new GridLayout(3,3));
        initializeButtons(gridP);//called to set visible later on
        

        //Status and Score Labels
        statusLabel = new JLabel("Player 1's turn (X)", SwingConstants.CENTER);//center pos
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setForeground(Color.BLUE);

        scoreLabel = new JLabel(getScoreText(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.MAGENTA);


        JPanel topP = new JPanel(new GridLayout(2,1));
        topP.add(statusLabel);
        topP.add(scoreLabel);


        // WRAPPER (mode + status)
        wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.add(modeTitlePanel);
        wrap.add(modeButtonPanel);
        wrap.add(topP);


        frame.add(wrap, BorderLayout.NORTH);
        frame.add(gridP, BorderLayout.CENTER);

        enableGrid(false);
        chooseMode();

        frame.setMinimumSize(new Dimension(420, 560));
        frame.setVisible(true);
    }
  
    private void initializeButtons(JPanel panel){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                button[i][j]=new JButton();
                button[i][j].setFont(new Font("Arial",Font.PLAIN,60));
                panel.add(button[i][j]);//button to panel, since panel in frame

                final int row=i, col=j;//values must not change after ActionListener
                button[i][j].addActionListener(e -> handleMove(row, col));//add click event
                    //ActionEvent: obj that carries info about a button clicked
                    //response when button clicked
                    // e is needed in syntax, however not using,since row and column known             
            }
        }
    }

    private void chooseMode(){
        button1=new JButton("Play v/s Player");
        button2=new JButton("Play v/s Computer");
        button1.setFont(new Font("Arial",Font.PLAIN,18));
        button2.setFont(new Font("Arial",Font.PLAIN,18));

        modeButtonPanel.removeAll();
        modeButtonPanel.add(button1);
        modeButtonPanel.add(button2);

        // Play vs Player        
        button1.addActionListener(e -> {
                vscomputer=false;

                modeTitlePanel.setVisible(false);
                modeButtonPanel.setVisible(false);

                //refresh layout so the rest of UI moves up
                wrap.revalidate();
                wrap.repaint();

                //enable grid and start fresh
                enableGrid(true);
                resetBoard();
                updateStatus();
        });
        // Play vs Computer
        button2.addActionListener(e -> {
                vscomputer=true;

                modeTitlePanel.setVisible(false);
                modeButtonPanel.setVisible(false);

                wrap.revalidate();
                wrap.repaint();

                enableGrid(true);
                resetBoard();
                updateStatus();
        });
        modeButtonPanel.revalidate();
        modeButtonPanel.repaint();             
    }
    
    //to enable/disable all grid buttons
    private void enableGrid(boolean enabled){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                button[i][j].setEnabled(enabled);
            }
        }
    }

    private String getScoreText(){
         return "Player 1 (X): " + player1_Score + "   |   Player 2 (O): " + player2_Score;
    }

    //Logical Board

    //update logical board from actual board
    private void updateBoardFromUI(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                String text = button[i][j].getText();
                board[i][j] = text.equals("") ? ' ' : text.charAt(0);//either empty or the value X/O
            }
        }
    }

    //Evaluate board for minimax: +10 if AI (O) wins, -10 if human (X) wins, 0 otherwise
    private int evaluateBoard(char[][] board){
        //Computer -> O and Human -> X

        //Check rows
        for(int i = 0; i < 3; i++){
            if(board[i][0]!=' ' &&
                board[i][0]==board[i][1] &&
                board[i][1]==board[i][2]){
                
                return (board[i][0]=='O') ? 10 : -10;
            }
        }

        // Check columns
        for(int j = 0; j < 3; j++){
            if(board[0][j]!=' ' &&
                board[0][j]==board[1][j] &&
                board[1][j]==board[2][j]){

                return (board[0][j]=='O') ? 10 : -10;
            }
        }

        // Check main diagonal
        if(board[0][0]!=' ' &&
            board[0][0]==board[1][1] &&
            board[1][1]==board[2][2]){

            return (board[0][0]=='O') ? 10 : -10;
        }

        // Check anti-diagonal
        if (board[0][2]!=' ' &&
            board[0][2]==board[1][1] &&
            board[1][1]==board[2][0]){

            return (board[0][2]=='O') ? 10 : -10;
        }

    return 0;// Draw
    }

    //to check if ai needs to perform recursion
    private boolean isMovesLeft(char[][] board){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == ' '){
                    return true;
                }
            }
        }
        return false;
    }

    private int[][] getWinningCellsOnBoard(char[][] board){//for both AI and Human

        //rows
        for(int i=0;i<3;i++){
            if(board[i][0] != ' ' && board[i][0]==board[i][1] && board[i][1]==board[i][2]){
                return new int[][]{{i,0},{i,1},{i,2}};
            }
        }

        //columns
        for(int j=0;j<3;j++){
            if(board[0][j] != ' ' && board[0][j]==board[1][j] && board[1][j]==board[2][j]){
                return new int[][]{{0,j},{1,j},{2,j}};
            }
        }

        //diagonal
        if(board[0][0] != ' ' && board[0][0]==board[1][1] && board[1][1]==board[2][2]){
            return new int[][]{{0,0},{1,1},{2,2}};
        }
        
        //anti-diagonal
        if(board[0][2] != ' ' && board[0][2]==board[1][1] && board[1][1]==board[2][0]){
            return new int[][]{{0,2},{1,1},{2,0}};
        }
        return null;
    }

    //main algorithm Minimax for computer moves
    private int miniMax(char[][] board, int depth, boolean isMaximizing){
        int score = evaluateBoard(board);

        if(score == 10) return score - depth;  // AI win -> reward
        if(score == -10) return score + depth; // Human win -> penalty
        if(!isMovesLeft(board)) return 0;      // Draw

        if(isMaximizing){//testing best position
            int best = Integer.MIN_VALUE;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(board[i][j] == ' '){
                        board[i][j] = 'O'; // AI move
                        best = Math.max(best, miniMax(board, depth + 1, false));
                        board[i][j] = ' '; // undo
                    }
                }
            }
            return best;
        }
        else{
            int best = Integer.MAX_VALUE;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(board[i][j] == ' '){
                        board[i][j] = 'X'; // Human move
                        best = Math.min(best, miniMax(board, depth + 1, true));
                        board[i][j] = ' '; // undo
                    }
                }
            }
            return best;
        }
    }

    private int[] getBestMove(char[][] board){
        int bestScore=Integer.MIN_VALUE;
        int bestRow=-1;
        int bestCol=-1;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j]==' '){
                    board[i][j]='O';//try move for AI
                    int score=miniMax(board, 0, false);//false means, now its human's turn
                    board[i][j]=' ';//undo
                    if(score>bestScore){
                        bestScore=score;
                        bestRow=i;
                        bestCol=j;
                    }
                }
            }
        }
        return new int[]{bestRow, bestCol};
    }

    //AI move code

    private void makeAIMove(){
        enableGrid(false);//temporarily disable input

        //small delay
        Timer timer = new Timer(300, e -> {
            //logical board match actual board
            updateBoardFromUI();

            int[] move=getBestMove(board);
            int row=move[0];
            int col=move[1];

            if(row==-1 || col==-1){//no moves (error catching)
                enableGrid(true);
                return;
            }

            //AI move
            button[row][col].setText("O");
            board[row][col]='O';

            //evaluateBoard(board) to check result
            int score = evaluateBoard(board);
            if(score == 10){ //AI (O) won
                int[][] win = getWinningCellsOnBoard(board);
                if(win != null){
                    for(int[] c : win){
                        button[c[0]][c[1]].setBackground(Color.green);
                        button[c[0]][c[1]].setOpaque(true);
                        button[c[0]][c[1]].setBorderPainted(false);
                    }
                }
            player2_Score++; // AI is player2
            scoreLabel.setText(getScoreText());
            Timer t2 = new Timer(500, e2 -> playAgain("Computer Wins!"));
            t2.setRepeats(false);
            t2.start();
        }
            else if(!isMovesLeft(board)) {
                playAgain("It's a Draw!");
            }
            else{
                player1turn = true; //back to human's turn
                updateStatus();
                enableGrid(true);
            }

        });
        timer.setRepeats(false);
        timer.start();
    }  

    //Actual Board Logic

    //method to check if current player won
    private int[][] getWinningCells() {//[row][col]
        String letter = player1turn ? "X" : "O";
        //check rows and columns
        for(int i=0;i<3;i++){
            //getText() returns string, & equals() to compare
            if(button[i][0].getText().equals(letter) && button[i][1].getText().equals(letter) && button[i][2].getText().equals(letter)){
                return new int[][]{{i,0},{i,1},{i,2}};//create new array everytime
            }
            else if(button[0][i].getText().equals(letter) && button[1][i].getText().equals(letter) && button[2][i].getText().equals(letter)){
                return new int[][]{{0,i},{1,i},{2,i}};
            }
        }
        //check diagonals
        if(button[0][0].getText().equals(letter) && button[1][1].getText().equals(letter) && button[2][2].getText().equals(letter)){
            return new int[][]{{0,0},{1,1},{2,2}};
        }
        else if(button[0][2].getText().equals(letter) && button[1][1].getText().equals(letter) && button[2][0].getText().equals(letter)){
            return new int[][]{{0,2},{1,1},{2,0}};
        }
        //if no conditions are met
        return null;
    }

    private void updateStatus(){
        if(player1turn){
            statusLabel.setText("Player 1's turn (X)");
            statusLabel.setForeground(Color.BLUE);
        } 
        else{
            statusLabel.setText("Player 2's turn (O)");
            statusLabel.setForeground(Color.RED);
        }
    }

    //method to handle player moves
    private void handleMove(int row, int col){//takes final values
        //if used button clicked again, display dialog msg
        if(!button[row][col].getText().equals("")){
            JOptionPane.showMessageDialog(frame,"Choose another cell");
            return;//Exit so no other changes occur
        }
        //set X or O for the current player's turn
        button[row][col].setText(player1turn ? "X" : "O");
        board[row][col]=player1turn ? 'X' : 'O';

        int[][] winningCells = getWinningCells();
        if(winningCells != null){
            // Highlight winning buttons
            for(int[] cell : winningCells){
                button[cell[0]][cell[1]].setBackground(Color.green);
                button[cell[0]][cell[1]].setOpaque(true);
                button[cell[0]][cell[1]].setBorderPainted(false);
            }
            if(player1turn){
                player1_Score++;
            } 
            else{
                player2_Score++;
            } 

            scoreLabel.setText(getScoreText());
            String winner = player1turn ? "Player 1 (X)" : "Player 2 (O)";
            Timer timer = new Timer(500, e -> playAgain(winner + " wins!"));
            timer.setRepeats(false);
            timer.start();
        } 
        //Draw
        else if(!isMovesLeft(board)){
            playAgain("It's a Draw!");
        }
        else{
            player1turn = !player1turn;  // Switch turn player1=player2
            updateStatus();

            //AI's turn
            if(vscomputer && !player1turn){
                makeAIMove();
            }
        }
    }

    private void playAgain(String message){
        int response = JOptionPane.showConfirmDialog(frame, message+"\nDo you want to play again?","Game Over!",JOptionPane.YES_NO_OPTION);
        if(response==JOptionPane.YES_OPTION){
            resetBoard();
            enableGrid(false);

            modeTitlePanel.setVisible(true);
            modeButtonPanel.setVisible(true);

            wrap.revalidate();  //recalculates layout
            wrap.repaint();     //redraws visuals
        }
        else{
            JOptionPane.showMessageDialog(frame, "Final Scores:\n" + getScoreText() + "\nThank you for playing!");
            frame.dispose();//release all resources, and free memory
            System.exit(0);//terminate JVM
        }
    }

    //method to reset the board for a new game
    private void resetBoard(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                button[i][j].setText("");//make cells empty
                button[i][j].setBackground(null);//reset to default colour
                button[i][j].setOpaque(false);
                button[i][j].setBorderPainted(true);
                board[i][j] = ' '; //keep logical board in sync
            }
        }
        player1turn = true;//reset player1turn to true
        updateStatus();
    } 

    public static void main(String[] args){
         SwingUtilities.invokeLater(() -> new Tics());
    }
}