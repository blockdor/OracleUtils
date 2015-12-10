import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.blocktopus.oracle.OracleQuerier;
import com.blocktopus.schema.SchemaBuilder;
import com.blocktopus.schema.model.Schema;
import com.blocktopus.schema.model.Table;

import static com.blocktopus.common.CollectionUtils.*;
public class TestSchemaBuilder {
	SchemaBuilder testClass =null;
	@Before
	public void setup() {
		OracleQuerier oq = new OracleQuerier();
		oq.setDataSource(DataSourceFactory.getDataSource());
		testClass = new SchemaBuilder();
		testClass.setOracleQuerier(oq);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testOneTable() throws Exception {
		Set<String> tables = newSet();
		tables.add("LOCATION");
		Schema s = testClass.buildSchema("block",tables );
		System.out.println(s.getTables());
		System.out.println(s.getTable("location").getPrimaryKey());
		System.out.println(s.getTable("location").getForeignKeys());
		System.out.println(s.getTable("location").getRefKeys());
	}

	@Test
	public void testDump() throws Exception {
		Set<String> tables = newSet();
		tables.add("location");
		tables.add("resident");
		tables.add("locationtype");
		tables.add("residentlocation");
		
		Schema s = testClass.buildSchema("block",tables );
		testClass.dumpSchemaToFile(s, new File("c:/schema.byte"));
	}
	@Test
	public void testLoad() throws Exception {
		Schema s = testClass.loadSchemaFromFile(new File("c:/schema.byte"));
		System.out.println(s.getTables());
		for(Table t:s.getTables()){
			System.out.println(t.getPrimaryKey());
			System.out.println(t.getForeignKeys());
			System.out.println(t.getRefKeys());
		}
	}
	

	@Test
	public void testPArentKeys() throws Exception {
		Set<String> tables = newSet();
		tables.add("location");
		Schema s = testClass.buildSchema("block",tables );
		for(Table t:s.getTables()){
			System.out.println(t.getPrimaryKey());
			System.out.println(t.getForeignKeys());
			System.out.println(t.getRefKeys());
		}
	}

}
