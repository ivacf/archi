# Archi
This repository showcases and compares different architectural patterns that can be used to build Android apps. The exact same sample app is built three times using the following approaches:
* __Standard Android__: traditional approach with layouts, Activities/Fragments and Model.
* __MVP__: Model View Presenter.
* __MVVM__: Model View ViewModel with data binding. 

## The App

The sample app displays a list of GitHub public repositories for a given username. Tapping on one of them will open a repository details screen, where more information about the repo can be found. This screen also shows information about the owner of the repository. 

![Screenshots](images/archi-screenshots.png)

### Libraries used 
* AppCompat, CardView and RecyclerView
* Data Binding (only MVVM)
* RxJava & RxAndroid
* Retrofit 2
* Picasso
* Mockito
* Robolectric

## Standard Android
The `/app` directoy contains the implementation that follows the traditional standar Android approach. This is a couple of layouts files, two Activities and the model. The model is exaclty the same for the three implementations, and it contains: `Repository`, `User` and a retrofit service (`GithubService`).

With this approach, Activities are in charge of calling the `GithubService`, process the data and update the views. They act kind of like a controller in MVC but with some extra responsabilities that should be part of the view. The problem with this standard architecture is that Activities and Fragments can become quite large and very difficult to tests. Hence why I didn't write any unit test for this case. 

## MVP - Model View Presenter
In `/app-mvp` you will find the sample app implemented following this pattern. When using mvp, Activities and Fragments become part of the view layer and they delegate most of the work to presenters. Each Activity has a matching presenter that handles accessing the model via the `GithubService`. They also notify the Activities when the data is ready to display. Unit testing presenters becomes very easy by mocking the view layer (Activities).

## MVVM - Model View ViewModel
This patter has recently started to gain popularity due to the release of the [data binding library](https://developer.android.com/tools/data-binding/guide.html). You will find the implementation in `/app-mvvm`. In this case, ViewModels retrive data from the model when requested form the view via data biding. With this patter Activities and Fragments become very lightweight. Moroever, writting unit tests becomes easier because the ViewModels are decoupled form the view.

