package edu.epam.connection;

import java.sql.Connection;

import org.apache.log4j.Logger;

import edu.epam.exceptions.NoDBConnectionsLongTime;


public class ConnectionManager {
    private static ConnectionManager instance;
    private ConnectionPool connectionPool;
    private static final int CONNECTIONS_NUMBER = 10;
    
    static final Logger LOGGER = Logger.getLogger(ConnectionManager.class);
    
    public ConnectionManager(){
    	LOGGER.info("Creating ConnectionPool");
        connectionPool = new ConnectionPool(CONNECTIONS_NUMBER);
    }
    
    public Connection getConnection() throws NoDBConnectionsLongTime{
    	LOGGER.info("get connection");
        Connection cn = connectionPool.getConnection();
        final int sleepTime = 500;//miliseconds
        final int maxWaitTime = 10000;//miliseconds
        for(int i = 1; i*sleepTime < maxWaitTime && cn == null; i++){
            try {
            	LOGGER.info("waiting for connection , time = " + i + "millsec.");
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
            	LOGGER.error("Interapted : " +ex.getMessage());
            }
            cn = connectionPool.getConnection();
        }
        
        if(cn == null){
        	throw new NoDBConnectionsLongTime();
        }
        return cn;
    }
    
    public void freeConnection(Connection connection){
    	LOGGER.info("Free connection");
        connectionPool.free(connection);
    }
    
    public static ConnectionManager getInstance(){
        if(instance == null){
        	LOGGER.info("Creates Connection Manager");
            instance = new ConnectionManager();
        }
        LOGGER.info("return ready instance of ConnectionManager");
        return instance;
    }
}

