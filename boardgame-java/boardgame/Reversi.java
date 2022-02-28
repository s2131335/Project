/**
 * CSCI1130 Java Assignment 6 BoardGame Reversi
 * Aim: Practise subclassing, method overriding
 *      Learn from other subclass examples
 * 
 * I declare that the assignment here submitted is original
 * except for source material explicitly acknowledged,
 * and that the same or closely related material has not been
 * previously submitted for another course.
 * I also acknowledge that I am aware of University policy and
 * regulations on honesty in academic work, and of the disciplinary
 * guidelines and procedures applicable to breaches of such
 * policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 *   http://www.cuhk.edu.hk/policy/academichonesty
 * Faculty of Engineering Guidelines to Academic Honesty:
 *   https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name: KONG Kam Lun
 * Student ID  : 1155146691
 * Date        : 11 Dec 2021
 */

package boardgame;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Reversi is a TurnBasedGame
 */
public class Reversi extends TurnBasedGame {
    
    public static final String BLANK = " ";
    String winner;
    protected int passCount = 0;
    
    public Reversi ()
    {
        super(8, 8, "BLACK", "WHITE");
        this.setTitle ("Reversi");
        
    }
    
    @Override
    protected void initGame()
    {
        for (int x = 0; x < xCount; x++ )
        {
            for (int y = 0; y < yCount; y++)
            {
                pieces[x][y].setText(BLANK);
                pieces[x][y].setBackground(new Color(204,204,204));
                pieces[x][y].setOpaque(true);

                
            }
        }
            
            pieces[3][3].setBackground(Color.WHITE);
            pieces[4][4].setBackground(Color.WHITE);
            pieces[3][4].setBackground(Color.BLACK);
            pieces[4][3].setBackground(Color.BLACK);
            pieces[3][3].setText("WHITE");
            pieces[4][4].setText("WHITE");
            pieces[3][4].setText("BLACK");
            pieces[4][3].setText("BLACK");
            pieces[3][3].setEnabled(false);
            pieces[4][4].setEnabled(false);
            pieces[3][4].setEnabled(false);
            pieces[4][3].setEnabled(false);
    }
    
    @Override
    protected void gameAction(JButton triggeredButton, int x, int y)
    {
        if (!mustPass())
        {
           if (!isValid(x, y, true))
            {
                addLineToOutput("Invalid move");
                return;
            }
           
            pieces[x][y].setText(currentPlayer);
            pieces[x][y].setBackground(currentPlayer.equals("WHITE")? Color.WHITE: Color.BLACK );
            pieces[x][y].setEnabled(false);
            addLineToOutput(currentPlayer + " piece at (" + x + " , "+ y + ")");
            passCount = 0;
        }
        
        checkEndGame();
        
        if (gameEnded) 
        {
            addLineToOutput("Game ended!");
            JOptionPane.showMessageDialog(null, "Game ended!");
            return;
        }
        
        changeTurn();
    }
    
    protected boolean checkEndGame()
    {
        if (passCount == 2 || boardIsFull())
        {
            winner = checkWinner();
            if (winner.equals("DRAW"))
            {
                addLineToOutput(winner + "!");
            }
            else
            addLineToOutput("Winner is " + winner + "!");
            return (gameEnded = true);
        }
        return (gameEnded = false);
    }
    
    protected boolean isBlank(int x, int y)
    {
        return pieces[x][y].getText().equals(BLANK);
    }
    
    protected boolean isFriend(int x, int y)
    {
        return pieces[x][y].getText().equals(currentPlayer);
    }
    
    protected boolean isOpponent(int x, int y)
    {
        return pieces[x][y].getText().equals(getOpponent());
    }
    
    protected boolean boardIsFull()
    {
        for (int x = 0; x < 8 ; x++)
            for (int y = 0; y < 8; y++)
            {
                if (isBlank(x,y)) return false;
            }
        return true;
    }
    
    protected boolean mustPass()
    {
        for (int x = 0; x <= 7; x++)
        {
            for (int y = 0; y <= 7; y++)
            {
                //try {
                if (!isBlank(x , y)) continue;
                if (isValid(x, y, false)) return false;
               // }
                //catch (ArrayIndexOutOfBoundsException e)
                {
                    //continue;
                }
            }
        }
        passCount++;
        addLineToOutput("Pass!");
        return true;
    }
    
    protected boolean isValid(int x, int y , boolean flipEnabled)
    {
        int valid = 0;
        for (int deltaX = -1; deltaX <= 1; deltaX++)
        {
            for (int deltaY = -1; deltaY <= 1; deltaY++)
            {
               try {
               if (!isOpponent(x+deltaX,y+deltaY)) continue;
               if (deltaX == 0 && deltaY == 0) continue;
               }
               catch (ArrayIndexOutOfBoundsException e)
               {
                   continue;
               }
               int count = 2;
               
               try {
               while (isOpponent(x+count*deltaX,y+count*deltaY))
               {
                   count++;
               }
               }
               catch (ArrayIndexOutOfBoundsException e) 
               {
                   continue;
               }
               if (isFriend(x+count*deltaX,y+count*deltaY)) 
               {
                  if (flipEnabled == false) return true;
                   
                  valid = flipCapturedPieces(x , y , count-1 , deltaX, deltaY);
                  
               }
               //System.out.println("count = " + count);
            }
        
        }
        return ((valid == 1));
    }
      
    protected int flipCapturedPieces(int x, int y , int count, int deltaX, int deltaY)
    {
        for (int i = count; i > 0; i--)
        {
//            System.out.println("Count = " + i);
//            System.out.println((x+i*deltaX) +" "+ (y+i*deltaY));
            pieces[x+i*deltaX][y+i*deltaY].setText(currentPlayer);
            pieces[x+i*deltaX][y+i*deltaY].setBackground(currentPlayer.equals("WHITE")? Color.WHITE: Color.BLACK );
            pieces[x+i*deltaX][y+i*deltaY].setForeground(currentPlayer.equals("WHITE")? Color.BLACK: Color.WHITE );
        }
        return 1;
    }
    
    protected String checkWinner()
    {
        int blackCount = 0;
        int whiteCount = 0;
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (isBlank(x , y)) continue;
                if (pieces[x][y].getText().equals("BLACK")) blackCount++;
                        else whiteCount++;
            }
        }
        addLineToOutput("BLACK score : " + blackCount);
        addLineToOutput("WHITE score : " + whiteCount);
        if (whiteCount == blackCount) return "DRAW";
        if (whiteCount > blackCount) return "WHITE";
        else return "BLACK";
    }

    
    public static void main(String[] args)
    {
        Reversi reversi;
        reversi = new Reversi();
        
        System.out.println("You are running class Reversi");

        
        
        reversi.setLocation(400, 20);
        reversi.verbose = false;

    }
}
