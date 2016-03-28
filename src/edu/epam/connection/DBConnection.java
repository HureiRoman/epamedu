package edu.epam.connection;

import java.sql.Connection;


public class DBConnection {
   private Connection connection;
   private boolean free;
   
   public DBConnection( Connection connection){
	   this.connection = connection;
       free = true;
   }

   public Connection getConnection() {
       return connection;
   }

   public void setConnection(Connection connection) {
       this.connection = connection;
   }

   public boolean isFree() {
       return free;
   }

   public void setFree(boolean free) {
       this.free = free;
   }
 
}
