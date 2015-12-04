import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.*;

import static com.blocktopus.common.CollectionUtils.newSet;
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
import com.blocktopus.schema.DataCache;
import com.blocktopus.schema.RowDataExtractor;
import com.blocktopus.schema.SchemaBuilder;
import com.blocktopus.schema.data.RowData;
import com.blocktopus.schema.data.PrimaryKeyId;
import com.blocktopus.schema.model.Schema;

public class TestDataExtractor {
	RowDataExtractor testClass =null;
	SchemaBuilder sb = null;
	@Before
	public void setup() {
		OracleQuerier oq = new OracleQuerier();
		oq.setDataSource(DataSourceFactory.getDataSource());
		testClass = RowDataExtractor.getInstance();
		testClass.setOracleQuerier(oq);
		sb = new SchemaBuilder();
		sb.setOracleQuerier(oq);
	}

	@After
	public void tearDown() {
	}
	
	@Test
	public void test1(){

		Set<String> tables = newSet();
		tables.add("location");
		Schema s = sb.buildSchema("block",tables );
		List<RowData> data = testClass.getAllDataFromTable(s.getTable("location"));
		System.out.println(data.size());
		System.out.println(data.get(0).getFields());
		System.out.println(data.get(0).getDataByColumnName("somedate"));
		System.out.println(data.get(0).getDataByColumnName("somedate").getClass());

		System.out.println(data.get(0).getDataByColumnName("locationid"));
		System.out.println(data.get(0).getDataByColumnName("locationid").getClass());

		System.out.println(data.get(0).getDataByColumnName("name"));
		System.out.println(data.get(0).getDataByColumnName("name").getClass());
	
	}

	@Test
	public void test2(){

		Set<String> tables = newSet();
		tables.add("location");
		Schema s = sb.buildSchema("block",tables );
		PrimaryKeyId pk = new PrimaryKeyId(s.getTable("location").getPrimaryKey(), 1);
		RowData data = testClass.getDataFromTableByPK(pk);
		System.out.println(data.getFields());
	}

	@Test
	public void test3(){

		Set<String> tables = newSet();
		tables.add("residentlocation");
		Schema s = sb.buildSchema("block",tables );
		PrimaryKeyId pk = s.getTable("residentlocation").getPrimaryKey().createPrimaryKeyId(1,5);
		RowData data = testClass.getDataFromTableByPK(pk);
		System.out.println(data.getFields());
	}
	//44760

	@Test
	public void testCache(){

		Set<String> tables = newSet();
		tables.add("location");
		Schema s = sb.buildSchema("block",tables );
		PrimaryKeyId pk = s.getTable("location").getPrimaryKey().createPrimaryKeyId(1);
		RowData data = testClass.getDataFromTableByPK(pk);
		System.out.println(data.getFields());
		OracleQuerier oq = new OracleQuerier();
		testClass.setOracleQuerier(oq);
		data = testClass.getDataFromTableByPK(pk);
		System.out.println(data.getFields());
		
		
		
	}
	@Test
	public void testCache2(){

		Set<String> tables = newSet();
		tables.add("location");
		Schema s = sb.buildSchema("block",tables );
		PrimaryKeyId pk = s.getTable("location").getPrimaryKey().createPrimaryKeyId(1);
		PrimaryKeyId pk2 = s.getTable("location").getPrimaryKey().createPrimaryKeyId(2);
		PrimaryKeyId pk3 = s.getTable("location").getPrimaryKey().createPrimaryKeyId(3);
		testClass.getDataCache().setCacheSize(2);
		testClass.getDataFromTableByPK(pk);
		testClass.getDataFromTableByPK(pk2);
		testClass.getDataFromTableByPK(pk3);
		OracleQuerier oq = new OracleQuerier();
		testClass.setOracleQuerier(oq);
		RowData data = testClass.getDataFromTableByPK(pk2);
		System.out.println(data.getFields());
		data = testClass.getDataFromTableByPK(pk3);
		System.out.println(data.getFields());
		try{
			data = testClass.getDataFromTableByPK(pk);
		} catch (RuntimeException npe){
			return;
		}
		fail("Should throw an Exception");		
		
		
	}
}
