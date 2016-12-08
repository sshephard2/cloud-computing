package uk.ac.ncl.smartcam;

import java.io.Serializable;
import java.util.Date;

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * Smart Camera Sighting message object
 * @author Stephen Shephard
 *
 */
public class Sighting extends TableServiceEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date timestamp;
	private String registration;
	private String vehicletype;
	private int speed;
	private int speedlimit;

	/**
	 * No-arg constructor for Jackson JSON serialization
	 */
	public Sighting() {
		// Do nothing
	}
	
	/**
	 * Constructor for Smart Camera Sighting message
	 * @param id camera id
	 * @param registration vehicle registration number
	 * @param vehicletype type of vehicle Car|Truck|Motorcycle
	 * @param speed speed of vehicle in mph
	 * @param speedlimit speed limit in mph
	 */
	public Sighting(Long id, String registration, String vehicletype, int speed, int speedlimit) {
		this.id = id;
		this.timestamp = new Date();
		this.registration = registration;
		this.vehicletype = vehicletype;
		this.speed = speed;
		this.speedlimit = speedlimit;
		this.partitionKey = registration; // PartitionKey
		this.rowKey = Long.toString(timestamp.getTime()); // RowKey
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
        this.rowKey = Long.toString(timestamp.getTime()); // RowKey
	}

	/**
	 * @return the registration
	 */
	public String getRegistration() {
		return registration;
	}

	/**
	 * @param registration the registration to set
	 */
	public void setRegistration(String registration) {
		this.registration = registration;
        this.partitionKey = registration; // PartitionKey
	}

	/**
	 * @return the vehicletype
	 */
	public String getVehicletype() {
		return vehicletype;
	}

	/**
	 * @param vehicletype the vehicletype to set
	 */
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sighting [id=" + id + ", timestamp=" + timestamp + ", registration=" + registration + ", vehicletype="
				+ vehicletype + ", speed=" + speed + "]";
	}
}
