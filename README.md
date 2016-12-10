# IGGY
Based on ImageGallery by <a href="https://github.com/lawloretienne/ImageGallery">lawloretienne </a>, it is a easy to implement library to create a gallery to host an array of images

You can add one or more images to the gallery

Support for using <a href="https://developer.android.com/intl/zh-cn/reference/android/support/v7/graphics/Palette.html">Palette</a> to set the background color

Palette color types
 - VIBRANT
 - LIGHT_VIBRANT
 - DARK_VIBRANT
 - MUTED
 - LIGHT_MUTED
 - DARK_MUTED

Supports pinch-to-zoom on the images

ImageGalleryActivity           |  FullScreenImageGallery
:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/ayoprez/IGGY/master/images/image_gallery.png)  |  ![](https://raw.githubusercontent.com//ayoprez/IGGY/master/images/full_screen.png)

## Setup

#### Gradle

`compile 'com.ayoprez:iggy:0.1.0'`

#### Maven
```
<dependency>
    <groupId>com.ayoprez</groupId>
    <artifactId>iggy</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Sample Usage

```java
Intent intent = new Intent(MainActivity.this, ImageGalleryActivity.class);

String[] images = getResources().getStringArray(R.array.unsplash_images);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImageGalleryActivity.KEY_IMAGES, new ArrayList<>(Arrays.asList(images)));
        bundle.putString(ImageGalleryActivity.KEY_TITLE, "Unsplash Images");
        bundle.putBoolean(FullScreenImageGalleryActivity.KEY_DOWNLOAD_BUTTON, true);
        bundle.putBoolean(FullScreenImageGalleryActivity.KEY_SHARE_BUTTON, true);

        intent.putExtras(bundle);

startActivity(intent);
```


If you want to use the ImageGalleryActivity you must declare the following in your AndroidManifest.xml .

```xml

<!-- Declare this activity in your AndroidManfest.xml -->
<activity
    android:name="com.ayoprez.iggy.library.activities.ImageGalleryActivity"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:label=""
    android:theme="@style/ImageGalleryTheme" />
```

Alternatively, you can use the ImageGalleryFragment and host the fragment in your own Activity. In that case you can do something like this in an activity:

```java
Fragment fragment = getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (fragment == null) {
            fragment = ImageGalleryFragment.newInstance(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, fragment, "")
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .attach(fragment)
                    .commit();
        }
```

If you want to implement the palette to the images, you should use the PaletteHelper class:

```java
Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();

PaletteHelper.applyPalette(bitmap, PaletteColorType.VIBRANT, bgLinearLayout);
```


### Important Note
You must set up image loading by implementing these interfaces `ImageGalleryAdapter.ImageThumbnailLoader` and `FullScreenImageGalleryAdapter.FullScreenImageLoader`. See https://github.com/ayoprez/IGGY/blob/master/sample/src/main/java/com/ayoprez/iggy/activities/MainActivity.java .


## Developed By

* Ayoze Pérez
 
&nbsp;&nbsp;&nbsp;**Email** - arezrod@gmail.com

&nbsp;&nbsp;&nbsp;**Blog** - https://medium.com/@ayoprez

&nbsp;&nbsp;&nbsp;**Website** - http://ayoprez.com

###Inspired in

<a href="https://github.com/lawloretienne/ImageGallery">ImageGallery</a> by <a href="https://github.com/lawloretienne">Etienne Lawlor</a>

## License

```
Copyright 2016 Ayoze Pérez

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
