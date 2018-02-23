/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.util.hibernateutil;

import fe.model.dao.ConfigDAO;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class TestInitializer {
    
    public static void main(String[] args) {
        ConfigDAO dao= new ConfigDAO();
        dao.BuscarConfigId(1);
    }
}
