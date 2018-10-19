/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcelo
 */
public class Message implements Serializable{
    private int resource;
    private int pid; //Id que identifica a mensagem
    private int time; //Refere-se ao clock da mensagem

    public Message(){}
    
    public Message(int recurso, int pid, int time){
        this.resource = recurso;
        this.pid = pid;
        this.time = time;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
    
    //Retorna o pid do processo que está enviando a mensagem
    public int getId() {
        return pid;
    }

    //Define o pid do processo que está enviando a mensagem
    public void setId(int pid) {
        this.pid = pid;
    }

    //Retorna clock da mensagem
    public int getTime() {
        return time;
    }

    //Define o clock da mensagem a ser enviada
    public void setTime(int time) {
        this.time = time;
    }
}
