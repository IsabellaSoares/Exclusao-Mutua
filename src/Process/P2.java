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
public class P2 {
    public static void main(String[] args) {
        int serverPort = 6790;
        int[] connectionPorts = {6789, 6791};
        Process p = new Process(2, serverPort, connectionPorts);
        p.exec();
        //exec(pid do processo, porta do servidor, vetor de conexões)
    }
}
