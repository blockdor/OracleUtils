

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;


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
import com.blocktopus.oracle.OracleParallelExecute;
import com.blocktopus.oracle.OracleParallelExecute.ParallelTask;
import com.blocktopus.oracle.OracleParallelExecute.ParallelTask.LongIDChunker;


//@Category(DBTest.class)
public class TestOracleParallelExecute {

	OracleParallelExecute testClass = null;

	@Before
	public void setup() throws SQLException {
		testClass = new OracleParallelExecute();
		OracleCodeCaller occ = new OracleCodeCaller();
		DataSource d = new SimpleOracleDataSource("datamigration", "datamigration", "192.168.91.128","rm810b");
		occ.setDataSource(d);
		//occ.setAutoCommit(true);
		testClass.setOracleCodeCaller(occ);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test1() throws Exception {
		LongIDChunker lic = new LongIDChunker();
		lic.addChunk(1l, 1000l);
		lic.addChunk(1001l, 2000l);
		System.out.println(lic.getChunkSQL());
		ParallelTask pt = new ParallelTask("begin for i in ? .. ? loop "
				+ "  insert into test values(i,SYS_CONTEXT('USERENV','SESSIONID')); "
				+ " end loop; " + " commit; " + " end;",lic);
	}

}
