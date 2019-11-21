package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import question.Question;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame implements Runnable {

    Socket socket;
    ObjectInputStream in;
    PrintWriter pw;
    private final String[] colors = {"Candy", "Egg", "Famous", "Random"};
    private JComboBox categoryChooser;
    private JPanel p = new JPanel();

    JButton categorybutton = new JButton("Start Game");
    JButton continueButton = new JButton("Continue");
    JButton[] buttons = new JButton[4];
    String[] strings = {"Allan", "Fazli Zekiqi", "Victor J", "Victor O"};
    JLabel label = new JLabel("Welcome to the Quiz Fight", SwingConstants.CENTER);
    JLabel spelare1 = new JLabel("s1");
    JLabel spelare2 = new JLabel("s2");
    JPanel gridPanel = new JPanel(new GridLayout(2, 2));
    JPanel centerPanel = new JPanel(new BorderLayout());
    Thread thread = new Thread(this);
    int counter;

    String rightAnswer;

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
            gridPanel.add(buttons[i]);


        }
        categorybutton.addActionListener(e -> {
            pw.println(categoryChooser.getSelectedItem());
            System.out.println("VALD KATEGORI" + categoryChooser.getSelectedItem());

        });

        add(continueButton, BorderLayout.SOUTH);
        continueButton.setVisible(false);
        add(spelare1, BorderLayout.WEST);
        add(spelare2, BorderLayout.EAST);

        centerPanel.add(gridPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        add(p, BorderLayout.NORTH);

        setSize(500, 500);
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
        label.setText(question.getQuestion());
        ArrayList<String> alt = question.getAlternatives();
        rightAnswer = question.getRightAnswer();
        for (int i = 0; i < alt.size(); i++) {
            buttons[i].setEnabled(true);
            buttons[i].setText(alt.get(i));
        }
    }

    private void showThePoints(Integer[] points) {
        if (spelare1.equals("Player 1")) {
            spelare1.setText("P1 : " + points[0]);
            spelare2.setText("P2 : " + points[1]);
        } else {
            spelare2.setText("P2 : " + points[1]);
            spelare1.setText("P1 : " + points[0]);
        }
    }

    private void showTheMessageFromServer(String message) {
        if (message.startsWith("Welcome")) {
            message = message.substring(message.indexOf(' '));
            if (message.contains("1")) {
                spelare1.setText(message);
                setTitle(message);
                spelare2.setText("Player 2");
            } else {
                spelare2.setText(message);
                setTitle(message);
                spelare1.setText("Player 1");
            }

        } else if (message.startsWith("Wait")) {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(false);
            }
            categoryChooser.setEnabled(false);
            categorybutton.setEnabled(false);
            label.setText(message);
        } else if (message.startsWith("YOU WIN")) {
            JOptionPane.showMessageDialog(null, "YOU WIN", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        } else if (message.startsWith("YOU LOSE")) {
            JOptionPane.showMessageDialog(null, "YOU LOSE", "You're defeated", JOptionPane.ERROR_MESSAGE);
        } else if (message.startsWith("YOU TIED")) {
            JOptionPane.showMessageDialog(null, "YOU TIED", " ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            categorybutton.setEnabled(true);
            categoryChooser.setEnabled(true);
            label.setText(message);
        }
    }//showTheMessageFromTheServer

    String theAnswerFromUser;
    ActionListener continueButtonListener = e -> {
        continueButton.setVisible(false);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(null);
        }


        pw.println(theAnswerFromUser);
    };//cnt


    ActionListener alternativesListener = e -> {
        JButton temp = (JButton) e.getSource();
        categoryChooser.setEnabled(false);
        categorybutton.setEnabled(false);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
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

            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].getText().equalsIgnoreCase(rightAnswer)) {
                    buttons[i].setBackground(Color.green);
                    buttons[i].setOpaque(true);
                }
            }
        }
    }//changeColor

    public static void main(String[] args) {

        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
