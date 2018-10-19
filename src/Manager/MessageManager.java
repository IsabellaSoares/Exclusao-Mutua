/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Listener.MessageManagerListener;
import Model.ACK;
import Model.Message;
import Model.Structure;
import static Process.Process.clock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Isabella
 */
public class MessageManager {
    
    private int pid;
    private MessageManagerListener listener;
    private List<Structure> messageList; //Lista de mensagens
    
    public MessageManager(int pid){
        this.pid = pid;
        this.messageList = new ArrayList<Structure>();
    }

    public void setListener(MessageManagerListener listener) {
        this.listener = listener;
    }
    
    public synchronized void addMessage(Message message){
        boolean addedToList = false;
        for(int i=0; i<messageList.size() && !addedToList; i++){
            Structure currentStructure = messageList.get(i);
            if(message.getTime()==currentStructure.getTime()){
                // item da lista de mensagens tem o mesmo clock da mensagem recebida
                // comparar o pid
                if(message.getId()<currentStructure.getId()){
                    // o pid da mensagem recebida eh menor do que o pid do item da lista
                    // cria uma nova estrutura e insere na lista
                    Structure newStructure = new Structure();
                    newStructure.setMessage(message);
                    messageList.add(i, newStructure);
                    addedToList = true;
                } else if(message.getId()==currentStructure.getId()){
                    // o pid da mensagem recebida eh igual ao pid do item da lista
                    // foi recebido algum ack dessa mensagem antes da propria mensagem chegar
                    // atualiza a estrutura com a mensagem
                    currentStructure.setMessage(message);
                    addedToList = true;
                }
            } else if(message.getTime()<currentStructure.getTime()){
                // mensagem recebida tem o clock maior que o do item da lista
                // cria uma nova estrutura e insere na lista
                Structure newStructure = new Structure();
                newStructure.setMessage(message);
                messageList.add(i, newStructure);
                addedToList = true;
            }
        }
        if(!addedToList){
            Structure e = new Structure();
            e.setMessage(message);
            messageList.add(e);
        }
    }
    
    public synchronized void addAck(ACK ack){
        boolean addedToList = false;
        
        if (ack.getIsAck() == 1) {
            for(int i=0; i<messageList.size() && !addedToList; i++){
                Structure currentStructure = messageList.get(i);
                if(ack.getTime()==currentStructure.getTime()){
                    // item da lista de mensagens tem o mesmo clock do ack recebido
                    // comparar o pid
                    if(ack.getDest()<currentStructure.getId()){
                        // o pid do ack recebida eh menor do que o pid do item da lista
                        // cria uma nova estrutura e insere na lista
                        Structure newStructure = new Structure();
                        newStructure.addACK(ack);
                        messageList.add(i, newStructure);
                        addedToList = true;
                    } else if(ack.getDest()==currentStructure.getId()){
                        // o pid da mensagem recebida eh igual ao pid do item da lista
                        // foi recebido algum ack dessa mensagem antes da propria mensagem chegar
                        // atualiza a estrutura com a mensagem
                        currentStructure.addACK(ack);
                        addedToList = true;
                    }
                } else if(ack.getTime()<currentStructure.getTime()){
                    // mensagem recebida tem o clock maior que o do item da lista
                    // cria uma nova estrutura e insere na lista
                    Structure newStructure = new Structure();
                    newStructure.addACK(ack);
                    messageList.add(i, newStructure);
                    addedToList = true;
                }
            }
            if(!addedToList){
                Structure e = new Structure();
                e.addACK(ack);
                messageList.add(e);
            }
        } else {
            if (ack.getDest()== pid) {
                System.out.println("[Log] O processo "+ack.getId()+" está utilizando o recurso "+ack.getResource()+". Aguarde.");
            }
        }
        
        updateList();
    }
    
    private synchronized void updateList(){
        try{
            boolean removed = true;
            for(int i=0; i<messageList.size() && removed; i++){
                Structure currentStructure = messageList.get(i);
                if(currentStructure.getMessage()!=null && currentStructure.getNumbersOfACK()==3){
                    if(currentStructure.getTime() > clock){
                        clock = currentStructure.getTime();
                    }
                    //lista.remove(i--);
                } else {
                    removed = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Deu erro updateList()!");
        }
    }
    
    public synchronized void printList(){
        for(Structure structure : messageList){
            
        }
        for(int i=0; i<messageList.size(); i++){
            Structure structure = messageList.get(i);
            System.out.println("["+i+"] pid: "+structure.getId() + " - time: " + structure.getTime() + " - recurso: " + structure.getResource());
        }
    }
}
