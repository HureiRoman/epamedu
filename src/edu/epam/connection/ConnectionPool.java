package edu.epam.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.epam.manager.ConfigurationManager;

public class ConnectionPool {
    private ArrayList<DBConnection> connections;
    private int capacity;
    
    static final Logger LOGGER = Logger.getLogger(ConnectionPool.class);

    
    public ConnectionPool(int capacity){
    	LOGGER.info("Creating ConnectionPool with capacity " + capacity);
        this.capacity = capacity;
        connections = new ArrayList<DBConnection>();
    }
    
    public synchronized Connection getConnection(){
    	 System.out.println("Connection POOL (opened connections ) = " + connections.size());
        for(DBConnection connection : connections){
            if(connection.isFree()){
                connection.setFree(false);
                LOGGER.info("Set free False, and get Connection");
                return connection.getConnection();
            }
        }
        if(connections.size() < capacity){
            DBConnection cn = new DBConnection(createConnection());
            connections.add(cn);
            LOGGER.info("creates DBConnection and return it ");
            return cn.getConnection();
        }
    
        //������ ������� 
        return null;
    }
    
    private synchronized Connection createConnection(){
        Connection cn = null;
        try {
            Class.forName(ConfigurationManager.getInstance().getProperty(ConfigurationManager.DATABASE_DRIVER_NAME));
            cn = DriverManager.getConnection(ConfigurationManager.getInstance().getProperty(ConfigurationManager.DATABASE_URL));
        } catch (SQLException ex) {
        	 LOGGER.error("SQLEXception = " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
        	 LOGGER.error("ClassNotFound exception = " + ex.getMessage());
        }
        return cn;
    }
    
    public void free(Connection connection){
        for(DBConnection connectionElement : connections){
            if(connectionElement.getConnection().equals(connection)){
            	 LOGGER.info(connectionElement + " set free = true");
                connectionElement.setFree(true);
            }
        }
    }
}

