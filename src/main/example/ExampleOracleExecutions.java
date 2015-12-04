import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.blocktopus.oracle.OracleExecutor;
import com.blocktopus.oracle.ParameterFactory;
import com.blocktopus.oracle.types.ObjectList;
import com.blocktopus.oracle.types.OutputParameter;
import com.blocktopus.oracle.types.PrimitiveList;


public class ExampleOracleExecutions {

	private DataSource getDataSource(){
		return DataSourceFactory.getDataSource();
	}
	
	public void setupExecutor() throws SQLException{
		//if you use a datasource as below a new connection will be retrieved for each query, easiest method.
		OracleExecutor oe = new OracleExecutor();		
		oe.setDataSource(getDataSource());
		
		//if you get a connection it will reuse that connection until it is closed
		// means you are in charge of connection management but can keep transactions going
		Connection c = getDataSource().getConnection();
		oe.setConnection(c);
		//do stuff
		oe.closeConnection(c);
	}
	
	public void callProcedure(){
		OracleExecutor oe = new OracleExecutor();		
		oe.setDataSource(getDataSource());
		List<OutputParameter> out = oe.callStoredProcedure("block.pkgexample.anexmapleprocedure", ParameterFactory.getLongOutputParameter(), "bob" , 1);
		//out will conatin the long output parameter in index 0
		Long output = (Long)out.get(0).getParameter();
		System.out.println(output);
	}
	public void callFunction(){
		OracleExecutor oe = new OracleExecutor();		
		oe.setDataSource(getDataSource());
		List<OutputParameter> out = oe.callFunction("block.pkgexample.anexamplefunction", ParameterFactory.getVarcharOutputParameter(),ParameterFactory.getLongOutputParameter(), "bob" , 1);
		//out will conatain the function output in index 0 and the long output parameter in index 1
		String output = (String)out.get(0).getParameter();
		Long output2 = (Long)out.get(0).getParameter();
		System.out.println(output);
		System.out.println(output2);
	}
	public void runDML(){
		OracleExecutor oe = new OracleExecutor();		
		oe.setDataSource(getDataSource());
		oe.executeDML("insert into table values(?,?,?)", 1,2,3);
	}
	
	public void runForallDML(){
		OracleExecutor oe = new OracleExecutor();		
		oe.setDataSource(getDataSource());
		PrimitiveList<Long> in1 = new PrimitiveList<Long>("block.t_number");
		PrimitiveList<Long> in2 = new PrimitiveList<Long>("block.t_number");
		PrimitiveList<String> in3 = new PrimitiveList<String>("block.t_string");
		
		//all lists must be of same size, in this case 3
		//usually you'd be looping through objects and assigning each attribute to a different list
		in1.add(1l);
		in1.add(2l);
		in1.add(3l);
		
		in2.add(4l);
		in2.add(5l);
		in2.add(6l);
		
		in3.add("first");
		in3.add("second");
		in3.add("third");
		
		oe.executeForAll("insert into table values(?,?,?)", in1,in2,in3);

	}
	
	public void passingInObjects(){
		OracleExecutor oe = new OracleExecutor();		
		oe.setDataSource(getDataSource());

		OObject oo = new OObject();
		oo.setaName("bob");
		oo.setaNumber( 1d);
		oo.setaDate(new Date(1,2,3));
		
		//simply make an sqldata object and pass it in!
		oe.callStoredProcedure("block.pkgexample.objectprocedure", oo);
	}
	
	public void passingInListsOfPrimitives(){
		OracleExecutor oe = new OracleExecutor();		
		oe.setDataSource(getDataSource());
		
		//create a primitive list with the sql type of the nested list
		PrimitiveList<Long> pl = new PrimitiveList<Long>("block.t_number");
		
		pl.add(1l);
		pl.add(2l);
		
		//pass in
		oe.callStoredProcedure("block.pkgexample.objectlistprocedure", pl);
	}
	public void passingInListsOfObjects(){
		OracleExecutor oe = new OracleExecutor();		
		oe.setDataSource(getDataSource());

		//need an sqldata for the object type
		OObject oo = new OObject();
		oo.setaName("bob");
		oo.setaNumber( 1d);
		oo.setaDate(new Date(1,2,3));
		
		//create an object list with the sql type of the nested list
		ObjectList<OObject> ol = new ObjectList<OObject>("block.t_object");
		
		//pass in
		oe.callStoredProcedure("block.pkgexample.objectlistprocedure", ol);
	}
}
