package client;

import javax.swing.*;
import java.awt.*;




public class ClientApp {



    private JPanel panel;
    private JLabel label;
    private static JTextField tf;
    private JButton send;

    private static JTextArea ta;

    private Client client;


    public static void main(String args[]){


        ClientApp app = new ClientApp();

    }

    public ClientApp(){
        initialize();
        String name = JOptionPane.showInputDialog("Enter name");
        client = new Client(name, "localhost", 52864);

    }

    private void initialize(){
        //Creating the Frame
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);




        //Creating the panel at bottom and adding components
        panel = new JPanel(); // the panel is not visible in output
        label = new JLabel("Enter Text");
        tf = new JTextField(10); // accepts upto 10 characters
        send = new JButton("Send");
        send.addActionListener(e -> {

            client.Send(tf.getText());
            tf.setText("");

        });

        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);




        // Text Area at the Center
        ta = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }

    public static void printToConsole(String message){
        ta.setText(ta.getText()+message+"\n");
    }



}
