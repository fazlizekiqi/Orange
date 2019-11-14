package client;

import Question.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {

//172.20.202.126

    JLabel label = new JLabel("Vad heter du?");
    JButton[] buttons;
    String[] alternativ = {"Fazli", "Johan", "VictorO", "VictorJ"};
    JPanel panel = new JPanel();
    Socket socket = new Socket("172.20.202.139", 56565);
    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


    public Client() throws IOException {

        buttons = new JButton[4];
        add(label, BorderLayout.NORTH);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(alternativ[i]);
            buttons[i].addActionListener(clientListener);
            panel.add(buttons[i], BorderLayout.SOUTH);
        }
        add(panel);
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameLoop();
    }

    public void gameLoop() {
        Object obj;

        try {
            while ((obj = in.readObject()) != null) {
                if (obj instanceof Question) {
                    Question q = (Question) obj;
                    //label.setText(q.getQuestion);
                    //TODO display buttons and make them visible/invisible
                } else if (obj instanceof String) {
                    String messageFromTheServer = (String) obj;
                    System.out.println(messageFromTheServer);
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
    }

    ActionListener clientListener = e -> {
        JButton temp = (JButton) e.getSource();
        pw.println(temp.getText());
    };

    public static void main(String[] args) {
        try {
            Client client = new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
