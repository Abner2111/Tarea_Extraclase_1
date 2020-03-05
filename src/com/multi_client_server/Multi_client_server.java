package com.multi_client_server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

public class Multi_client_server {

    //codigo basado en codigo encontrado en https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
    //Multi-threaded chat Application in Java
    //vector que almacena los clientes activos
    static Vector<ClientHandler> ar = new Vector<>();

    static int i = 0;

    //Port to listen to
    static int PORT = 500;

    public static void main(String[] args) throws IOException {
        JFrame f = new JFrame("Send it!: SERVER"); //Crea instancia de JFrame

        JButton stop = new JButton("Stop!");
        stop.setBounds(150, 200, 100,100);

        JLabel port = new JLabel("");
        port.setBounds(150, 100, 100, 100);

        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(stop);f.add(port);


        final boolean[] flag1 = {true};
        ServerSocket server;

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                flag1[0] = false;
                System.out.println("Stopped listening");

            }
        });
        while(true){
            try {
                server = new ServerSocket(PORT);
                break;
            } catch (IOException e) {
                PORT += 1;
                System.out.println(e.getMessage());
                }
        }
        System.out.println("Server started listening on port: " + PORT);

        port.setText("PUERTO: "+String.valueOf(PORT));

        System.out.println("Waiting for client...");
        Socket socket;
        //running infinite loop for getting client request

        while (flag1[0]) {
            //Creating socket on incoming requests
            socket = server.accept();

            System.out.println("New client accepted: " + socket);

            //Obtain input and output streams
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String sock_adr = socket.getInetAddress().toString();

            System.out.println("Creating a new Handler object to for the client" + i + " with ip: " + sock_adr);

            //instanciacion de clase clientHandler
            ClientHandler mtch = new ClientHandler(socket, "client" + i, dis, dos);

            System.out.println("Adding client " + i + " to active client list");

            //add tis client to active client vector
            ar.add(mtch);

            mtch.start();

            i++;

        }


    }
}
//Herencia con la plabra extend. CLientHandler hereda de Thread

//Abnstraccion se crea una clase
class ClientHandler extends Thread{
    private String name; //encapsulacion private
    final DataInputStream dis;
    final DataOutputStream dos; //atributos
    Socket socket;
    boolean isloggedin;

    //constructor
    public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos) {
        this.name = name;
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
        this.isloggedin = true;
    }

    private String get_name(){
        return (this.name);
    }

    public void run(){ //se sobrescribe el metodo run aplicando polimorfismo

        String message;

        while(true){
            try{
                //receive the message
                message = dis.readUTF();

                System.out.println(message);

                if (message.equals("EndCom")){
                    this.isloggedin=false;
                    this.socket.close();
                    Multi_client_server.ar.removeElement(this);
                    break;
                }
                else if (message.equals("Name?")){
                    this.dos.writeUTF(this.getName());
                }
                //breaks the string into message and recipients
                StringTokenizer st = new StringTokenizer(message, "#");
                String MesgToSend = st.nextToken();
                String recipient = st.nextToken();

                //search for recipient in the connected devices list

                for (ClientHandler mc : Multi_client_server.ar){
                    if (mc.name.equals(recipient) && mc.isloggedin){

                        mc.dos.writeUTF(this.name+" : "+MesgToSend);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            //closing DataInputStreams and DataOutputStreams
            this.dis.close();
            this.dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


