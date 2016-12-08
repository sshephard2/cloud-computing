package uk.ac.ncl.smartcam;

import java.io.Serializable;
import java.util.Date;

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * Smart Camera Registration message object
 * @author Stephen Shephard
 *
 */
public class Registration extends TableServiceEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Date timestamp;
	private String street;
	private String town;
	private int speedlimit;
	
	/**
	 * No-arg constructor for Jackson JSON serialization
	 */
	public Registration() {
		// Do nothing
	}	
	
	/**
	 * Constructor for Smart Camera Registration message
	 * @param id camera id
	 * @param street street name
	 * @param town town/city
	 * @param speedlimit speed limit in mph
	 */
	public Registration(Long id, String street, String town, int speedlimit) {
		this.id = id;
		this.timestamp = new Date();
		this.street = street;
		this.town = town;
		this.speedlimit = speedlimit;
        this.partitionKey = "Registration"; // PartitionKey
        this.rowKey = id.toString() + Long.toString(timestamp.getTime()); // RowKey
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = (Date)timestamp.clone();
	}
	
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	
	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}
	
	/**
	 * @param town the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}
	
	/**
	 * @return the speedlimit
	 */
	public int getSpeedlimit() {
		return speedlimit;
	}
	
	/**
	 * @param speedlimit the speedlimit to set
	 */
	public void setSpeedlimit(int speedlimit) {
		this.speedlimit = speedlimit;
	}
	
	/**
	 * @return the rowKey
	 */
	public String getRowKey() {
		return rowKey;
	}

	/**
	 * @param rowKey the rowKey to set
	 */
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Registration [id=" + id + ", timestamp=" + timestamp + ", street=" + street + ", town=" + town
				+ ", speedlimit=" + speedlimit + "]";
	}
	
	
}
