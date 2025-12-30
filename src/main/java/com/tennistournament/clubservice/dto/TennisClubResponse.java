package com.tennistournament.clubservice.dto;

import java.util.ArrayList;
import java.util.List;

public class TennisClubResponse {
    private Long id;
    private String name;
    private String address;
    private List<Long> courtIds = new ArrayList<>();

    // Constructors
    public TennisClubResponse() {
    }

    public TennisClubResponse(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Long> getCourtIds() {
        return courtIds;
    }

    public void setCourtIds(List<Long> courtIds) {
        this.courtIds = courtIds;
    }
}
