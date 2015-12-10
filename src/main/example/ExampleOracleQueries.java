import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.blocktopus.oracle.OracleQuerier;


public class ExampleOracleQueries {

	private DataSource getDataSource(){
		return DataSourceFactory.getDataSource();
	}
	
	public void setupQuerier() throws SQLException{
		//if you use a datasource as below a new connection will be retrieved for each query, easiest method.
		OracleQuerier oq = new OracleQuerier();		
		oq.setDataSource(getDataSource());
		
		//if you get a connection it will reuse that connection until it is closed
		// means you are in charge of connection management but can keep transactions going
		Connection c = getDataSource().getConnection();
		oq.setConnection(c);
		//do stuff
		oq.closeConnection(c);
	}
	
	public void queryingForASingleValue() throws SQLException{
		OracleQuerier oq = new OracleQuerier();		
		oq.setDataSource(getDataSource());
		
		//main method to get a single value returns object
		//BigDecimal for number
		//String for varchar/clob etc
		//java.sql.timestamp for date and timestamp (since java.sql.date doesn't match oracle dates)
		Object o = oq.queryForSingleValue("select avalue from atable where pkid=?", 1);
		
		//if you know what your value will be though use one of the easier methods
		Long l = oq.queryForLong("select alongvalue from atable where pkid=?", 1);
		
		//if you know what your value will be though use one of the easier methods
		String s = oq.queryForString("select aStringvalue from atable where pkid=?", 1);
		
		//etc
				
	}
	
	public void queryingForMultipleValues() throws SQLException{
		OracleQuerier oq = new OracleQuerier();		
		oq.setDataSource(getDataSource());
		
		//you can get rows which we model as maps of columnname:value
		//multiple rows are lists of maps
		
		//1 row
		Map<String,Object> row = oq.queryForMap("select * from atable where pkid =", 1);
		
		///many rows
		List<Map<String,Object>> rows = oq.queryForMapList("select * from atable");
		
	}
	
	
}
