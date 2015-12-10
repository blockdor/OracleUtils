import com.blocktopus.oracle.OracleQuerier;

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

import com.blocktopus.oracle.OracleExecutor;
import com.blocktopus.oracle.types.PrimitiveListOutputParameter;
import com.blocktopus.oracle.types.ObjectListLazy;
import com.blocktopus.oracle.types.ObjectList;
import com.blocktopus.oracle.types.PrimitiveOutputParameter;
import com.blocktopus.oracle.types.PrimitiveList;
import com.blocktopus.oracle.types.ObjectListOutputParameter;
import com.blocktopus.oracle.types.ObjectOutputParameter;


public class TestOracleQuerier {

	OracleQuerier testClass =null;
	@Before
	public void setup() {
		testClass = new OracleQuerier();
		testClass.setDataSource(DataSourceFactory.getDataSource());
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testStar() throws Exception {
		List<Map<String,Object>> result = testClass.queryForMapList("select * from location where rownum=1");
		System.out.println(result);
	}
}
