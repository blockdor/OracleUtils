

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Types;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.junit.*;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.*;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ SAMClientImpl.class, SAMServiceLogger.class })
import org.junit.experimental.categories.Category;

import static com.blocktopus.common.CollectionUtils.*;

import com.blocktopus.oracle.OracleExecutor;
import com.blocktopus.oracle.ParameterFactory;
import com.blocktopus.oracle.types.OutputParameter;
import com.blocktopus.oracle.types.ObjectListLazy;
import com.blocktopus.oracle.types.ObjectList;
import com.blocktopus.oracle.types.PrimitiveOutputParameter;
import com.blocktopus.oracle.types.PrimitiveList;
import com.blocktopus.oracle.types.ObjectOutputParameter;

//@Category(DBTest.class)
public class TestOracleCodeCaller {

	
	
	OracleExecutor testClass = null;

	@Before
	public void setup() {
		testClass = new OracleExecutor();
		testClass.setDataSource(DataSourceFactory.getDataSource());
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testPrimitive() throws Exception {
		List<OutputParameter> r = testClass.callStoredProcedure("sptest.testPrimitives",
				ParameterFactory.getLongOutputParameter(),
				ParameterFactory.getVarcharOutputParameter(),
				new Long(1),
				"blah blah",
				new java.util.Date(System.currentTimeMillis()));
		System.out.println(r.get(0));
		System.out.println(r.get(1));
	}

	@Test
	public void testObjects() throws Exception {
		List<OutputParameter> r = testClass.callStoredProcedure("sptest.testObjects",
				ParameterFactory.getLongOutputParameter(),
				ParameterFactory.getVarcharOutputParameter(),
				new OObject("name", 1d, new Date(1, 2, 3)));
		System.out.println(r.get(0));
		System.out.println(r.get(1));
	}

	@Test
	public void testNumberList() throws Exception {
		PrimitiveList<Long> numberArray = new PrimitiveList<Long>("CUSTOM.T_NUMBER");
		numberArray.add(new Long(1));
		numberArray.add(new Long(2));

		List<OutputParameter> r = testClass.callStoredProcedure("sptest.testNumberArray", 
				ParameterFactory.getLongOutputParameter(),
				ParameterFactory.getVarcharOutputParameter(),
				numberArray);
		System.out.println(r.get(0));
		System.out.println(r.get(1));
	}

	@Test
	public void testStringList() throws Exception {
		PrimitiveList<String> stringArray = new PrimitiveList<String>("CUSTOM.T_STRING");
		stringArray.add("abc");
		stringArray.add("abc2");

		List<OutputParameter> r = testClass.callStoredProcedure("sptest.testStringArray",			
				ParameterFactory.getLongOutputParameter(),
				ParameterFactory.getVarcharOutputParameter(),
				stringArray);
		System.out.println(r.get(0));
		System.out.println(r.get(1));
	}

	@Test
	public void testObjectList() throws Exception {
		ObjectList<OObject> objArray = new ObjectList<OObject>("T_OBJECT");

		objArray.add(new OObject("name", 1d, new Date(1,2,3)));
		objArray.add(new OObject("name2", 2d, new Date(2,2,3)));
		objArray.add(new OObject("name3", 3d, new Date(3,2,3)));


		List<OutputParameter> r = testClass.callStoredProcedure("sptest.testObjectArray", 
				ParameterFactory.getLongOutputParameter(),
				ParameterFactory.getVarcharOutputParameter(),
				objArray);
		System.out.println(r.get(0));
		System.out.println(r.get(1));
	}

	@Test
	public void testLazyObjectList() throws Exception {
		ObjectListLazy objArray = new ObjectListLazy("BLOCK.T_OBJECT","BLOCK.O_OBJECT");

		Map<String,Object> object = newMap();
		object.put("ONAME","name");
		object.put("OTYPE",1);
		object.put("ODATE",new Date(1,2,3));
		
		Map<String,Object> object2 = newMap();
		object.put("ONAME","name2");
		object.put("OTYPE",2);
		object.put("ODATE",new Date(2,2,3));
		Map<String,Object> object3 = newMap();
		object.put("ONAME","name3");
		object.put("OTYPE",3);
		object.put("ODATE",new Date(3,2,3));

		objArray.add(object);
		objArray.add(object2);
		objArray.add(object3);
		/*OracleConnection oc = (OracleConnection)testClass.getConnection();
		StructDescriptor sd = StructDescriptor.createDescriptor("O_OBJECT", oc);
		STRUCT s = new STRUCT(sd,oc,object);*/
		
		
		List<OutputParameter> r = testClass.callStoredProcedure("sptest.testObjectArray", 
				ParameterFactory.getLongOutputParameter(),
				ParameterFactory.getVarcharOutputParameter(),
				objArray);
		System.out.println(r.get(0));
		System.out.println(r.get(1));
	}
	@Test
	public void testOddParams() throws Exception {

		Long in_long = 10l;
		String in_string = "abc";
		PrimitiveOutputParameter<Long> out_long = new PrimitiveOutputParameter<Long>(Long.class);
		
		List<OutputParameter> r = testClass.callStoredProcedure("sptest.testOddParams", 
				ParameterFactory.getLongOutputParameter(),
				ParameterFactory.getVarcharOutputParameter(),
				in_long,out_long,in_string);
		System.out.println(r.get(0));
		System.out.println(r.get(1));
		System.out.println(out_long.getParameter().getClass());
		System.out.println(out_long.getParameter());
	}

	@Test
	public void testsqldataOutput() throws Exception {

		ObjectOutputParameter<OObject> out_obj = new ObjectOutputParameter<OObject>("BLOCK.O_OBJECT", OObject.class);
		
		List<OutputParameter> r = testClass.callStoredProcedure("sptest.testoutputobject",
				ParameterFactory.getLongOutputParameter(),
				ParameterFactory.getVarcharOutputParameter(),
				out_obj);
		System.out.println(r.get(0));
		System.out.println(r.get(1));
		OObject pp = out_obj.getParameter();
		System.out.println(out_obj.getParameter().getClass());
		System.out.println(out_obj.getParameter());
	}
	
/*
	
	@Test
	public void testOutputArray() throws Exception {

		PrimitiveListOutputParameter<Long> out_obj = new PrimitiveListOutputParameter<Long>("CUSTOM.T_NUMBER",Long.class);
		
		Response r = testClass.callStoredProcedure("sptest.testoutputArray", ResponseType.ErrorCodeAndErrorText,
				out_obj);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
		System.out.println(out_obj.getParameter().getClass());
		System.out.println(out_obj.getParameter());
		System.out.println(((List)out_obj.getParameter()).get(0).getClass());
		System.out.println(((List)out_obj.getParameter()).get(0));
	}
	@Test
	public void testOutputArrayDate() throws Exception {

		PrimitiveListOutputParameter<Date> out_obj = new PrimitiveListOutputParameter<Date>("CUSTOM.T_DATE",Date.class);
		
		Response r = testClass.callStoredProcedure("sptest.testoutputArrayDate", ResponseType.ErrorCodeAndErrorText,
				out_obj);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
		System.out.println(out_obj.getParameter().getClass());
		System.out.println(out_obj.getParameter());
		System.out.println(((List)out_obj.getParameter()).get(0).getClass());
		System.out.println(((List)out_obj.getParameter()).get(0));
	}
	
	
	@Test
	public void testOutputObjectArray() throws Exception {

		ObjectListOutputParameter<PportType> out_obj = new ObjectListOutputParameter<PportType>("HSI.T_PPORTTYPE","HSI.O_PPORTTYPE",PportType.class);
		
		Response r = testClass.callStoredProcedure("sptest.testoutputObjectArray", ResponseType.ErrorCodeAndErrorText,
				out_obj);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
		System.out.println(out_obj.getParameter().getClass());
		System.out.println(out_obj.getParameter());
		System.out.println(out_obj.getParameter().get(0).getClass());
		System.out.println(out_obj.getParameter().get(0));
	}
	
	/*@Test
	public void testFunction() throws Exception{
		PrimitiveOutputParameter<String> functionOutput = new PrimitiveOutputParameter<String>(String.class);
		Response r = testClass.callOracleFunction("CUSTOM.PKGHSICUSTOMNAMING.GENERATENODENAME",
				functionOutput, OracleCodeCaller.ResponseType.ErrorCodeAndErrorText,
				1901000006,18434l,null);
		System.out.println(functionOutput.getParameter());
		System.out.println(r.getErrorcode());
	}*/
}
