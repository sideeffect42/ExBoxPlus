# ExBox+

**NOTE:** This code is probably only interesting to students of [ZHAW](https://www.zhaw.ch/) taking the ADS course.

ExBox+ is an improved version of the "ExBox" application provided.


### Improvements in ExBox+
- Includes class-templates as `abstract` classes that can be extended.
- Includes a GNUmakefile to get started in no time
- Allows building with gcj into a native binary (for people on non-HotSpot systems)
- Application can be provided with command line options for faster turnaround times
  (use `-help` for documentation)
- Supports dynamic classpath extension (you can put your solutions anywhere).
  Simply select the `*.class` file and ExBox+ will take care of adjusting the classpath accordingly.
- **Dynamic class reloading.**
  Make a change to your solution, run `javac` on it and tell ExBox+ to reload the server.
  No need to restart ExBox+ just to test the change.


### Getting started

**NOTE:** ExBox+ should work just fine on unixoide operating systems. Windows should work when GNUmake and Java are installed and in PATH.

After cloning this repo, just run `make` (or `make native` to use GCJ) to compile ExBox+.

When compiled you can run ExBox+ with `make run`.

To compile your solution files together with ExBox+, set the `SRCDIR` environment variable,
e.g. `SRCDIR='/path/to/your/solutions' make`

To provide command line arguments to ExBox+, use e.g. `make run args='-help'`.


### Issues

If something doesn't work as you expect it -> Pull requests welcome!
