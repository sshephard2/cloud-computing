package uk.ac.ncl.smartcam;

import java.io.Serializable;
import java.util.Date;

/**
 * Smart Camera Sighting message object
 * @author Stephen Shephard
 *
 */
public class Sighting implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date timestamp;
	private String registration;
	private String vehicletype;
	private float speed;

	/**
	 * @param id
	 * @param registration
	 * @param vehicletype
	 * @param speed
	 */
	public Sighting(Long id, String registration, String vehicletype, float speed) {
		this.id = id;
		this.timestamp = new Date();
		this.registration = registration;
		this.vehicletype = vehicletype;
		this.speed = speed;
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
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
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
