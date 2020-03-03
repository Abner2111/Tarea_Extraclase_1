package com.multi_client_server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Vector;
import java.util.Scanner;
import javax.swing.*;

public class Multi_client_server {

    //codigo basado en codigo encontrado en https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
    //Multi-threaded chat Application in Java
    //vector que almacena los clientes activos
    static Vector<ClientHandler> ar = new Vector<>();

    static int i = 0;

    //Port to listen to
    static int PORT = 5000;

    public static void main(String[] args) {
        JFrame f = new JFrame("Send it!: SERVER"); //Crea instancia de JFrame

        JButton stop = new JButton("Stop!");
        stop.setBounds(15, 15, 50,50);

        f.setSize(80, 80);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(stop);


        final boolean[] flag1 = {true};
        ServerSocket server;

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                flag1[0] = false;

            }
        });

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started on port: " + PORT);

            System.out.println("Waiting for client...");
            Socket socket;

            //running infinite loop for getting client request

            while (flag1[0]) {
                //Creating socket on incoming requests
                socket = server.accept();

                System.out.println("New client accepted");

                //Obtain input and output streams
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                String sock_adr = socket.getInetAddress().toString();

                System.out.println("Creating a new Handler object to for the client" + i + " with ip: " + sock_adr);

                ClientHandler mtch = new ClientHandler(socket, "client" + i, dis, dos);

                System.out.println("Adding client " + i + " to active client list");

                //add tis client to active client vector
                ar.add(mtch);

                mtch.start();

                i++;

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

    }
}
class ClientHandler extends Thread{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket socket;
    boolean isloggedin;

    //constructor
    public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos) {
        this.name = name;
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

}


