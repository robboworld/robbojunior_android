# robbojunior_android

RobboJunior is a fork of ScratchJr (https://github.com/LLK/scratchjr) project. 

## Building RobboJunior
To build the Android version, you need to have a system equipped with Android Studio.

Ensure you have node and npm [installed](http://blog.npmjs.org/post/85484771375/how-to-install-npm).

With all of the code checked out, you must install npm dependencies for bundling the JavaScript:
* <tt>npm install</tt>

The build caches .png files out of the .svg files to improve performance. To enable this build step, you need to install a few dependencies.

On Ubuntu:

* Run <tt>sudo easy_install pysvg</tt> to install python svg libraries. If you don't have easy_install installed on your system than you should install python-setuptools. Run <tt>sudo apt-get install python-setuptools </tt>
* Run <tt>sudo apt-get install librsvg2-bin</tt> to install rsvg-convert
* Run <tt>sudo apt-get install imagemagick</tt> to install ImageMagick

On OS X:

* Install [Homebrew](http://brew.sh).
* Run <tt>sudo easy_install pysvg</tt> to install python svg libraries
* Run <tt>brew install librsvg</tt> to install rsvg-convert
* Run <tt>brew install imagemagick</tt> to install ImageMagick

Once these are installed, select  the appropriate flavor/build variant in Android Studio. To build in Android Studio, open the project <tt>android/RobboJunior</tt>.
