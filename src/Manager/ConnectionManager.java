/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Tools.Convert;
import Listener.ServerListener;
import Listener.ServerManagerListener;
import Model.ACK;
import Connection.Client;
import Model.Message;
import Connection.Server;

/**
 *
 * @author Isabella
 */
public class ConnectionManager {
    private Client client = null;
    private Server server;
    private ServerManagerListener serverManagerListener;
    
    public ConnectionManager(int serverPort){
        //Cria cliente
        client = new Client();
        
        //Cria servidor
        this.server = new Server(serverPort);
        
        this.server.setServerListener(new ServerListener() {
            @Override
            public void receivePacket(String json) {
                if(Convert.isACK(json)){
                    serverManagerListener.ACKReceived(Convert.JsonToACK(json));
                } else {
                    serverManagerListener.messageReceived(Convert.JsonToMessage(json));
                }
            }
        });
    }

    public void setServerManagerListener(ServerManagerListener serverManagerListener) {
        this.serverManagerListener = serverManagerListener;
    }
    
    //Adiciona as conexões aos outros processos
    public void addConnection(int port){
        client.addConnection(port);
    }
    
    //Envia mensagem para o servidor
    public void sendMessageToServer(Message message) throws Exception{
        client.outToServer(Convert.MessageToJson(message));
    }
    
    //Envia ACK para o servidor
    public void sendACKToServer(ACK ack) throws Exception{
        client.outToServer(Convert.ACKToJson(ack));
    }
    
    //Encerra o processo e fecha as conexões
    public void close(){
        try {
            this.client.close();
            this.server.close();
        } catch (Exception e) {
        }
    }
}
