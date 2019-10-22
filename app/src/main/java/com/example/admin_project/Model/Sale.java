package com.example.admin_project.Model;

public class Sale {
    private String Name;
    private String Image;

    public Sale() {
    }

    public Sale(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
