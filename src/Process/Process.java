/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Process;

import Listener.ServerManagerListener;
import Manager.ConnectionManager;
import Manager.MessageManager;
import Model.ACK;
import Model.Structure;
import Model.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Marcelo
 */
public class Process {
    public static int clock = 1;
    private static boolean restricState = false;
    private static int pid;
    private static Scanner keyboard;
    
    public void exec(int pid, int serverPort, int[] connectionPorts) {
        this.pid = pid;
        keyboard = new Scanner(System.in);
        
                
        ConnectionManager manager = new ConnectionManager(serverPort); //Gerenciador de conexão
        
        MessageManager messageManager = new MessageManager(pid);
        
        manager.setServerManagerListener(new ServerManagerListener() {
            @Override
            public void messageReceived(Message message) {
                // recebeu uma mensagem
                System.out.println("[Log] P"+message.getId()+" solicitou acesso.");                
                messageManager.addMessage(message); //Adiociona mensagem na lista
                sendACK(manager, pid, message); //Envia ACK
            }

            @Override
            public void ACKReceived(ACK ack) {
                //Recebeu um ACK
                if(ack.getDest()==pid){
                    System.out.println("[Log] Recebeu um 'Okay' de P" + ack.getId());
                }
                messageManager.addAck(ack); //Adiciona ACK na lista
            }
        });
        
        //Inicia o servidor do processo e aguarda que todos os outros processos sejam iniciados
        System.out.println("Iniciou P"+pid+" na porta "+serverPort);
        System.out.print("Pressione 1 para iniciar as conexões:\n>> ");
        keyboard.next();
        
        //Cria conexão com os outros processos e com ele mesmo
        manager.addConnection(connectionPorts[0]);
        System.out.println("Criou conexão com a porta "+ connectionPorts[0]);
        manager.addConnection(connectionPorts[1]);
        System.out.println("Criou conexão com a porta " + connectionPorts[1]);
        manager.addConnection(serverPort);
        System.out.println("Criou conexão com a porta " + serverPort);
        
        System.out.print("\nMenu:\n 0 - Finaliza processo\n 1 - Solicitar acesso\n 2 - Ver clock\n");
        
        int option = 0;
        int recurso = 0;
        
        do{
            option = keyboard.nextInt();
            
            if(option!=0){
                switch(option){
                    case 1 : 
                        //Envia mensagem para o servidor
                        do {
                            System.out.print("Escolha um recurso: ");
                            recurso = keyboard.nextInt();                            
                        } while (recurso < 1 || recurso > 4);
                        
                        try{
                            Message m = new Message(recurso, pid, ++clock);
                            manager.sendMessageToServer(m);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        //Exibe o valor de clock
                        System.out.println("Clock: " + clock);
                        break;
                    case 3:
                        messageManager.printList();
                        break;
                }
            }
        } while(option!=0);
        
        manager.close();
        System.out.println("Finalizou P"+pid);
        return;
    }
    
    public static void sendACK(ConnectionManager cm, int pid, Message message){
        try {
            ACK ack = new ACK();
            ack.setId(pid);
            ack.setTime(message.getTime());
            ack.setResource(message.getResource());
            ack.setIsAck(1);
            ack.setDest(message.getId());
            // Verificar aqui se é ACK ou NACK
            
            cm.sendACKToServer(ack);
        } catch (Exception e) {
        }
    }
    
    
//    public static void restricArea () {
//        restricState = true;
//        System.out.println("Processo P"+pid+" entrou na área restrita. Aperte 1 para sair.");
//        int option = 0;
//        
//        do {
//            option = keyboard.nextInt();
//            System.out.println("option: "+option);
//        } while (option != 1);
//        
//        restricState = false;
//        System.out.println("Processo P"+pid+" está saindo da área restrita.");
//    }
}
