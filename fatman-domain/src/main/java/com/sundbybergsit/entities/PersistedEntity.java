package com.sundbybergsit.entities;

import javax.persistence.*;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class PersistedEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        throw new UnsupportedOperationException("This operation is not supported!");
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
