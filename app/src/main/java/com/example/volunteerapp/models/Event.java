package com.example.volunteerapp.models;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventId;
    private String organizerId;
    private String organizerName;
    private String eventName;
    private String description;
    private String venue;
    private String city;
    private double latitude;
    private double longitude;
    private long eventDate;
    private String eventTime;
    private int volunteersRequired;
    private List<String> rolesRequired;
    private String imageUrl;
    private String contactPhone;
    private String contactEmail;
    private String status; // "upcoming", "ongoing", "completed"
    private long createdAt;
    private int registeredVolunteers;

    public Event() {
        this.rolesRequired = new ArrayList<>();
        this.registeredVolunteers = 0;
    }

    public Event(String eventId, String organizerId, String organizerName, String eventName) {
        this.eventId = eventId;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.eventName = eventName;
        this.rolesRequired = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.status = "upcoming";
        this.registeredVolunteers = 0;
    }

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getOrganizerId() { return organizerId; }
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public long getEventDate() { return eventDate; }
    public void setEventDate(long eventDate) { this.eventDate = eventDate; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public int getVolunteersRequired() { return volunteersRequired; }
    public void setVolunteersRequired(int volunteersRequired) { this.volunteersRequired = volunteersRequired; }

    public List<String> getRolesRequired() { return rolesRequired; }
    public void setRolesRequired(List<String> rolesRequired) { this.rolesRequired = rolesRequired; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public int getRegisteredVolunteers() { return registeredVolunteers; }
    public void setRegisteredVolunteers(int registeredVolunteers) { this.registeredVolunteers = registeredVolunteers; }
}
