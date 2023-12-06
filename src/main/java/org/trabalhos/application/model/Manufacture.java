package org.trabalhos.application.model;

public class Manufacture {

    private Integer idManufacture;
    private String name;

    public Manufacture(Integer idManufacture, String name) {
        this.idManufacture = idManufacture;
        this.name = name;
    }

    public Manufacture() {

    }

    public Integer getIdManufacture() {
        return idManufacture;
    }

    public void setIdManufacture(Integer idManufacture) {
        this.idManufacture = idManufacture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
