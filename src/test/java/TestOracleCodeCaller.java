

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Types;
import java.util.Date;
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
import com.blocktopus.oracle.OracleCodeCaller;
import com.blocktopus.oracle.types.PrimitiveListOutputParameter;
import com.blocktopus.oracle.types.ObjectListLazy;
import com.blocktopus.oracle.types.ObjectList;
import com.blocktopus.oracle.types.PrimitiveOutputParameter;
import com.blocktopus.oracle.types.PrimitiveList;
import com.blocktopus.oracle.types.ObjectListOutputParameter;
import com.blocktopus.oracle.types.ObjectOutputParameter;

//@Category(DBTest.class)
public class TestOracleCodeCaller {
/*
	OracleCodeCaller testClass = null;

	@Before
	public void setup() {
		testClass = new OracleCodeCaller();
		testClass.setDataSource(new SimpleOracleDataSource("hsi", "HSI", "192.168.91.128","rm810b"));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testPrimitive() throws Exception {
		Response r = testClass.callStoredProcedure("sptest.testPrimitives", ResponseType.ErrorCodeAndErrorText,
				new Long(1),
				"blah blah",
				new java.util.Date(System.currentTimeMillis()));
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
	}

	@Test
	public void testObjects() throws Exception {
		Response r = testClass.callStoredProcedure("sptest.testObjects", ResponseType.ErrorCodeAndErrorText,
				new PportType("port_name", "port_type", "mac_domain_name"));
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
	}

	@Test
	public void testNumberList() throws Exception {
		PrimitiveList<Long> numberArray = new PrimitiveList<Long>("CUSTOM.T_NUMBER");
		numberArray.add(new Long(1));
		numberArray.add(new Long(2));

		Response r = testClass.callStoredProcedure("sptest.testNumberArray", ResponseType.ErrorCodeAndErrorText,
				numberArray);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
	}

	@Test
	public void testStringList() throws Exception {
		PrimitiveList<String> stringArray = new PrimitiveList<String>("CUSTOM.T_STRING");
		stringArray.add("abc");
		stringArray.add("abc2");

		Response r = testClass.callStoredProcedure("sptest.testStringArray", ResponseType.ErrorCodeAndErrorText,
				stringArray);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
	}

	@Test
	public void testObjectList() throws Exception {
		ObjectList<PportType> objArray = new ObjectList<PportType>("T_PPORTTYPE");

		objArray.add(new PportType("port_name", "port_type", "mac_domain_name"));
		objArray.add(new PportType("port_name2", "port_type", "mac_domain_name"));
		objArray.add(new PportType("port_name3", "port_type", "mac_domain_name"));


		Response r = testClass.callStoredProcedure("sptest.testObjectArray", ResponseType.ErrorCodeAndErrorText,
				objArray);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
	}

	@Test
	public void testLazyObjectList() throws Exception {
		ObjectListLazy objArray = new ObjectListLazy("HSI.T_PPORTTYPE","HSI.O_PPORTTYPE");

		Map<String,Object> object = newMap();
		object.put("PORTNAME","portname");
		object.put("PORTTYPE","porttype");
		object.put("MACDOMAINNAME","macdomainname");
		
		Map<String,Object> object2 = newMap();
		object2.put("portname","portname2");
		object2.put("porttype","porttype2");
		object2.put("macdomainname","macdomainname2");
		Map<String,Object> object3 = newMap();
		object3.put("portname","portname3");
		object3.put("porttype","porttype3");
		object3.put("macdomainname","macdomainname3");

		objArray.add(object);
		objArray.add(object2);
		objArray.add(object3);
		OracleConnection oc = (OracleConnection)testClass.getConnection();
		StructDescriptor sd = StructDescriptor.createDescriptor("O_PPORTTYPE", oc);
		STRUCT s = new STRUCT(sd,oc,object);
		
		
		Response r = testClass.callStoredProcedure("sptest.testObjectArray", ResponseType.ErrorCodeAndErrorText,
				objArray);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
	}
	@Test
	public void testOddParams() throws Exception {

		Long in_long = 10l;
		String in_string = "abc";
		PrimitiveOutputParameter<Long> out_long = new PrimitiveOutputParameter<Long>(Long.class);
		
		Response r = testClass.callStoredProcedure("sptest.testOddParams", ResponseType.ErrorCodeAndErrorText,
				in_long,out_long,in_string);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
		System.out.println(out_long.getParameter().getClass());
		System.out.println(out_long.getParameter());
	}

	@Test
	public void testsqldataOutput() throws Exception {

		ObjectOutputParameter<PportType> out_obj = new ObjectOutputParameter<PportType>("HSI.O_PPORTTYPE", PportType.class);
		
		Response r = testClass.callStoredProcedure("sptest.testoutputobject", ResponseType.ErrorCodeAndErrorText,
				out_obj);
		System.out.println(r.getErrorcode());
		System.out.println(r.getErrortext());
		PportType pp = out_obj.getParameter();
		System.out.println(out_obj.getParameter().getClass());
		System.out.println(out_obj.getParameter());
	}
	
	class IDPair implements java.sql.SQLData{
		
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
			// TODO Auto-generated method stub
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
	/*public void call() throws Exception{

		PrimitiveListOutputParameter addr = new PrimitiveListOutputParameter("T_NUMBER");
		PrimitiveListOutputParameter sa = new PrimitiveListOutputParameter("T_NUMBER");
		ObjectListOutputParameter rfb = new ObjectListOutputParameter("CRAMER.T_ID_PAIRS","CRAMER.O_ID_PAIR",IDPair.class);
		
		String currentReturnSegment = null;
		String plannedReturnSegment = null;
		Long segmentationType = null;
		OracleCodeCaller occ = new OracleCodeCaller();
		Response r = occ.callStoredProcedure("HSI.PKGSEGMENTATION.GETOBJECTSFORSEGMENTATION", OracleCodeCaller.ResponseType.ErrorCodeAndErrorText,
				sa,addr,rfb,currentReturnSegment,plannedReturnSegment,segmentationType);
		
		List serviceAreas = (List)sa.getParameter();
		BigDecimal serviceArea = (BigDecimal)serviceAreas.get(0);
		List Addresses = (List) addr.getParameter();
		BigDecimal address = (BigDecimal)Addresses.get(0);
		
		List rfbearers = (List) rfb.getParameter();
		IDPair currentAndPlannedRfbearers = (IDPair)rfbearers.get(0);
	}
	
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
