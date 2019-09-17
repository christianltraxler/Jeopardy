package jeopardy;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Jeopardy {

    //Initialize static variables
    public static JButton[][] buttons = new JButton[6][5];
    public static String Question[][] = new String[5][5];
    public static String Answer[][] = new String[5][5];
    public static JFrame frame = new JFrame("Jeopardy");
    public static JPanel logo = new JPanel();
    public static JPanel board = new JPanel();
    public static JLabel tracker[];
    public static JPanel game;
    public static String[] Categories = new String[5];
    public static int score[];
    public static int turns = 0;
    public static int teams;
    public static String filepath = "src/categories.txt";

    public static void main(String[] args) throws FileNotFoundException {
        //Let user choose the number of teams
        String[] options = {"1", "2", "3"};
        teams = JOptionPane.showOptionDialog(null, "Please choose the number of teams to play", "Jeopardy",
                JOptionPane.WARNING_MESSAGE, 0, null, options, options[2]);
        //Add one to teams to not get 0
        teams++;
        //Initialize score for each team to be zero
        score = new int[teams];
        for (int i = 0; i < teams; i++) {
            score[i] = 0;
        }
        //Run methods to create the questions, answers, and images
        CreateQuestionsAnswers();
        CreateImages();
    }

    public static void CreateImages() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Make JFrame close when exited
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Make JFrame the maximum size
        frame.setLayout(null);

        game = new JPanel(); //Create JPanel for the Jeorpardy board
        game.setSize(1600, 1020); //Set the size of the panel
        game.setLayout(new GridLayout(6, 5)); //Create a GridLayout for the game

        //Create category buttons
        for (int i = 0; i < 5; i++) {
            buttons[0][i] = new JButton("");
            //Set the colour and font of the buttons
            buttons[0][i].setBackground(Color.BLUE);
            buttons[0][i].setForeground(Color.WHITE);
            buttons[0][i].setFont(new Font("Helvetica", Font.BOLD, 30));
            game.add(buttons[0][i]); //Add the buttons to the JPanel
        }

        //Create clickable buttons
        for (int i = 1; i < 6; i++) {
            for (int n = 0; n < 5; n++) {
                buttons[i][n] = new JButton("");
                //Set the colour and font of the buttons
                buttons[i][n].setBackground(Color.BLUE);
                buttons[i][n].setForeground(Color.WHITE);
                buttons[i][n].setFont(new Font("Helvetica", Font.PLAIN, 80));
                buttons[i][n].addActionListener(new ActionA()); //Add ActionListener to the buttons
                game.add(buttons[i][n]); //Add the buttons to the JPanel
            }
        }

        //Set the text of all the buttons
        for (int i = 0; i < 5; i++) {
            buttons[0][i].setText(Categories[i]);
            buttons[1][i].setText("200");
            buttons[2][i].setText("400");
            buttons[3][i].setText("600");
            buttons[4][i].setText("800");
            buttons[5][i].setText("1000");
        }

        board.setBounds(1650, 400, 275, 380); //Set the size and position of the scoreboard
        board.setLayout(new GridLayout(teams, 1)); //Create GridLayout for the score depending on the number of teams

        //Create JLabel for the teams score
        tracker = new JLabel[teams];
        for (int i = 0; i < teams; i++) {
            tracker[i] = new JLabel();
            int team = i + 1; //Create new variable to not have a team 0
            //Set text, colour, font, size and position of the scores
            tracker[i].setText("Team " + team + ": 0");
            tracker[i].setBackground(Color.BLACK);
            tracker[i].setForeground(Color.BLUE);
            tracker[i].setFont(new Font("Helvetica", Font.BOLD, 30));
            tracker[i].setBounds(0, 0, 400, 200);
            board.add(tracker[i]); //Add the scores to the JPanel
        }

        //Create JLabel for Jeopardy logo
        JLabel jeopardy = new JLabel();
        jeopardy.setIcon(new ImageIcon("src/images/j!.png")); //Retrieve the image from the image file
        jeopardy.setBounds(0, 0, jeopardy.getPreferredSize().width, jeopardy.getPreferredSize().height); //Set the size and position of the logo

        logo = new JPanel();
        logo.setBounds(1550, 0, 375, 400); //Set size and position of the logo
        logo.setLayout(null);

        //Add everything to the frame
        logo.add(jeopardy);
        frame.add(game);
        frame.add(board);
        frame.add(logo);
        frame.setVisible(true);
    }

    static class ActionA implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            //Check which button was pressed
            for (int i = 1; i < 6; i++) {
                for (int a = 0; a < 5; a++) {
                    if (actionEvent.getSource() == buttons[i][a]) {
                        AskQuestions(i, a); //Run AskQuestions method
                    }
                }
            }
        }
    }

    public static void CreateQuestionsAnswers() throws FileNotFoundException {
        //Retrieve the questions for the file
        File file = new File(filepath);
        Scanner in = new Scanner(file);
        String info = "";
        while (in.hasNextLine()) {
            info += in.nextLine();
        }
        String text[] = info.split("  "); //Split the text into different lines

        for (int i = 0; i < Categories.length; i++) {
            Categories[i] = ""; //Initialize the categories array
        }

        boolean check; //Initialize the check variable
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * (text.length / 11)); //Get a random number
            check = false; //Set the check variable to be false
            for (int a = 0; a < 5; a++) {
                if ((text[11 * random]).equals(Categories[a])) { //Check if the random category chosen has already been used
                    check = true;
                }
            }
            if (check == true) { //If the check is true, repeat
                i--;
            } else if (check == false) {
                Categories[i] = text[11 * random]; //If the category has not already been chosen, set the category equals to the random category
            }
        }

        //Set all the questions and answers to variables depending on the category
        for (int i = 0; i < 5; i++) {
            for (int a = 0; a < (text.length / 11); a++) {
                if (Categories[i].equals(text[11 * a])) {
                    for (int n = 0; n < 5; n++) {
                        Question[n][i] = text[(11 * a) + (2 * n) + 1];
                        Answer[n][i] = text[(11 * a) + (2 * n) + 2];
                    }
                }
            }
        }
    }

    public static void AskQuestions(int i, int a) {
        //Initialize variables
        int turn = 0;
        String guess = null;

        //Do if the button has not been clicked before
        if (Question[i - 1][a].equals("")) {
        } else {
            //Get the users guess
            guess = JOptionPane.showInputDialog(Question[i - 1][a]);

            //Check which teams turn it is
            if (turns % teams == 0) {
                turn = 0;
            } else if ((turns % teams == 1) && (teams > 1)) {
                turn = 1;
            } else if ((turns % teams == 2) && (teams > 2)) {
                turn = 2;
            }

            //Execute if the question has not been exited
            if (guess != null) {
                guess = guess.toUpperCase(); //Convert the guess to upper case

                //Check if the user is correct
                if (guess.equals(Answer[i - 1][a])) {
                    JOptionPane.showMessageDialog(null, "You are Correct!");
                    score[turn] += (i * 200); //Add to score depending on the question
                } else if (!guess.equals(Answer[i - 1][a])) {
                    JOptionPane.showMessageDialog(null, "You are Wrong!");
                    score[turn] -= (i * 200); //Subtract to score depending on the questeion
                }
            } else {
                //If the question was exited
                JOptionPane.showMessageDialog(null, "You did not answer!");
                score[turn] -= (i * 200); //Subtract to score depending on the questeion
            }
            //Set the score equal to zero if the score is negative
            if (score[turn] < 0) {
                score[turn] = score[turn] - score[turn];
            }
            //Update the game
            int team = turn + 1;
            tracker[turn].setText("Team " + team + ": " + score[turn]);
            JOptionPane.showMessageDialog(null, "Your score is: " + score[turn]);
            buttons[i][a].setText("");
            buttons[i][a].setBackground(Color.decode("#87CEFA"));
            Question[i - 1][a] = "";
            Answer[i - 1][a] = "";
            turns++; //Add to turns

            //If there are no more turns execute Finish method
            if (turns == 25) {
                Finish();
            }
        }
    }

    public static void Finish() {
        int won = 0;

        //Check the team and find which team won
        for (int i = 0; i < score.length; i++) {
            if (teams == 2) {
                if (score[i] >= score[0] && score[i] >= score[1]) {
                    won = i + 1;
            }
            if (teams == 3) {
                if (score[i] >= score[0] && score[i] >= score[1] && score[i] >= score[2]) {
                    won = i + 1;
                }
                }
            }
        }
        //Output the results
        if (teams == 1) {
            JOptionPane.showMessageDialog(null, "You had " + score[0] + " points");
        } else {
            JOptionPane.showMessageDialog(null, "Team " + won + " won!\nThey had " + score[won - 1] + " points");
        }
        System.exit(0); //Exit the game
    }
}
