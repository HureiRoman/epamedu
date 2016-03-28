package edu.epam.model;

import java.util.Date;

import edu.epam.annotations.Column;

/**
 * Created by fastforward on 19/06/15.
 */
public class Interview {
    @Column("interview_id")
    private Integer id = null;
    @Column("direction_id")
    private Integer directionId = null;
    @Column("date_of_testing")
    private Date dateOfTesting;
    @Column("place")
    private String place;
    @Column("description_interview")
    private String description;
    
    private Direction directionInfo;
    
    public Direction getDirectionInfo() {
		return directionInfo;
	}

	public void setDirectionInfo(Direction directionInfo) {
		this.directionInfo = directionInfo;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Integer directionId) {
        this.directionId = directionId;
    }

    public Date getDateOfTesting() {
        return dateOfTesting;
    }

    public void setDateOfTesting(Date dateOfTesting) {
        this.dateOfTesting = dateOfTesting;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
