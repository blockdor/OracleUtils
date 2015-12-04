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
import com.blocktopus.schema.ForeignKeyNavigator;
import com.blocktopus.schema.RowDataExtractor;
import com.blocktopus.schema.SchemaBuilder;
import com.blocktopus.schema.data.RowData;
import com.blocktopus.schema.data.PrimaryKeyId;
import com.blocktopus.schema.model.ForeignKey;
import com.blocktopus.schema.model.Schema;
import com.blocktopus.schema.model.Table;

public class TestForeignKeyNavigator {
	ForeignKeyNavigator testClass =null;
	SchemaBuilder sb = null;
	RowDataExtractor rde = null;
	@Before
	public void setup() {
		OracleQuerier oq = new OracleQuerier();
		oq.setDataSource(DataSourceFactory.getDataSource());
		testClass = ForeignKeyNavigator.getInstance();
		testClass.setOracleQuerier(oq);
		sb = new SchemaBuilder();
		sb.setOracleQuerier(oq);
		rde = RowDataExtractor.getInstance();
		rde.setOracleQuerier(oq);
	}

	@After
	public void tearDown() {
	}
	@Test
	public void testFK1(){
		Set<String> tables = newSet();
		tables.add("locationtype");
		tables.add("location");
		Schema s = sb.buildSchema("block",tables );
		PrimaryKeyId pk = s.getTable("location").getPrimaryKey().createPrimaryKeyId(1);
		RowData rd = rde.getDataFromTableByPK(pk);
		ForeignKey fk = s.getTable("location").getFK("LOC_LT_FK");
		PrimaryKeyId pkid = testClass.navigateToRefTable(rd, fk);
		System.out.println(rd.getFields());
		System.out.println(pkid.getId());
	}
	@Test
	public void testFK2(){
		Set<String> tables = newSet();
		tables.add("location");
		Schema s = sb.buildSchema("block",tables );
		PrimaryKeyId pk = s.getTable("location").getPrimaryKey().createPrimaryKeyId(1);
		RowData rd = rde.getDataFromTableByPK(pk);
		ForeignKey fk = s.getTable("location").getFK("LOC_LT_FK");
		try{
			testClass.navigateToRefTable(rd, fk);
		} catch (RuntimeException re){
			return;
		}
		Assert.fail("Should throw Exception here");
	}
	@Test
	public void testRefKey1(){
		Set<String> tables = newSet();
		tables.add("location");
		tables.add("locationtype");
		Schema s = sb.buildSchema("block",tables );
		PrimaryKeyId pk = s.getTable("locationtype").getPrimaryKey().createPrimaryKeyId(1);
		RowData rd = rde.getDataFromTableByPK(pk);
		ForeignKey fk = s.getTable("locationtype").getRefKey("LOC_LT_FK");
		Set<PrimaryKeyId> pkids = testClass.navigateToFKTable(rd, fk);
		System.out.println(rd.getFields());
		
		for(PrimaryKeyId pkid:pkids){
			RowData rd2 = rde.getDataFromTableByPK(pkid);
			System.out.println(rd2.getFields());
		}
		
		
	}
	@Test
	public void testRefKey2(){
		Set<String> tables = newSet();
		tables.add("locationtype");
		Schema s = sb.buildSchema("block",tables );
		PrimaryKeyId pk = s.getTable("locationtype").getPrimaryKey().createPrimaryKeyId(1);
		RowData rd = rde.getDataFromTableByPK(pk);
		ForeignKey fk = s.getTable("locationtype").getRefKey("LOC_LT_FK");
		try{
			testClass.navigateToFKTable(rd, fk);
		} catch (RuntimeException re){
			return;
		}
		Assert.fail("Should throw Exception here");
	}
}
