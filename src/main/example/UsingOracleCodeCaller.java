import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

import com.blocktopus.oracle.OracleCodeCaller;
import com.blocktopus.oracle.types.ObjectListLazy;
import com.blocktopus.oracle.types.ObjectList;
import com.blocktopus.oracle.types.PrimitiveOutputParameter;
import com.blocktopus.oracle.types.PrimitiveList;
import com.blocktopus.oracle.types.Response;


public class UsingOracleCodeCaller {

	/**
	 * create or replace type CUSTOM.o_historyObject as object (
	                                     historyObject2History number,
	                                     historyObject2DimensionObject number,
	                                     historyObject2Object number,
	                                     historyObject2Relation number,
	                                     objectname varchar2(32767),
	                                     event number,
	                                     description varchar2(32767))
	 */
		public class HistoryObject implements SQLData{

			private BigDecimal historyObject2History;
	        private BigDecimal historyObject2DimensionObject; 
	        private BigDecimal historyObject2Object;
	        private BigDecimal historyObject2Relation; 
	        private String objectname; 
	        private BigDecimal event; 
	        private String description;
			public BigDecimal getHistoryObject2History() {
				return historyObject2History;
			}
			public void setHistoryObject2History(BigDecimal historyObject2History) {
				this.historyObject2History = historyObject2History;
			}
			public BigDecimal getHistoryObject2DimensionObject() {
				return historyObject2DimensionObject;
			}
			public void setHistoryObject2DimensionObject(
					BigDecimal historyObject2DimensionObject) {
				this.historyObject2DimensionObject = historyObject2DimensionObject;
			}
			public BigDecimal getHistoryObject2Object() {
				return historyObject2Object;
			}
			public void setHistoryObject2Object(BigDecimal historyObject2Object) {
				this.historyObject2Object = historyObject2Object;
			}
			public BigDecimal getHistoryObject2Relation() {
				return historyObject2Relation;
			}
			public void setHistoryObject2Relation(BigDecimal historyObject2Relation) {
				this.historyObject2Relation = historyObject2Relation;
			}
			public String getObjectname() {
				return objectname;
			}
			public void setObjectname(String objectname) {
				this.objectname = objectname;
			}
			public BigDecimal getEvent() {
				return event;
			}
			public void setEvent(BigDecimal event) {
				this.event = event;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
			
			
			public String getSQLTypeName() throws SQLException {
				return "CUSTOM.O_HISTORYOBJECT";
			}
			
			public void readSQL(SQLInput stream, String typeName)
					throws SQLException {
				//ORDER IS IMPORTANT
				historyObject2History = stream.readBigDecimal();
				historyObject2DimensionObject = stream.readBigDecimal();
				historyObject2Object = stream.readBigDecimal();
				historyObject2Relation = stream.readBigDecimal();
				objectname = stream.readString();
				event = stream.readBigDecimal();
				description = stream.readString();
				
			}
			
			public void writeSQL(SQLOutput stream) throws SQLException {
				//ORDER IS IMPORTANT
				stream.writeBigDecimal(historyObject2History);
				stream.writeBigDecimal(historyObject2DimensionObject);
				stream.writeBigDecimal(historyObject2Object);
				stream.writeBigDecimal(historyObject2Relation);
				stream.writeString(objectname);
				stream.writeBigDecimal(event);
				stream.writeString(description);
			} 
		}
	
	/*
	 *   PROCEDURE createHistory(o_errorCode       OUT NUMBER, -- the errorCode <> 0 if an error occured within the procedure.
                      o_errorText       OUT VARCHAR2, -- description of the error that occured within the procedure.
                      o_historyID       OUT NUMBER, -- the history record that was created.
                      i_workorderIDs    IN t_number, -- the ARM workorders that are to be associated to the hisotory record.
                      i_historyobjects  IN t_historyobject)               
                      
                      t_number is table of number;
                      t_historyobject is table of o_historyobject;
	 */	
		
	/**
	 * this method uses {@link ObjectList} to model t_historyobject and {@Link PrimitiveList} to model t_number. 
	 * <P> As such a sqldata representation on o_historyobject is required. 
	 * @throws Exception
	 */
	public void callCreateHistory() throws Exception{

		
		Connection c = null;
		//get connection somehow
		OracleCodeCaller occ = new OracleCodeCaller();
		occ.setConnection(c);
		
		PrimitiveOutputParameter<BigDecimal> o_historyID = new PrimitiveOutputParameter<BigDecimal>(BigDecimal.class);
		PrimitiveList<Long> workorderIds = new PrimitiveList<Long>("CUSTOM.T_NUMBER");
		ObjectList<HistoryObject> historyObjects = new ObjectList<HistoryObject>("CUSTOM.T_HISTORYOBJECT");
		
		workorderIds.add(1l);
		workorderIds.add(2l);
		
		HistoryObject ho = new HistoryObject();
		ho.setHistoryObject2DimensionObject(BigDecimal.valueOf(1));
		ho.setHistoryObject2Object(BigDecimal.valueOf(1234));
		ho.setHistoryObject2Relation(BigDecimal.valueOf(1234));
		ho.setObjectname("Node1");
		ho.setEvent(BigDecimal.valueOf(1));
		ho.setDescription("SomeDescription");
		historyObjects.add(ho);
		
		Response r = occ.callStoredProcedure("CUSTOM.PKGHISTORY.CREATEHISTORY", OracleCodeCaller.ResponseType.ErrorCodeAndErrorText,
				o_historyID,
				workorderIds,
				historyObjects
				);
		r.getErrorcode();
		r.getErrortext();
		BigDecimal id = (BigDecimal)o_historyID.getParameter();
		try{
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * this method uses {@link ObjectListLazy} to model t_historyobject and {@link PrimitiveList} to model t_number. 
	 * <P> As such <b>no</b> sqldata representation on o_historyobject is required. 
	 * @throws Exception
	 */
	public void callCreateHistoryLazy() throws Exception{

		
		Connection c = null;
		//get connection somehow
		OracleCodeCaller occ = new OracleCodeCaller();
		occ.setConnection(c);
		
		PrimitiveOutputParameter<Long> o_historyID = new PrimitiveOutputParameter<Long>(Long.class);
		PrimitiveList<Long> workorderIds = new PrimitiveList<Long>("CUSTOM.T_NUMBER");
		ObjectListLazy historyObjects = new ObjectListLazy("CUSTOM.T_HISTORYOBJECT","CUSTOM.O_HISTORYOBJECT");
		
		workorderIds.add(1l);
		workorderIds.add(2l);
		
		// lazy so we use a map with names... obviously not as safe as using an sqldata implementation
		// as there is no compile time checking of types etc
		Map<String,Object> historyObject = new HashMap<String,Object>();
		historyObject.put("HistoryObject2DimensionObject", BigDecimal.valueOf(1));
		historyObject.put("HistoryObject2Object",BigDecimal.valueOf(1234));
		historyObject.put("HistoryObject2Relation", BigDecimal.valueOf(1234));
		historyObject.put("ObjectName", "Node1");
		historyObject.put("Description","SomeDescription");

		historyObjects.add(historyObject);
		
		Response r = occ.callStoredProcedure("CUSTOM.PKGHISTORY.CREATEHISTORY", OracleCodeCaller.ResponseType.ErrorCodeAndErrorText,
				o_historyID,
				workorderIds,
				historyObjects
				);
		r.getErrorcode();
		r.getErrortext();
		Long id = o_historyID.getParameter();
		try{
			c.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	
	
}
