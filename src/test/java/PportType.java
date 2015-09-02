/**
 * PportType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */


import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class PportType  implements java.io.Serializable , SQLData{
    private java.lang.String port_name;

    private java.lang.String port_type;

    private java.lang.String mac_domain_name;

    public PportType() {
    }

    public PportType(
           java.lang.String port_name,
           java.lang.String port_type,
           java.lang.String mac_domain_name) {
           this.port_name = port_name;
           this.port_type = port_type;
           this.mac_domain_name = mac_domain_name;
    }


    /**
     * Gets the port_name value for this PportType.
     *
     * @return port_name
     */
    public java.lang.String getPort_name() {
        return port_name;
    }


    /**
     * Sets the port_name value for this PportType.
     *
     * @param port_name
     */
    public void setPort_name(java.lang.String port_name) {
        this.port_name = port_name;
    }


    /**
     * Gets the port_type value for this PportType.
     *
     * @return port_type
     */
    public java.lang.String getPort_type() {
        return port_type;
    }


    /**
     * Sets the port_type value for this PportType.
     *
     * @param port_type
     */
    public void setPort_type(java.lang.String port_type) {
        this.port_type = port_type;
    }


    /**
     * Gets the mac_domain_name value for this PportType.
     *
     * @return mac_domain_name
     */
    public java.lang.String getMac_domain_name() {
        return mac_domain_name;
    }


    /**
     * Sets the mac_domain_name value for this PportType.
     *
     * @param mac_domain_name
     */
    public void setMac_domain_name(java.lang.String mac_domain_name) {
        this.mac_domain_name = mac_domain_name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PportType)) return false;
        PportType other = (PportType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.port_name==null && other.getPort_name()==null) ||
             (this.port_name!=null &&
              this.port_name.equals(other.getPort_name()))) &&
            ((this.port_type==null && other.getPort_type()==null) ||
             (this.port_type!=null &&
              this.port_type.equals(other.getPort_type()))) &&
            ((this.mac_domain_name==null && other.getMac_domain_name()==null) ||
             (this.mac_domain_name!=null &&
              this.mac_domain_name.equals(other.getMac_domain_name())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getPort_name() != null) {
            _hashCode += getPort_name().hashCode();
        }
        if (getPort_type() != null) {
            _hashCode += getPort_type().hashCode();
        }
        if (getMac_domain_name() != null) {
            _hashCode += getMac_domain_name().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }



    //For SQLData

	public String getSQLTypeName() throws SQLException {
		return "HSI.O_PPORTTYPE";
	}

	public void readSQL(SQLInput stream, String typeName) throws SQLException {
		//this.sqlTypeName=typeName;
		this.port_name= stream.readString();
		this.port_type=stream.readString();
		this.mac_domain_name=stream.readString();
	}

	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeString(port_name);
		stream.writeString(port_type);
		stream.writeString(mac_domain_name);
	}
}
