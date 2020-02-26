package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {


    private static DatagramSocket socket;
    private static boolean alive;

    private static ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();
    private static int ClientId;


    public static void Start(int port){

        try{

            socket = new DatagramSocket(port);

            alive = true;
            threadListen();
            System.out.println("Server started on port: "+port);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void sendAllClients(String message){

        for(ClientInfo info : clients){
            send(message, info.getAddress(), info.getPort());
        }

    }

    private static void send(String message, InetAddress address, int port){

        try{

            message += "\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Sent to: "+address.getHostAddress()+ ":"+port);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private static void threadListen(){

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
                            sendAllClients(message);
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

            String name = message.substring(message.indexOf(":")+1);

            clients.add(new ClientInfo(name, ClientId++, packet.getAddress(), packet.getPort()));
            sendAllClients("User "+name+" connected");
            return true;
        }

        return false;
    }

    public static void close(){

        alive = false;

    }


}
