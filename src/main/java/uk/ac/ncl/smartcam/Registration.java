package uk.ac.ncl.smartcam;

import java.util.Date;

/**
 * Smart Camera Registration message object
 * @author Stephen Shephard
 *
 */
public class Registration {

	private Long id;
	private Date timestamp;
	private String street;
	private String town;
	private int speedlimit;
	
	/**
	 * @param id
	 * @param street
	 * @param town
	 * @param speedlimit
	 */
	public Registration(Long id, String street, String town, int speedlimit) {
		this.id = id;
		this.timestamp = new Date();
		this.street = street;
		this.town = town;
		this.speedlimit = speedlimit;
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
		this.timestamp = timestamp;
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
	
	
}
