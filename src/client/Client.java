package client;

import java.awt.event.ActionListener;
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
    private JComboBox colChooser ;
    private JPanel p = new JPanel();
    private final String[] nr = {"2", "3","4","5","6","7","8","9","10"};
    private JComboBox numbers ;
    private final String[] rung = {"1", "2","3","4"};
    private JComboBox runder ;
    JButton button=new JButton("Start Game");
    JButton[] buttons=new JButton[4];
    String[] strings={"Allan","Fazli Zekiqi","Victor J","Victor O"};

    JLabel label=new JLabel("HÄR KOMMMER VI HA FRÅGAN?", SwingConstants.CENTER);
    JLabel s1=new JLabel("Player 1");
    JLabel s2=new JLabel("Player 2");

    JPanel gridPanel=new JPanel(new GridLayout(2,2));
    JPanel centerPanel=new JPanel(new BorderLayout());

    public Client() throws IOException {
        socket=new Socket("localhost",56565);
        in=new ObjectInputStream(socket.getInputStream());
        pw=new PrintWriter(socket.getOutputStream(),true);
        setLayout(new BorderLayout());
        numbers=new JComboBox(nr);
        numbers.setSelectedItem(-1);
        colChooser=new JComboBox(colors);
        colChooser.setSelectedIndex(-1);
        runder=new JComboBox(rung);
        runder.setSelectedIndex(-1);


        p.add(numbers);
        p.add(runder);
        p.add(colChooser);
        p.add(button);
        centerPanel.add(label, BorderLayout.CENTER);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i]=new JButton(strings[i]);
            buttons[i].addActionListener(clientListener);
            gridPanel.add(buttons[i]);

        }

        add(s1,BorderLayout.WEST);
        add(s2,BorderLayout.EAST);
        button.addActionListener(e->{
            numbers.setEnabled(false);
            colChooser.setEnabled(false);
            runder.setEnabled(false);
        });
        centerPanel.add(gridPanel,BorderLayout.SOUTH);
        add(centerPanel,BorderLayout.CENTER);
        add(p,BorderLayout.NORTH);


        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameLoop();

    }//CONSTRUCTOR

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
    }//gameLoop()

    ActionListener clientListener = e -> {
        JButton temp = (JButton) e.getSource();
        pw.println(temp.getText());
        System.out.println(temp.getText());
    };//clientListener

    public static void main(String[] args) {
        try {
            Client client = new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
