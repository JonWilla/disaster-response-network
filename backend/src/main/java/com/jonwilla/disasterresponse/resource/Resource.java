package com.jonwilla.disasterresponse.resource;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @UuidGenerator
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @Column(
            name = "name",
            nullable = false,
            length = 150
    )
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "type",
            nullable = false,
            length = 50
    )
    private ResourceType type;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private ResourceStatus status;

    @Column(
            name = "quantity",
            nullable = false
    )
    private int quantity;

    @Column(
            name = "location",
            nullable = false,
            length = 255
    )
    private String location;

    protected Resource() {
    }

    public Resource(
            String name,
            ResourceType type,
            ResourceStatus status,
            int quantity,
            String location
    ) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.quantity = quantity;
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(
            ResourceType type
    ) {
        this.type = type;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    public void setStatus(
            ResourceStatus status
    ) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(
            int quantity
    ) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(
            String location
    ) {
        this.location = location;
    }
}