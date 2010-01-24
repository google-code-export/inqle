package org.inqle.data.rdf.jena.util;

import java.sql.*;


 /**

 *  A concrete implementation of the ConnectionValidator that executes a simple

 *  SQL query against a connection to be checked.

 *

 *  @author  Serguei Eremenko

 *  @version 1.0

 */

 public class SqlConnectionValidator {
   /**
   *  @param   sql the SQL query that is used to check a connection validity,
   *           for example "select * from Dual"
   */
   public SqlConnectionValidator(String sql){
      if ("".equals(sql))
         throw new IllegalArgumentException("Wrong SQL query: "+sql);
      this.sql = sql;
   }
   /**
   *  Checks the connection; it does not check whether an exeption resulted
   *  in connection's or query's errors.
   *  @param   conn the connection to be checked
   */
   public void validateConnection(Connection conn) throws SQLException{
      PreparedStatement ps = null;
      try{
         ps = conn.prepareStatement(sql);
         ps.execute();
         // it should filter out connection's exceptions from others to rise
         // only these of connection related.
      }finally{
         if (ps != null) {
            try{ ps.close();}catch (SQLException e){}
         }
      }
   }

   public String toString(){ return "["+sql+"]";}

   private String sql;

 }

