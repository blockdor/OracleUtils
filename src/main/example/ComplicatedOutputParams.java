import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.List;

import com.blocktopus.oracle.OracleExecutor;
import com.blocktopus.oracle.ParameterFactory;
import com.blocktopus.oracle.types.ObjectOutputParameter;
import com.blocktopus.oracle.types.PrimitiveListOutputParameter;
import com.blocktopus.oracle.types.ObjectListOutputParameter;

public class ComplicatedOutputParams {

	public void call() throws Exception {

		Connection c = null;
		// get connection somehow
		OracleExecutor occ = new OracleExecutor();
		occ.setConnection(c);

		// for nested tables of primitives we use PrimitiveListOutputParameter
		// and give
		// the typename
		PrimitiveListOutputParameter<Long> out1 = ParameterFactory
				.getListOfLongOutputParameter("BLOCK.T_NUMBER");
		// note we can omit the schema if we have synonyms etc in place or are
		// logged into the schema we want!
		PrimitiveListOutputParameter<Double> out2 = ParameterFactory
				.getListOfDoubleOutputParameter("T_NUMBER");

		// for object output param we need to know
		// the typename of the object
		// the sqldata class that maps to the object
		ObjectOutputParameter<OObject> out3 = ParameterFactory
				.getObjectOutputParameter("BLOCK.O_OBJECT", OObject.class);

		// For nested table of object we also need the table sql typename
		ObjectListOutputParameter<OObject> out4 = ParameterFactory
				.getObjectListOutputParameter("BLOCK.T_OBJECT",
						"BLOCK.O_OBJECT", OObject.class);

		Long in1 = 10l;
		String in2 = "Something";
		occ.callStoredProcedure("BLOCK.PKGEXAMPLE.COMPLICATEDOUTPUTS", out1,
				out2, out3, out4, in1, in2);

		// how to get the params out!
		List<Long> longOutputs = out1.getParameter();
		Long longOutput = longOutputs.get(0);
		List<Double> doubleOutputs = out2.getParameter();
		Double doubleOutput = doubleOutputs.get(0);

		OObject objectOutput = out3.getParameter();
		
		List<OObject> objects = out4.getParameter();
		OObject objectOutput2 = objects.get(0);
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
