package com.ning.codebot.common.repo.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ning.codebot.common.chat.domain.entity.Message;
import com.ning.codebot.common.chat.mapper.MessageMapper;
import com.ning.codebot.common.repo.domain.entity.UserRepo;
import com.ning.codebot.common.repo.mapper.RepoMapper;
import com.ning.codebot.common.user.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepoDao extends ServiceImpl<RepoMapper, UserRepo> {
    public List<UserRepo> getRepos(String userName) {
        return lambdaQuery()
                .eq(UserRepo::getUserName, userName)
                .list();
    }

    public Boolean hasRepo(String userName, String repoName) {
        Integer count = lambdaQuery()
                        .eq(UserRepo::getUserName, userName)
                        .eq(UserRepo::getRepoName, repoName)
                        .count();
        if (count == 0) return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public Integer checkRepo(String userName, String repoName) {
        UserRepo userRepo = lambdaQuery()
                .eq(UserRepo::getUserName, userName)
                .eq(UserRepo::getRepoName, repoName)
                .one();
        return userRepo.getStatus();
    }

    public List<String> getUnsuccessRepo(){
        return lambdaQuery()
                .eq(UserRepo::getStatus, 0)
                .list()
                .stream()
                .map(UserRepo::getRepoName)
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean updateUnsuccessfulRepos() {
        return lambdaUpdate()
                .eq(UserRepo::getTimes, 2)
                .ne(UserRepo::getStatus, 1)
                .set(UserRepo::getStatus, 2)
                .update();
    }

    public boolean updateSuccessRepo(String repoName) {
        return lambdaUpdate()
                .eq(UserRepo::getRepoName, repoName)
                .set(UserRepo::getStatus, 1)
                .update();
    }

    public boolean updateTimesRepo(String repoName) {
        return lambdaUpdate()
                .eq(UserRepo::getRepoName, repoName)
                .setSql("update_time = update_time + 1")
                .update();
    }

}
