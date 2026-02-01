package com.granzonamarciana.entity;

import androidx.room.PrimaryKey;

import java.io.Serializable;

public abstract class DomainEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public DomainEntity() {

    }
    public DomainEntity(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
