package com.example.androidtask.api;

import androidx.annotation.NonNull;

import com.example.androidtask.model.GithubRepository;
import com.example.androidtask.model.GithubReposResponse;
import com.example.androidtask.model.Owner;
import com.jakewharton.retrofit2.adapter.rxjava2.Result;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Endpoints {

    @GET("search/repositories")
    Observable<GithubReposResponse> gitGithubRepos(@NonNull @Query("q") String qualifier,
                                                   @Query("per_page") int perPage,
                                                   @Query("page") int page);

    @GET("repositories/{id}")
    Observable<GithubRepository> gitGithubRepository(@Path(value = "id") int id);

    @GET("users/{login}")
    Observable<Owner> getGithubUser(@Path(value = "login") String login);

    @GET("users/{login}/repos")
    Observable<Result<ArrayList<GithubRepository>>> getGithubUserRepos(@Path(value = "login") String login,
                                                                       @Query("per_page") int perPage,
                                                                       @Query("page") int page);

}
