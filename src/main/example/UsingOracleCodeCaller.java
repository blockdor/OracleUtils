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
		

	
}
