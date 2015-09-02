import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.List;

import com.blocktopus.oracle.OracleCodeCaller;
import com.blocktopus.oracle.types.PrimitiveListOutputParameter;
import com.blocktopus.oracle.types.ObjectListOutputParameter;


public class ComplicatedOutputParams {

    public class IDPair implements java.sql.SQLData{
          
          private BigDecimal id1;
          private BigDecimal id2;

          public BigDecimal getId1() {
                 return id1;
          }

          public void setId1(BigDecimal id1) {
                 this.id1 = id1;
          }

          public BigDecimal getId2() {
                 return id2;
          }

          public void setId2(BigDecimal id2) {
                 this.id2 = id2;
          }

          
          public String getSQLTypeName() throws SQLException {
                 return "CRAMER.O_ID_PAIR";
          }

          
          public void readSQL(SQLInput stream, String typeName)
                        throws SQLException {
                 id1 = stream.readBigDecimal();
                 id2 = stream.readBigDecimal();
          }

          
          public void writeSQL(SQLOutput stream) throws SQLException {
                 // TODO Auto-generated method stub
                 stream.writeBigDecimal(id1);
                 stream.writeBigDecimal(id2);
          }
          
    }
    
    public void call() throws Exception{

    	Connection c = null;
    	//get connection somehow
        OracleCodeCaller occ = new OracleCodeCaller();
        occ.setConnection(c);
        
        //for nested tables of primitives we use ArrayOutputParameter and give the typename
          PrimitiveListOutputParameter<Long> addr = new PrimitiveListOutputParameter<Long>("HSI.T_NUMBER",Long.class);
          //note we can omit the schema if we have synonyms etc in place or are logged into the schema we want!
          PrimitiveListOutputParameter<Long> sa = new PrimitiveListOutputParameter<Long>("T_NUMBER",Long.class);
          
          //set up the object output param, we need to know:
          //the typename of the nested table
          //the typename of the object in the table
          //the sqldata class that maps to the object
          
          ObjectListOutputParameter<IDPair> rfb = new ObjectListOutputParameter<IDPair>("CRAMER.T_ID_PAIRS","CRAMER.O_ID_PAIR",IDPair.class);
          
          //input params are just normal objects
          String currentReturnSegment = null;
          String plannedReturnSegment = null;
          Long segmentationType = null;

          occ.callStoredProcedure("HSI.PKGSEGMENTATION.GETOBJECTSFORSEGMENTATION", sa,addr,rfb,currentReturnSegment,plannedReturnSegment,segmentationType);
          
          //how to get the params out!
          List<Long> serviceAreas = sa.getParameter();
          Long serviceArea = serviceAreas.get(0);
          List<Long> Addresses = addr.getParameter();
          Long address = Addresses.get(0);
          
          List<IDPair> rfbearers = rfb.getParameter();
          IDPair currentAndPlannedRfbearers = rfbearers.get(0);
  		try{
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
    }

}
