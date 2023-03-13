package com.example.community.factory;

import com.example.community.domain.category.Category;


public class CategoryFactory {

    public static Category createCategory(){
        return new Category("name",null);
    }
}
