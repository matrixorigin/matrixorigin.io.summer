package com.ning.codebot.common.repo.controller;

import com.ning.codebot.common.chat.domain.vo.response.ChatMessageResp;
import com.ning.codebot.common.client.LLMClient;
import com.ning.codebot.common.common.utils.RequestHolder;
import com.ning.codebot.common.domain.vo.response.ApiResult;
import com.ning.codebot.common.repo.domain.RepoUploadReq;
import com.ning.codebot.common.repo.service.RepoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("codebot/repo")
@Api(tags = "The interface for chat service")
@Slf4j
public class RepoController {

    @Autowired
    RepoService repoService;

    @PostMapping("/upload")
    @ApiOperation("subscribe the repository")
    public ApiResult<ChatMessageResp> upload(@Valid @RequestBody RepoUploadReq request) {
        // store in DB
        repoService.storeRepo(request.getUserName(), request.getRepoName());
        return ApiResult.success();
    }

    @PostMapping("/check")
    @ApiOperation("check the repository status")
    public ApiResult<ChatMessageResp> check(@Valid @RequestBody RepoUploadReq request) {
        // store in DB
        int res = repoService.checkRepo(request.getUserName(), request.getRepoName());
        ChatMessageResp chatMessageResp = new ChatMessageResp();
        if (res == 0)
            chatMessageResp.setContent("building");
        if (res == 1)
            chatMessageResp.setContent("success");
        else
            chatMessageResp.setContent("fail");
        return ApiResult.success(chatMessageResp);
    }
}