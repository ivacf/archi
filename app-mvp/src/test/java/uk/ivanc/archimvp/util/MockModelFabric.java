package uk.ivanc.archimvp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ivanc.archimvp.model.Repository;
import uk.ivanc.archimvp.model.User;

public class MockModelFabric {

    public static List<Repository> newListOfRepositories(int numRepos) {
        List<Repository> repositories = new ArrayList<>(numRepos);
        for (int i = 0; i < numRepos; i++) {
            repositories.add(newRepository("Repo " + i));
        }
        return repositories;
    }

    public static Repository newRepository(String name) {
        Random random = new Random();
        Repository repository = new Repository();
        repository.name = name;
        repository.id = random.nextInt(10000);
        repository.description = "Description for " + name;
        repository.watchers = random.nextInt(100);
        repository.forks = random.nextInt(100);
        repository.stars = random.nextInt(100);
        repository.owner = newUser("User-" + name);
        return repository;
    }

    public static User newUser(String name) {
        Random random = new Random();
        User user = new User();
        user.id = random.nextInt(10000);
        user.name = name;
        user.email = name + "@email.com";
        user.location = "Location of " + name;
        user.url = "http://user.com/" + name;
        user.avatarUrl = "http://user.com/image/" + name;
        return user;
    }
}
