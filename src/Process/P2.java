/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Process;

import Listener.ServerManagerListener;
import Manager.ConnectionManager;
import Model.ACK;
import Model.Message;
import static Process.P1.sendMessage;
import java.util.Scanner;

/**
 *
 * @author Marcelo
 */
public class P2 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner keyboard = new Scanner(System.in);
        int clock = 2;
        int serverPort = 6790;
        ConnectionManager manager = new ConnectionManager(serverPort);
        manager.setServerManagerListener(new ServerManagerListener() {
            @Override
            public void messageReceived(Message message) {
                // recebeu uma mensagem
            }

            @Override
            public void ACKReceived(ACK ack) {
                // recebeu um ack
            }
        });
        
        System.out.println("Iniciou P2 na porta 6790");
        
        int esperaEntrada = keyboard.nextInt();
        
        manager.addConnection(6789);
        System.out.println("Criou conexão com a partoa 6789");
        manager.addConnection(6791);
        System.out.println("Criou conexão com a partoa 6791");
        
        int option;
        do{
            option = keyboard.nextInt();
            if(option!=0){
                try {
                    sendMessage(manager, "2", clock);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while(option!=0);
        
        manager.close();
        System.out.println("Finalizou P2");
    }
    
    public static void sendMessage(ConnectionManager cm, String id, int time) throws Exception{
        Message m = new Message(id, time);
        cm.sendMessageToServer(m);
    }
}
