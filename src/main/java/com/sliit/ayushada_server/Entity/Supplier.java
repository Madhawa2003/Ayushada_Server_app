package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Supplier {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "isActive")
    private Byte isActive;
    @Basic
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "supplier")
    private Collection<SupplierLogs> supplierLogs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return id == supplier.id && Objects.equals(name, supplier.name) && Objects.equals(isActive, supplier.isActive) && Objects.equals(description, supplier.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isActive, description);
    }

    public Collection<SupplierLogs> getSupplierLogs() {
        return supplierLogs;
    }

    public void setSupplierLogs(Collection<SupplierLogs> supplierLogs) {
        this.supplierLogs = supplierLogs;
    }
}
