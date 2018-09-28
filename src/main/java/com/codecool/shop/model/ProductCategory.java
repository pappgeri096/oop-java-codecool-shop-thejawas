package com.codecool.shop.model;

public class ProductCategory extends BaseModel {
    private String department;

    public ProductCategory() {
    }

    public ProductCategory(int id, String name, String department, String description) {
        super(id, name, description);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public String toString() {
        return String.format(
                "id: %1$d," +
                        "name: %2$s, " +
                        "department: %3$s, " +
                        "description: %4$s",
                this.id,
                this.name,
                this.department,
                this.description);
    }
}