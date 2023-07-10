package org.example.database.entity;


public interface BaseEntity <I>{

    I getId();

    void setId(I id);
}