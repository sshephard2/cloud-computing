package uk.ac.ncl.smartcam;

import java.io.Serializable;
import java.util.Date;

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * Smart Camera Speeding Vehicle entity object
 * @author Stephen Shephard
 *
 */
public class SpeedingVehicle extends TableServiceEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date timestamp;
	private String registration;
	private String vehicletype;
	private int speed;
	private int speedlimit;
	private String priority;

	/**
	 * Returns the year a vehicle was registered (from the registration number)
	 * @param registration
	 * @return
	 */
	private int registrationYear(String registration) {
		// Actual rules are more complex than this, this is a huge simplification
		return 2000 + Integer.parseInt(registration.substring(2, 4));
	}
	
	/**
	 * No-arg constructor for Jackson JSON serialization
	 */
	public SpeedingVehicle() {
		// Do nothing
	}
	
	/**
	 * Constructor for Smart Camera Speeding Vehicles message
	 * @param id camera id
	 * @param registration vehicle registration number
	 * @param vehicletype type of vehicle Car|Truck|Motorcycle
	 * @param speed speed of vehicle in mph
	 * @param speedlimit speed limit in mph
	 * @param priority PRIORITY if this is a priority speeding vehicle
	 */
	public SpeedingVehicle(Long id, String registration, String vehicletype, int speed, int speedlimit, String priority) {
		this.id = id;
		this.timestamp = new Date();
		this.registration = registration;
		this.vehicletype = vehicletype;
		this.speed = speed;
		this.speedlimit = speedlimit;
		this.partitionKey = "SpeedingVehicle" + registrationYear(registration) ; // PartitionKey
		this.rowKey = registration + Long.toString(timestamp.getTime()); // RowKey
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

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return the partitionKey
	 */
	public String getPartitionKey() {
		return partitionKey;
	}

	/**
	 * @param partitionKey the partitionKey to set
	 */
	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
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
		return "Sighting [id=" + id + ", timestamp=" + timestamp + ", registration=" + registration + ", vehicletype="
				+ vehicletype + ", speed=" + speed + "]";
	}
}
