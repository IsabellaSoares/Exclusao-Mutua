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
 * @author Isabella
 */
public class Resource {
    private int id; //Identificador do recurso
    private List<Integer> processList;
    
    public Resource () {
        processList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void putProcess (int p) {
        processList.add(p);
    }
    
    public void removeProcess () {
        processList.remove(0);
    }
}
