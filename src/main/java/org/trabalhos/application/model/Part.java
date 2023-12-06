package org.trabalhos.application.model;

public class Part {

    private Integer id;
    private String name;
    private Double price;
    private Integer manufacturerId;

    public Part(Integer id, String name, Double price, Integer manufacturerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.manufacturerId = manufacturerId;
    }

    public Part() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
}
