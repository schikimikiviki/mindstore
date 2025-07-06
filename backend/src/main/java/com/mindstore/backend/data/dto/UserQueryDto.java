package com.mindstore.backend.data.dto;

import java.util.Date;

public class UserQueryDto {
    private String email;
    private Date createdAt;


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

