import javax.sql.DataSource;


public class DataSourceFactory {
	public static DataSource getDataSource(){
		return new SimpleOracleDataSource("block", "block", "192.168.0.50","rm810b");
	}
}
