package com.example.community.controller.message;

import com.example.community.config.guard.Login;
import com.example.community.domain.member.Member;
import com.example.community.dto.message.MessageCreateRequestDto;
import com.example.community.response.Response;
import com.example.community.service.message.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Messages Controller", tags = "Messages")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "편지 작성", notes = "편지 보내기")
    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Response sendMessage(@Valid @RequestBody MessageCreateRequestDto messageCreateRequestDto, @Login Member member) {
        messageService.sendMessage(member, messageCreateRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "받은 편지 전체 조회", notes = "받은 편지 전체 조회")
    @GetMapping("/messages/receiver")
    @ResponseStatus(HttpStatus.OK)
    public Response findALlReceiveMessages(@Login Member member) {
        return Response.success(messageService.findALlReceiveMessages(member));
    }

    @ApiOperation(value = "받은 편지 단건 조회", notes = "받은 편지 단건 조회")
    @GetMapping("/messages/receiver/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findReceiveMessage(@ApiParam(value = "쪽지 id", required = true) @PathVariable("id") Long id
        , @Login Member member) {
        return Response.success(messageService.findReceiveMessage(member, id));
    }

    @ApiOperation(value = "보낸 편지 전체 조회", notes = "받은 편지 전체 조회")
    @GetMapping("/messages/sender")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllSendMessages(@Login Member member) {
        return Response.success(messageService.findAllSendMessages(member));
    }

    @ApiOperation(value = "보낸 편지 단건 조회", notes = "보낸 편지 단건 조회")
    @GetMapping("/messages/sender/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findSendMessage(@ApiParam(value = "쪽지 id", required = true) @PathVariable("id") Long id
        , @Login Member member) {
        return Response.success(messageService.findSendMessage(member, id));
    }

    @ApiOperation(value = "받은 편지 삭제", notes = "받은 편지 삭제")
    @DeleteMapping("/messages/receiver/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteReceiverMessage(@ApiParam(value = "쪽지 id", required = true) @PathVariable("id") Long id
        , @Login Member member) {
        messageService.deleteReceiverMessage(member, id);
        return Response.success();
    }

    @ApiOperation(value = "보낸 편지 삭제", notes = "보낸 편지 삭제")
    @DeleteMapping("/messages/sender/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteSenderMessage(@ApiParam(value = "쪽지 id", required = true) @PathVariable("id") Long id
        , @Login Member member) {
        messageService.deleteSenderMessage(member, id);
        return Response.success();
    }
}
