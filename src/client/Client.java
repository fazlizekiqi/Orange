package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;
import question.Question;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Client extends JFrame {

    Socket socket;
    ObjectInputStream in;
    PrintWriter pw;
    private final String[] colors = {"Candy", "Egg", "Famous", "Random"};
    private JComboBox categoryChooser;
    private JPanel p = new JPanel();
    private final String[] nr = {"2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private JComboBox numbers;
    private final String[] rung = {"1", "2", "3", "4"};
    private JComboBox runder;
    JButton categorybutton = new JButton("Start Game");
    JButton continueButton = new JButton("Continue");
    JButton[] buttons = new JButton[4];
    String[] strings = {"Allan", "Fazli Zekiqi", "Victor J", "Victor O"};
    JLabel label = new JLabel("HÄR KOMMMER VI HA FRÅGAN?", SwingConstants.CENTER);
    JLabel s1 = new JLabel("Player 1");
    JLabel s2 = new JLabel("Player 2");
    JPanel gridPanel = new JPanel(new GridLayout(2, 2));
    JPanel centerPanel = new JPanel(new BorderLayout());

    String rightAnswer;

    public Client() throws IOException {
        socket = new Socket("localhost", 56565);
        in = new ObjectInputStream(socket.getInputStream());
        pw = new PrintWriter(socket.getOutputStream(), true);

        gridPanel.setPreferredSize(new Dimension(500, 200));
        gridPanel.setBorder(new EmptyBorder(0, 30, 0, 0));
        setLayout(new BorderLayout());
        categoryChooser = new JComboBox(colors);
        categoryChooser.setSelectedIndex(0);

//        numbers=new JComboBox(nr);
//        numbers.setSelectedItem(-1);
//        runder=new JComboBox(rung);
//        runder.setSelectedIndex(-1);
//        p.add(numbers);
//        p.add(runder);

        p.add(categoryChooser);
        p.add(categorybutton);
        centerPanel.add(label, BorderLayout.CENTER);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(strings[i]);
            buttons[i].addActionListener(clientListener);
            gridPanel.add(buttons[i]);

        }

        add(continueButton, BorderLayout.SOUTH);
        continueButton.setVisible(false);
        add(s1, BorderLayout.WEST);
        add(s2, BorderLayout.EAST);

        centerPanel.add(gridPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        add(p, BorderLayout.NORTH);

        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameLoop();

    }//CONSTRUCTOR

    public void gameLoop() {
        Object obj;
        continueButton.addActionListener(cnt);

        try {
                while ((obj = in.readObject()) != null) {
                    if (obj instanceof Question) {
                        Question q = (Question) obj;
                        label.setText(q.getQuestion());
                        ArrayList<String> alt = q.getAlternatives();
                        rightAnswer = q.getRightAnswer();
                        for (int i = 0; i < alt.size(); i++) {

                            buttons[i].setText(alt.get(i));
                        }

                        //TODO display buttons and make them visible/invisible
                    } else if (obj instanceof String) {
                        String messageFromTheServer = (String) obj;
                        if (messageFromTheServer.startsWith("wait")){
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                        }
                        label.setText(messageFromTheServer);
                        categorybutton.addActionListener(e -> {
                            categoryChooser.setEnabled(false);
                            pw.println(categoryChooser.getSelectedItem());
                            categorybutton.setEnabled(false);
                        });
                    } else if (obj instanceof Integer[]) {
                        Integer[] points = (Integer[]) obj;
                        System.out.println("Spelare 1 points :" + points[0]);
                        System.out.println("Spelare 2 points :" + points[1]);
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
// throws IOException, ClassNotFoundException
    }//gameLoop()

    String theAnswer;

    ActionListener cnt = e -> {
        continueButton.setVisible(false);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(null);
        }
        pw.println(theAnswer);
    };

    ActionListener clientListener = e -> {

        JButton temp = (JButton) e.getSource();
        changeColor(temp);
        continueButton.setVisible(true);
        theAnswer = temp.getText();
        System.out.println(theAnswer);
    };//clientListener

    private void changeColor(JButton temp) {
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
    }

    public static void main(String[] args) {

        try {
            new Client();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
