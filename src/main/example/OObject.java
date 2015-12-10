import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class OObject implements SQLData, Serializable {
	public String getaName() {
		return aName;
	}

	public void setaName(String aName) {
		this.aName = aName;
	}

	public Double getaNumber() {
		return aNumber;
	}

	public void setaNumber(Double aNumber) {
		this.aNumber = aNumber;
	}

	public Date getaDate() {
		return aDate;
	}

	public void setaDate(Date aDate) {
		this.aDate = aDate;
	}

	public OObject() {
	}

	public OObject(String aName, Double aNumber, Date aDate) {
		super();
		this.aName = aName;
		this.aNumber = aNumber;
		this.aDate = aDate;
	}

	private String aName;
	private Double aNumber;
	private Date aDate;

	public String getSQLTypeName() throws SQLException {
		// TODO Auto-generated method stub
		return "BLOCK.O_OBJECT";
	}

	public void readSQL(SQLInput arg0, String arg1) throws SQLException {
		aName = arg0.readString();
		aNumber = arg0.readDouble();
		aDate = arg0.readDate();

	}

	public void writeSQL(SQLOutput arg0) throws SQLException {
		arg0.writeString(aName);
		arg0.writeDouble(aNumber);
		arg0.writeDate(aDate);
	}

}