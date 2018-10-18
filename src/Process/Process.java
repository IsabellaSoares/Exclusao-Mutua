/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Process;

import Listener.ServerManagerListener;
import Manager.ConnectionManager;
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
    private static int clock = 1;
    private static boolean restricState = false;
    private static int pid;
    private static Scanner keyboard;
    
    public void exec(int pid, int serverPort, int[] connectionPorts) {
        this.pid = pid;
        keyboard = new Scanner(System.in);
        List<Structure> messageList = new ArrayList<Structure>(); //Lista de mensagens
                
        ConnectionManager manager = new ConnectionManager(serverPort); //Gerenciador de conexão
        
        manager.setServerManagerListener(new ServerManagerListener() {
            @Override
            public void messageReceived(Message message) {
                // recebeu uma mensagem
                System.out.println("[Log] P"+message.getId()+" solicitou acesso.");                
                //addMessagem(messageList, message); //Adiociona mensagem na lista
                //sendACK(manager, pid, message); //Envia ACK
            }

            @Override
            public void ACKReceived(ACK ack) {
                //Recebeu um ACK
                System.out.println("[Log] Recebeu um 'Okay' de P" + ack.getProcess());
                ackReceived(messageList, ack); //Adiciona ACK na lista
                updateList(messageList);
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
            ack.setId(message.getId());
            ack.setTime(message.getTime());
            ack.setProcess(pid);
            
            // Verificar aqui se é ACK ou NACK
            
            cm.sendACKToServer(ack);
        } catch (Exception e) {
        }
    }
    
    public static synchronized void messageReceived(List<Structure> lista, Message message){
        boolean addedToList = false;
        for(int i=0; i<lista.size() && !addedToList; i++){
            Structure currentStructure = lista.get(i);
            if(message.getTime()==currentStructure.getTime()){
                // item da lista de mensagens tem o mesmo clock da mensagem recebida
                // comparar o pid
                if(message.getId()<currentStructure.getId()){
                    // o pid da mensagem recebida eh menor do que o pid do item da lista
                    // cria uma nova estrutura e insere na lista
                    Structure newStructure = new Structure();
                    newStructure.setMessage(message);
                    lista.add(i, newStructure);
                    addedToList = true;
                } else if(message.getId()==currentStructure.getId()){
                    // o pid da mensagem recebida eh igual ao pid do item da lista
                    // foi recebido algum ack dessa mensagem antes da propria mensagem chegar
                    // atualiza a estrutura com a mensagem
                    currentStructure.setMessage(message);
                    addedToList = true;
                }
            } else if(message.getTime()>currentStructure.getTime()){
                // mensagem recebida tem o clock maior que o do item da lista
                // cria uma nova estrutura e insere na lista
                Structure newStructure = new Structure();
                newStructure.setMessage(message);
                lista.add(i, newStructure);
                addedToList = true;
            }
        }
        if(!addedToList){
            Structure e = new Structure();
            e.setMessage(message);
            lista.add(e);
        }
    }
    
    public static synchronized void ackReceived(List<Structure> lista, ACK ack){
        boolean addedToList = false;
        
        if (ack.getIsAck() == 1) {
            for(int i=0; i<lista.size() && !addedToList; i++){
                Structure currentStructure = lista.get(i);
                if(ack.getTime()==currentStructure.getTime()){
                    // item da lista de mensagens tem o mesmo clock do ack recebido
                    // comparar o pid
                    if(ack.getId()<currentStructure.getId()){
                        // o pid do ack recebida eh menor do que o pid do item da lista
                        // cria uma nova estrutura e insere na lista
                        Structure newStructure = new Structure();
                        newStructure.addACK(ack);
                        lista.add(i, newStructure);
                        addedToList = true;
                    } else if(ack.getId()==currentStructure.getId()){
                        // o pid da mensagem recebida eh igual ao pid do item da lista
                        // foi recebido algum ack dessa mensagem antes da propria mensagem chegar
                        // atualiza a estrutura com a mensagem
                        currentStructure.addACK(ack);
                        addedToList = true;
                    }
                } else if(ack.getTime()>currentStructure.getTime()){
                    // mensagem recebida tem o clock maior que o do item da lista
                    // cria uma nova estrutura e insere na lista
                    Structure newStructure = new Structure();
                    newStructure.addACK(ack);
                    lista.add(i, newStructure);
                    addedToList = true;
                }
            }
            if(!addedToList){
                Structure e = new Structure();
                e.addACK(ack);
                lista.add(e);
            }
        } else {
            if (ack.getDest()== pid) {
                System.out.println("[Log] O processo "+ack.getId()+" está utilizando o recurso "+ack.getProcess()+". Aguarde.");
            }
        }
    }
    
    public static synchronized void updateList(List<Structure> lista){
        try{
            boolean removed = true;
            for(int i=0; i<lista.size() && removed; i++){
                Structure currentStructure = lista.get(i);
                if(currentStructure.getMessage()!=null && currentStructure.getNumbersOfACK()==3){
                    if(currentStructure.getTime() > clock){
                        clock = currentStructure.getTime();
                    }
                    lista.remove(i--);
                } else {
                    removed = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Deu erro updateList()!");
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
