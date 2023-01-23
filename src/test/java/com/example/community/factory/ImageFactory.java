package com.example.community.factory;

import com.example.community.domain.board.Image;

public class ImageFactory {

    public static Image createImage() {
        return new Image("origin_filename.jpg");
    }
}
