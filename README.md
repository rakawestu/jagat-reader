#Jagat Reader

# Description

This is open source project with a provide mobile apps using [JagatReview website](http://jagatreview.com) as data source. 

This app design is using material design provided by Android support library.

This app can be downloaded from play store.

[Download here](https://play.google.com/store/apps/details?id=com.github.rakawestu.jagatreader)

# Used Libraries

This project uses various libraries.

1. Picasso (Image Loader).
2. Retrofit (Typesafe rest client).
3. Okhttp (Http client).
4. Timber (Logging).
5. Circular progress (github.com/castorflex).
6. Joda Time (Time utilities).
7. Recycler view animator.
8. Calligraphy (Custom font).

# Feature Planning
1. Read news feed (xml) from 3 categories of jagat review and its siblings. *(done)*

   a. JagatReview: http://jagatreview.com/feed  
   b. JagatPlay: http://jagatplay.com/feed/  
   c. JagatOC: http://jagatoc.com/feed  
   
2. Read news details. 

   * Using the feed. (Simplest solution) *(done)*
   * Parsing html from website. (More complex solution but more complete news)
   
3. Sharing news. *(done)*
4. Search news. 
5. Comment (Using Facebook). 

## License

> Copyright 2015 rakawestu
>
> Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
>
> http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
