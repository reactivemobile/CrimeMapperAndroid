# CrimeMapper [![Build Status](https://travis-ci.org/reactivemobile/CrimeMapperAndroid.svg?branch=master)](https://travis-ci.org/reactivemobile/CrimeMapperAndroid)

This is [a simple android application](https://play.google.com/store/apps/details?id=com.reactivemobile.ukpoliceapp) for downloading and displaying crime report data from the UK Police 
See https://data.police.uk/ for API documentation

The app is built using MVP principles with some help from 
- [Dagger 2](https://google.github.io/dagger/)
- [Retrofit](https://square.github.io/retrofit/)
- [Gson](https://github.com/google/gson)
- [PermissionsDispatcher](https://github.com/hotchemi/PermissionsDispatcher)
- [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [Parceler](https://github.com/johncarl81/parceler)
- [Timber](https://github.com/JakeWharton/timber)
- [Butterknife](http://jakewharton.github.io/butterknife)
- [Supertooltips](https://github.com/nhaarman/supertooltips)

Unit testing is carried out using JUnit and Mockito

- [Mockito](http://site.mockito.org/)

To enable Google Maps in the application please add your [maps api key](https://developers.google.com/maps/android/) to the *api-keys.xml* file
