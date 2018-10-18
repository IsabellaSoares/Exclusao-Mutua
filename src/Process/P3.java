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
public class P3 {
    public static void main(String[] args) {
        int serverPort = 6791;
        int[] connectionPorts = {6789, 6790};
        Process p = new Process();
        p.exec(3, serverPort, connectionPorts);
        //exec(pid do processo, porta do servidor, vetor de conexões)
    }
}
