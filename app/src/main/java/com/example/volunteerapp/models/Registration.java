package com.example.volunteerapp.models;

public class Registration {
    private String registrationId;
    private String eventId;
    private String volunteerId;
    private String volunteerName;
    private String volunteerEmail;
    private String volunteerPhone;
    private String status; // "pending", "approved", "rejected"
    private String assignedRole;
    private String assignedTimeSlot;
    private long registrationDate;
    private boolean checkedIn;
    private String qrCode;

    public Registration() {
        // Default constructor required for Firebase
    }

    public Registration(String registrationId, String eventId, String volunteerId) {
        this.registrationId = registrationId;
        this.eventId = eventId;
        this.volunteerId = volunteerId;
        this.status = "pending";
        this.registrationDate = System.currentTimeMillis();
        this.checkedIn = false;
    }

    // Getters and Setters
    public String getRegistrationId() { return registrationId; }
    public void setRegistrationId(String registrationId) { this.registrationId = registrationId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getVolunteerId() { return volunteerId; }
    public void setVolunteerId(String volunteerId) { this.volunteerId = volunteerId; }

    public String getVolunteerName() { return volunteerName; }
    public void setVolunteerName(String volunteerName) { this.volunteerName = volunteerName; }

    public String getVolunteerEmail() { return volunteerEmail; }
    public void setVolunteerEmail(String volunteerEmail) { this.volunteerEmail = volunteerEmail; }

    public String getVolunteerPhone() { return volunteerPhone; }
    public void setVolunteerPhone(String volunteerPhone) { this.volunteerPhone = volunteerPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedRole() { return assignedRole; }
    public void setAssignedRole(String assignedRole) { this.assignedRole = assignedRole; }

    public String getAssignedTimeSlot() { return assignedTimeSlot; }
    public void setAssignedTimeSlot(String assignedTimeSlot) { this.assignedTimeSlot = assignedTimeSlot; }

    public long getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(long registrationDate) { this.registrationDate = registrationDate; }

    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}