/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Process;

/**
 *
 * @author Marcelo
 */
public class P1 {

    public static void main(String[] args) {
        int serverPort = 6789;
        int[] connectionPorts = {6790, 6791};
        Process p = new Process();
        p.exec(1, serverPort, connectionPorts);
        //exec(pid do processo, porta do servidor, vetor de conexões)
    }
}
