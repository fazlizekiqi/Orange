package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Client extends JFrame {

//172.20.202.126

    JLabel label=new JLabel("Vad heter du?");
    JButton[] buttons;
    String[] alternativ={"Fazli","Johan","VictorO","VictorJ"};
    JPanel panel=new JPanel();
    Socket socket=new Socket("localhost",56565);
    PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);

    public Client() throws IOException {


        buttons=new JButton[4];
        add(label, BorderLayout.NORTH);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i]=new JButton(alternativ[i]);
            buttons[i].addActionListener(clientListener);
            panel.add(buttons[i], BorderLayout.SOUTH);
        }
        add(panel);
        setSize(400,400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }


    ActionListener clientListener=e->{
        JButton temp=(JButton) e.getSource();
        pw.println(temp.getText());
    };


    public static void main(String[] args) {
        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
