package com.example.community.factory;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Image;

import java.util.ArrayList;
import java.util.List;

import static com.example.community.factory.ImageFactory.createImage;
import static com.example.community.factory.MemberFactory.createMember;

public class BoardFactory {

    public static Board createBoard() {
        List<Image> images = new ArrayList<>();
        images.add(createImage());
        Board board = new Board("title", "content", createMember(), images);
        return board;
    }
}
