package com.mazanenko.petproject.bookshop.entity;

public enum ProductType {
    BOOK(Constants.BOOK), POSTER(Constants.POSTER);

    ProductType(String type) {
        if (!type.equals(this.name())) throw new IllegalArgumentException();
    }

    public static class Constants {
        public static final String BOOK = "BOOK";
        public static final String POSTER = "POSTER";
    }
}
