package edu.epam.model;

import edu.epam.annotations.Column;

/**
 * Created by fastforward on 20/06/15.
 */
public class InterviewResult {
    @Column("id")
    private Integer id = null;
    @Column("rating")
    private Integer rating = null;
    @Column("application_id")
    private Integer applicationId = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
}
