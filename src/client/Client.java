package client;

import question.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends JFrame implements Runnable {

    private Socket socket;
    private ObjectInputStream in;
    private PrintWriter pw;
    private final String[] colors = {"Candy", "Egg", "Famous", "Random"};
    private JComboBox categoryChooser;
    private JPanel p = new JPanel();
    private JTextArea label = new JTextArea();

    private JButton categorybutton = new JButton("Start Game");
    private JButton continueButton = new JButton("Continue");
    private JButton[] buttons = new JButton[4];
    private String[] strings = {"Allan", "Fazli Zekiqi", "Victor J", "Victor O"};
    //private JLabel label = new JLabel("Welcome to the Quiz Fight", SwingConstants.CENTER);
    private JLabel playerOne = new JLabel("s1");
    private JLabel playerTwo = new JLabel("s2");
    private JPanel gridPanel = new JPanel(new GridLayout(2, 2));
    private JPanel panelLeft = new JPanel();
    private JPanel panelRight = new JPanel();
    private JPanel centerPanel = new JPanel(new BorderLayout());
    private Thread thread = new Thread(this);
    int counter;

    private String rightAnswer;

    public Client() throws IOException {
        socket = new Socket("localhost", 56565); // 172.20.201.169
        in = new ObjectInputStream(socket.getInputStream());
        pw = new PrintWriter(socket.getOutputStream(), true);

        gridPanel.setPreferredSize(new Dimension(500, 200));
        gridPanel.setBorder(new EmptyBorder(0, 30, 0, 0));
        setLayout(new BorderLayout());
        categoryChooser = new JComboBox(colors);
        categoryChooser.setSelectedIndex(0);

        p.add(categoryChooser);
        p.add(categorybutton);
        centerPanel.add(label, BorderLayout.CENTER);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(strings[i]);
            buttons[i].addActionListener(alternativesListener);
            buttons[i].setEnabled(false);
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setForeground(Color.WHITE);
            gridPanel.add(buttons[i]);
        }

        categorybutton.addActionListener(e -> {
            pw.println(categoryChooser.getSelectedItem());
            System.out.println("VALD KATEGORI" + categoryChooser.getSelectedItem());
            categoryChooser.setEnabled(false);
            categorybutton.setEnabled(false);
        });

        add(continueButton, BorderLayout.SOUTH);
        continueButton.setVisible(false);
        panelLeft.add(playerOne);
        panelRight.add(playerTwo);
        panelLeft.setBackground(Color.ORANGE);
        panelRight.setBackground(Color.ORANGE);
        add(panelLeft, BorderLayout.WEST);
        add(panelRight, BorderLayout.EAST);

        playerTwo.setBackground(Color.ORANGE);
        centerPanel.add(gridPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        add(p, BorderLayout.NORTH);

        label.setEnabled(false);
        label.setLineWrap(true);
        label.setDisabledTextColor(Color.BLACK);
        label.setBackground(Color.ORANGE);
        label.setForeground(Color.BLACK);

        label.setFont(label.getFont().deriveFont(15.0f));


        p.setBackground(Color.ORANGE);
        gridPanel.setBackground(Color.ORANGE);
        centerPanel.setBackground(Color.ORANGE);
        setBackground(Color.ORANGE);
        setSize(700, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        thread.start();
    }//CONSTRUCTOR

    @Override
    public void run() {
        Object obj;
        continueButton.addActionListener(continueButtonListener);

        try {
            while ((obj = in.readObject()) != null) {
                if (obj instanceof Question) {
                    Question question = (Question) obj;
                    showTheQuestion(question);
                } else if (obj instanceof String) {
                    String message = (String) obj;
                    showTheMessageFromServer(message);

                } else if (obj instanceof Integer[]) {
                    Integer[] points = (Integer[]) obj;
                    showThePoints(points);
                } else if (obj instanceof ArrayList) {
                    // Kontrollera typer!
                    ArrayList<java.util.List> lista = (ArrayList) obj;
                    List<Integer> playerOneHistory = lista.get(0);
                    List<Integer> playerTwoHistory = lista.get(1);
                    String playerOneText = getScoreSummary("Spelare 1", playerOneHistory);
                    String playerTwoText = getScoreSummary("Spelare 2", playerTwoHistory);
                    JOptionPane.showMessageDialog(this, playerOneText + "\n\n" + playerTwoText);
                }
            }//while
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }//run()

    private void showTheQuestion(Question question) {
        System.out.println(question.getQuestion());
        label.setText("\n\n\n\n\n\n             " + question.getQuestion());
        ArrayList<String> alt = question.getAlternatives();
        rightAnswer = question.getRightAnswer();
        for (int i = 0; i < alt.size(); i++) {
            buttons[i].setEnabled(true);
            buttons[i].setText(alt.get(i));
        }
    }

    private void showThePoints(Integer[] points) {
        if (playerOne.equals("Player 1")) {
            playerOne.setText("P1 : " + points[0]);
            playerTwo.setText("P2 : " + points[1]);
        } else {
            playerTwo.setText("P2 : " + points[1]);
            playerOne.setText("P1 : " + points[0]);
        }
    }

    private void showTheMessageFromServer(String message) {
        if (message.startsWith("Welcome")) {
            message = message.substring(message.indexOf(' '));
            if (message.contains("1")) {
                playerOne.setText(message);
                setTitle(message);
                playerTwo.setText("Player 2");
            } else {
                playerTwo.setText(message);
                setTitle(message);
                playerOne.setText("Player 1");
            }
        } else if (message.startsWith("Wait")) {
            for (JButton button : buttons) {
                button.setEnabled(false);
            }
            categoryChooser.setEnabled(false);
            categorybutton.setEnabled(false);
            label.setText("\n\n\n\n\n\n\n                    " + message);
        } else if (message.startsWith("YOU WIN")) {
            JOptionPane.showMessageDialog(this, "YOU WIN", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        } else if (message.startsWith("YOU LOSE")) {
            JOptionPane.showMessageDialog(this, "YOU LOSE", "You're defeated", JOptionPane.ERROR_MESSAGE);
        } else if (message.startsWith("YOU TIED")) {
            JOptionPane.showMessageDialog(this, "YOU TIED", " ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            categorybutton.setEnabled(true);
            categoryChooser.setEnabled(true);
            label.setText("\n\n\n\n\n\n                     " + message);
        }
    }//showTheMessageFromTheServer

    private String theAnswerFromUser;
    private ActionListener continueButtonListener = e -> {
        continueButton.setVisible(false);
        for (JButton button : buttons) {
            button.setBackground(Color.BLACK);
        }
        pw.println(theAnswerFromUser);
    };//cnt

    private ActionListener alternativesListener = e -> {
        JButton temp = (JButton) e.getSource();
        categoryChooser.setEnabled(false);
        categorybutton.setEnabled(false);
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
        changeButtonsColor(temp);
        continueButton.setVisible(true);
        theAnswerFromUser = temp.getText();
    };//clientListener

    private void changeButtonsColor(JButton temp) {
        if (temp.getText().equalsIgnoreCase(rightAnswer)) {
            temp.setBackground(Color.GREEN);
            temp.setOpaque(true);
        } else {
            temp.setBackground(Color.RED);
            temp.setOpaque(true);

            for (JButton button : buttons) {
                if (button.getText().equalsIgnoreCase(rightAnswer)) {
                    button.setBackground(Color.green);
                    button.setOpaque(true);
                }
            }
        }
    }//changeColor

    String getScoreSummary(String playerName, List<Integer> scores) {
        StringBuilder s = new StringBuilder(playerName + ":");
        int sum = 0;
        for (int i = 0; i < scores.size(); i++) {
            sum += scores.get(i);
            s.append(String.format("\nRond %d: %d", i + 1, scores.get(i)));
        }
        s.append("\n\nSumma: " + sum);
        return s.toString();
    }

    public static void main(String[] args) {
        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
