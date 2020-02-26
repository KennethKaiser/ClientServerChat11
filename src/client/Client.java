package client;


import jdk.internal.org.objectweb.asm.tree.InnerClassNode;
import server.ClientInfo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    private DatagramSocket socket;
    private InetAddress address;
    private String name;
    private int port;
    private boolean alive;

    public Client(String name, String address, int port){
        try{
            this.name = name;
            this.address = InetAddress.getByName(address);
            this.port = port;

            socket = new DatagramSocket();

            alive =true;
            threadListen();
            Send("\\JOIN:" +name);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void Send(String message){
        try{

            if(!message.startsWith("\\")){
                message = name+": "+message;
            }

            message += "\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void threadListen(){

        Thread thread = new Thread("ChatSystem Listener"){

            public void run(){

                try {

                    while(alive){

                        byte[] data = new byte[1024];

                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);

                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e"));

                        if(!isCommand(message, packet)) {

                            ClientApp.printToConsole(message);

                        }
                    }

                } catch (Exception e){

                }

            }

        };
        thread.start();

    }

    private static boolean isCommand(String message, DatagramPacket packet){

        if(message.startsWith("\\JOIN:"))
        {


            return true;
        }

        return false;
    }

}
