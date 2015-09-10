package com.ivancarballo.archi.model;

import com.google.gson.annotations.SerializedName;

public class Repository {
    public long id;
    public String name;
    public String description;
    public int forks;
    public int watchers;
    @SerializedName("stargazers_count")
    public int starts;
}
