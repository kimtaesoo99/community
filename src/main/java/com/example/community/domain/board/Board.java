package com.example.community.domain.board;

import com.example.community.domain.category.Category;
import com.example.community.domain.common.BaseEntity;
import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardUpdateRequestDto;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Category category;

    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    private int likeCount;

    private int viewCount;

    private boolean reportedStatus;

    public Board(String title, String content, Member member,Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.likeCount = 0;
        this.viewCount = 0;
        this.category = category;
        this.images = new ArrayList<>();
        reportedStatus = false;
        addImages(images);
    }

    public ImageUpdatedResult update(BoardUpdateRequestDto req) {
        this.title = req.getTitle();
        this.content = req.getContent();
        ImageUpdatedResult result = findImageUpdatedResult(req.getAddedImages(), req.getDeletedImages());
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        onPreUpdate();
        return result;
    }

    private void addImages(List<Image> added) {
        added.forEach(i -> {
            images.add(i);
            i.initBoard(this);
        });
    }

    private void deleteImages(List<Image> deleted) {
        deleted.forEach(di -> this.images.remove(di));
    }

    private ImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles, List<Long> deletedImageIds) {
        List<Image> addedImages = convertImageFilesToImages(addedImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
    }

    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
            .map(id -> convertImageIdToImage(id))
            .filter(i -> i.isPresent())
            .map(i -> i.get())
            .collect(toList());
    }

    private Optional<Image> convertImageIdToImage(Long id) {
        return this.images.stream().filter(i -> i.getId() == (id)).findAny();
    }

    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile -> new Image(imageFile.getOriginalFilename())).collect(toList());
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        this.likeCount -= 1;
    }

    public void increaseViewCount(){
        this.viewCount +=1;
    }

    public boolean isOwnBoard(Member member) {
        return this.member.equals(member);
    }

    public void reportBoard(){
        reportedStatus = true;
    }

    public void unlockReportedStatus(){
        reportedStatus = false;
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult {
        private List<MultipartFile> addedImageFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }
}
