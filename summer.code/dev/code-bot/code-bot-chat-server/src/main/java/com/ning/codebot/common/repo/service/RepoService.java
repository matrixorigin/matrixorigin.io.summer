package com.ning.codebot.common.repo.service;

public interface RepoService {
    public void storeRepo(String userName, String repoName);
    public Integer checkRepo(String userName, String repoName);
}
