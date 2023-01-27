package com.example.community.advice;

import com.example.community.exception.*;
import com.example.community.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    // 500 에러
    @ExceptionHandler(IllegalArgumentException.class) // 지정한 예외가 발생하면 해당 메소드 실행
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 각 예외마다 상태 코드 지정
    public Response illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(500, e.getMessage().toString());
    }

    // 401 응답
    // 아이디 혹은 비밀번호 오류시 발생
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return Response.failure(401, "로그인에 실패하였습니다.");
    }

    // 409 응답
    // username 중복
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(UsernameAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 아이디 입니다.");
    }

    // 404 응답
    // 요청한 유저를 찾을 수 없음
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return Response.failure(404, "요청한 회원을 찾을 수 없습니다.");
    }

    // 404 응답
    // 요청한 쪽지를 찾을 수 없음
    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response messageNotFoundException() {
        return Response.failure(404, "요청한 쪽지를 찾을 수 없습니다.");
    }

    // 404 응답
    // 요청한 게시판을 찾을 수 없음
    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response boardNotFoundException() {
        return Response.failure(404, "요청한 게시판을 찾을 수 없습니다.");
    }

    // 404 응답
    // 좋아요기록을 찾을 수 없음
    @ExceptionHandler(LikeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response likeNotFoundException() {
        return Response.failure(404, "게시판 좋아요 기록을 찾을 수 없습니다.");
    }

    // 404 응답
    // 즐겨찾기를 찾을 수 없음
    @ExceptionHandler(FavoriteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response favoriteNotFoundException() {
        return Response.failure(404, "즐겨찾기 게시판을 찾을 수 없습니다.");
    }

    // 404 응답
    // 댓글을 찾을 수 없음
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException() {
        return Response.failure(404, "댓글을 찾을 수 없습니다.");
    }

    // 404 응답
    // Image 형식 지원하지 않음
    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response unsupportedImageFormatException() {
        return Response.failure(404, "이미지 형식을 지원하지 않습니다.");
    }

    // 404 응답
    // 파일 업로드 실패
    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        log.error("e = {}", e.getMessage());
        return Response.failure(404, "이미지 업로드 실패");
    }

    // 404 응답
    // 자기 자신을 신고할 수 없다.
    @ExceptionHandler(NotSelfReportException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response notSelfReportException() {
        return Response.failure(404, "자기 자신은 신고할 수 없습니다.");
    }

    // 404 응답
    // 이미 신고 기록이 있어서 신고 실패 실패
    @ExceptionHandler(AlreadyReportException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response alreadyReportException() {
        return Response.failure(404, "이미 신고를 했습니다.");
    }

    // 401 응답
    // 요청자와 요청한 유저의 정보가 일치하지 않을시에 발생
    @ExceptionHandler(MemberNotEqualsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response memberNotEqualsException() {
        return Response.failure(401, "유저 정보가 일치하지 않습니다.");
    }

}
