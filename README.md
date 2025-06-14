# NMR Mouse Simulator

This project contains a simple Java program that demonstrates the working
principles of a large NMR mouse applied to newly laid asphalt. The simulation
models the magnetic field of the permanent magnet, generates NMR T1/T2 signals
based on water content and visualizes the resulting signal.
The simulation also includes a small finite element solver demonstrating magnetic potential calculations on a 2D grid.


## Building and running

The project uses Maven. Dependencies are provided offline in `/usr/share/maven-repo`.
To compile the code and run tests execute:

```bash
mvn -Dmaven.repo.local=/usr/share/maven-repo -o -q test
```

To run the simulation after building:

```bash
mvn -Dmaven.repo.local=/usr/share/maven-repo -o -q exec:java
```

If run in a graphical environment, a window will display the simulated signal.
In headless mode a textual preview of the signal is printed instead.
