/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Marcelo
 */
public class ACK {
    private int id; //PID do processo que está enviando o ACK
    private int time; //Clock
    private int resource; //Recurso que o processo quer utilizar
    private int dest; //Processo que pediu o recurso
    private int isAck;

    //Retorna ID da mensagem
    public int getId() {
        return id;
    }

    //Define ID da mensagem
    public void setId(int id) {
        this.id = id;
    }

    //Retorna clock do ACK
    public int getTime() {
        return time;
    }

    //Define clock do ACK
    public void setTime(int time) {
        this.time = time;
    }

    //Retorna o processo que está enviando o ACK
    public int getProcess() {
        return resource;
    }

    //Define o processo que está enviando o ACK
    public void setProcess(int recsource) {
        this.resource = resource;
    }

    public void setIsAck(int isAck) {
        this.isAck = isAck;
    }
    
    public int getIsAck() {
        return isAck;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public int getDest() {
        return dest;
    }
}
