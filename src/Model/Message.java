/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcelo
 */
public class Message {
    private int recurso;
    private int pid; //Id que identifica a mensagem
    private int time; //Refere-se ao clock da mensagem

    public Message(){}
    
    public Message(int recurso, int pid, int time){
        this.recurso = recurso;
        this.pid = pid;
        this.time = time;
    }

    public int getRecurso() {
        return recurso;
    }

    public void setRecurso(int recurso) {
        this.recurso = recurso;
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
