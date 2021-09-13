package com.mazanenko.petproject.firstspringcrudapp.entity;

public abstract class Product {
    private int id;
    private String name;
    private String description;
    private int availableQuantity;

    private ProductPhoto productPhoto;

    public Product() {}

    public Product(int id, String name, String description, int availableQuantity,
                   ProductPhoto productPhoto) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.availableQuantity = availableQuantity;
        this.productPhoto = productPhoto;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", availableQuantity='" + getAvailableQuantity() + '\'' +
                '}';
    }

    class ProductPhoto {
        private int id;
        private String URL;
        private int productId;

        protected ProductPhoto() {}

        public ProductPhoto(int id, String URL, int productId) {
            this.id = id;
            this.URL = URL;
            this.productId = productId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }
    }
}
