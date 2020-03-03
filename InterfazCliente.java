import javax.swing.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;

public class InterfazCliente{
    public static void main(String[] args){
        JFrame f = new JFrame("Send it!"); //Crea instancia de JFrame
        Font label_font = new Font("ARIAL", Font.PLAIN, 30);
        Font entry_font = new Font("ARIAL", Font.PLAIN, 20);
        //Entradas de texto
        JTextField ip_entry, port_entry, mensajes_entry;
        JLabel ip_label, port_label;
        JTextArea mensajes;
        JButton send_f, mensajes_send;

        ip_label = new JLabel("I.P: ");
        ip_label.setBounds(250, 200, 60, 30);
        ip_label.setFont(label_font);

        ip_entry=new JTextField();
        ip_entry.setBounds(310,200,150,30);
        ip_entry.setFont(entry_font);

        port_label = new JLabel("PUERTO: ");
        port_label.setBounds(160, 240, 150, 30);
        port_label.setFont(label_font);

        port_entry = new JTextField();
        port_entry.setBounds(310, 240, 150, 30);
        port_entry.setFont(entry_font);

        send_f = new JButton("SEND IT!");
        send_f.setBounds(335,280,100,40);

        f.add(ip_entry);f.add(ip_label);f.add(port_entry);f.add(port_label);f.add(send_f);

        f.setSize(720, 480);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame i = new JFrame("Send it!");

        mensajes=new JTextArea();
        mensajes.setBounds(10,100, 690, 300);

        mensajes_entry=new JTextField();
        mensajes_entry.setBounds(10, 410, 600, 30);

        mensajes_send=new JButton("SEND IT");
        mensajes_send.setBounds(610, 410, 90, 30);

        i.add(mensajes);i.add(mensajes_entry);i.add(mensajes_send);
        i.setSize(720,480);
        i.setLayout(null);
        i.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        send_f.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                f.setVisible(false);
                i.setVisible(true);
            }
        }); //fin metodo send_f.addActionListener

    }
}