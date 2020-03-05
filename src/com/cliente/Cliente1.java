package com.cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente1 {
    //Server port
    static int SERVER_PORT = 1024;

    public static void main(String[] args) throws IOException {
        InetAddress ip = InetAddress.getByName("localhost");

        Socket socket = new Socket(ip, SERVER_PORT);

        DataInputStream dis = new DataInputStream(socket.getInputStream());

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        JFrame f = new JFrame("Send it!"); //Crea instancia de JFrame
        Font label_font = new Font("ARIAL", Font.PLAIN, 30);
        Font entry_font = new Font("ARIAL", Font.PLAIN, 20);



        JLabel port_label = new JLabel("PUERTO: ");
        port_label.setBounds(160, 240, 150, 30);
        port_label.setFont(label_font);

        JTextField port_entry = new JTextField();
        port_entry.setBounds(310, 240, 150, 30);
        port_entry.setFont(entry_font);

        JButton send_f = new JButton("SEND IT!");
        send_f.setBounds(335,280,100,40);

        f.add(port_entry);f.add(port_label);f.add(send_f);

        f.setSize(720, 480);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame i = new JFrame("Send it!");

        JTextArea mensajes=new JTextArea();
        mensajes.setBounds(10,100, 690, 300);

        JTextField mensajes_entry=new JTextField("Message");
        mensajes_entry.setBounds(100, 410, 500, 30);

        JButton mensajes_send=new JButton("SEND IT");
        mensajes_send.setBounds(610, 410, 90, 30);

        JTextField recipient_entry  = new JTextField("Cliente");
        recipient_entry.setBounds(10, 410, 90, 30);


        i.add(mensajes);i.add(mensajes_entry);i.add(mensajes_send);i.add(recipient_entry);
        i.setSize(720,480);
        i.setLayout(null);
        i.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mensajes_entry.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                mensajes_entry.setText("");
            }
        });

        recipient_entry.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                recipient_entry.setText("");
            }
        });
        send_f.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String server_port = port_entry.getText();

                if (server_port.equals("")){
                    JOptionPane.showMessageDialog(null, "Select a server port!");

                } else {
                    f.setVisible(false);
                    i.setVisible(true);
                }
            }
        }); //fin metodo send_f.addActionListener

        mensajes_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SendMessage send = new SendMessage(dos, recipient_entry.getText(), mensajes_entry.getText());
                send.start();
                mensajes_entry.setText("Message");
                recipient_entry.setText("Cliente");
            }
        });
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try{
                        String msg = dis.readUTF();
                        mensajes.append(msg);
                        mensajes.append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        readMessage.start();

    }

}

class SendMessage extends Thread{
    DataOutputStream dos;
    String recipient;
    String text;

    public SendMessage(DataOutputStream dos, String recipient, String text){
        this.dos=dos;
        this.recipient=recipient;
        this.text=text;
    }
    @Override
    public void run(){
        while (true) {
            String msg = text+"#"+recipient;
            try{
                dos.writeUTF(msg);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
