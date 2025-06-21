# Digital-Logic-Lab

![Java](https://img.shields.io/badge/Java-17-orange) ![JavaFX](https://img.shields.io/badge/JavaFX-17-blue) ![Maven](https://img.shields.io/badge/Maven-3.8-red)

<!-- Add a compelling GIF or screenshot of the application's CPU simulator here -->
<p align="center">
  <img src="https://i.imgur.com/gK1L7v7.png" alt="CPU Simulator Screenshot" width="800"/>
</p>

An interactive desktop application built with **JavaFX** to simulate and visualize fundamental digital logic circuits, from basic latches to a fully functional 4-bit CPU. This project was developed as a hands-on tool to solidify concepts learned in digital design courses, providing a dynamic way to see abstract theories in action.

## Project Goal

The primary goal of this "Digital Logic Lab" is to bridge the gap between theoretical knowledge and practical understanding. Instead of just reading about how a flip-flop works or what the fetch-decode-execute cycle is, this simulator allows users to **see it, click it, and interact with it**. It's an educational tool designed for students, hobbyists, and anyone curious about the building blocks of modern computers.

## ✨ Features

The simulator is organized into a modular lab environment where each module represents a different digital component or application.

### 🧠 Sequential Logic Circuits (Latches & Flip-Flops)
Visualize the core principles of digital memory and state management.
- **SR, Clocked D, Master-Slave D-FF:** Explore the behavior of fundamental memory units.
- **T & JK Flip-Flops:** Interact with the most common flip-flops, complete with **dynamic timing diagrams** to visualize signal changes over time.

### ⚡️ Combinational Logic Circuits
Interact with circuits that produce an output based on current inputs, with no memory.
- **Multiplexer (MUX), Decoder, Encoder:** See how data is selected and transformed.
- **Full Adder & 4-Bit Adder:** Understand the basics of binary arithmetic from the ground up.
- **4-Bit Comparator:** A circuit that compares two binary numbers.

### ⚙️ Practical Applications
See how basic components are combined to create more complex and meaningful systems.
- **4-Bit Up/Down Counter:** A synchronous counter built from flip-flops, with its output shown on a **7-segment display**.
- **Shift Register:** Visualize how data bits are "shifted" across a series of flip-flops.

### 🚀 The Capstone Project: 4-Bit CPU Simulator
The highlight of the lab, a fully animated Von Neumann architecture CPU that conceptually integrates many of the other components.
- **Interactive Memory:** Load and **edit** your own simple 4-bit programs and data directly in the UI.
- **Animated Fetch-Decode-Execute Cycle:** Each phase of the CPU's operation is visualized with highlighted components and data paths.
- **Register & Flag Visualization:** Watch the Program Counter, Accumulator, and other registers change in real-time. Status flags like **Zero (Z)** and **Carry (C)** are also visualized.
- **Mnemonic Display:** The simulator automatically translates 8-bit machine code into human-readable assembly mnemonics (e.g., `LDA E`, `ADD F`).
- **Full Execution Control:** `Animate Step` through the program instruction by instruction, or `Run` it automatically at an adjustable speed.

## 🚀 Getting Started

You have two options to run the Interactive Digital Logic Lab on your machine.

### Option 1: Run the Installer (Recommended for Windows)
Download the pre-packaged installer. This does not require you to have Java installed on your system.

1.  Go to the [**Releases**](https://github.com/ulukayalperen7/Digital-Logic-Lab/releases) page of this repository.
2.  Download the `Digital-Logic-Simulator-[version].exe` file.
3.  Run the installer. A shortcut will be created on your desktop and in the Start Menu.

### Option 2: Run from Source (For Developers)
If you want to run the project from the source code, you will need:
- Java JDK 17 or higher
- Apache Maven

1.  Clone the repository:
    ```bash
    git clone https://github.com/ulukayalperen7/Digital-Logic-Lab.git
    ```
2.  Navigate to the project directory:
    ```bash
    cd Digital-Logic-Lab
    ```
3.  Compile and run the application using Maven:
    ```bash
    mvn clean javafx:run
    ```

## 🛠️ Building the Installer from Source

If you have made changes and want to create a new `.exe` installer, run the following Maven command:
```bash
mvn clean package
```
This will compile the code, create a fat JAR, and then use `jpackage` to generate a native installer in the `jpackage-output/` directory.

## 💻 Tech Stack
- **Language:** Java 17
- **Framework:** JavaFX 17
- **Build Tool:** Apache Maven
- **Packaging:** `maven-shade-plugin` & `jpackage-maven-plugin`