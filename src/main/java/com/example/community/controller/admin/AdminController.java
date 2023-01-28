package com.example.community.controller.admin;

import com.example.community.response.Response;
import com.example.community.service.admin.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Admin Controller", tags = "Admin")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/manages")
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "정지 유저 관리", notes = "정지된 유저를 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members")
    public Response findAllReportedMember(){
        return Response.success(adminService.findAllReportedMember());
    }

    @ApiOperation(value = "신고된 유저 정지 해제", notes = "신고된 유저를 정지 해제시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/members/{id}")
    public Response unlockMember(@PathVariable Long id) {
        return Response.success(adminService.unlockMember(id));
    }

    @ApiOperation(value = "신고된 유저 삭제", notes = "신고된 유저를 삭제 시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/members/{id}")
    public Response deleteReportedMember(@PathVariable Long id) {
        return Response.success(adminService.deleteReportedMember(id));
    }

    @ApiOperation(value = "게시판 관리", notes = "게시판을 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards")
    public Response findAllReportedBoards() {
        return Response.success(adminService.findAllReportedBoards());
    }

    @ApiOperation(value = "신고된 게시판 정지 해제", notes = "신고된 게시판을 정지 해제시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards/{id}")
    public Response unlockBoard(@PathVariable Long id) {
        return Response.success(adminService.unlockBoard(id));
    }

    @ApiOperation(value = "신고된 게시글 삭제", notes = "신고된 게시글을 삭제 시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/boards/{id}")
    public Response deleteReportedBoard(@PathVariable Long id) {
        return Response.success(adminService.deleteReportedBoard(id));
    }


    @ApiOperation(value = "댓글 관리", notes = "댓글을 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/comments")
    public Response findAllReportedComments() {
        return Response.success(adminService.findAllReportedComments());
    }

    @ApiOperation(value = "신고된 댓글 정지 해제", notes = "신고된 댓글을 정지 해제시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/comments/{id}")
    public Response unlockComment(@PathVariable Long id) {
        return Response.success(adminService.unlockComment(id));
    }


    @ApiOperation(value = "신고된 댓글 삭제", notes = "신고된 댓글을 삭제 시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/comments/{id}")
    public Response deleteReportedComment(@PathVariable Long id) {
        return Response.success(adminService.deleteReportedComment(id));
    }
}
