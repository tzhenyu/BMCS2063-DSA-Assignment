# TARUMT Clinic System

BMCS2063 Data Structures and Algorithms, Assignment 202601.
Programme: Bachelor in Data Science, Year 2 Semester 2, Tutorial Group 1 (RDS2S2G1). Tutor: Dr. Wong Yoke Seng.

A console prototype of a clinic management system. Everything is stored in one custom collection ADT (`ListADT`). The Java Collections Framework is not used for storage.

## Team

| Member | Student ID | Module |
| :-- | :-- | :-- |
| Chieng Yuan Jye | 24WMR12095 | Patient |
| Jonathan Ho Yoon Choon | 24WMR14220 | Staff |
| Mohamed Mahir Bin Habibullah | 24WMR14240 | Appointment |
| Tan Zhen Yu | 24WMR13353 | Medicine |

The team component (ADT specification and implementation) is the `ListADT` package.

> Note: Teammates contributed their tasks and modules, but Tan Zhen Yu merged all contributions into this repository on GitHub, so the commit history shows him as the committer.

## The ADT: `ListADT<T>`

A circular singly linked list with a dummy node. It implements `ListADTInterface<T>` (which extends `Iterable<T>`).

| Operation | What it does |
| :-- | :-- |
| `insert(T)` | Add an element to the end |
| `remove(T)` | Remove the first occurrence, returns `boolean` |
| `getEntry(int)` | Get the element at a position, `null` if the index is invalid |
| `search(T)` | Linear search, returns the stored object or `null` |
| `earlyExitSearch(T)` | Linear search that stops early (list must be sorted) |
| `binarySearch(T)` | Binary search (list must be sorted) |
| `sort()` | Merge sort by natural ordering (`Comparable`) |
| `sort(Comparator<T>)` | Merge sort by a custom column |
| `size()`, `isEmpty()`, `clear()`, `display()` | Utility operations |

Full pre/postconditions are in the Team Report (section 1.1).

## Modules

| Module | Features |
| :-- | :-- |
| Patient | Add (auto ID, IC or passport validation), search, update, delete, display (sort by ID or name), insurance eligibility check, export medical report |
| Staff | Add staff or doctor (auto ID), view all (sort by ID or name), update, delete, search doctor by medical registration number |
| Appointment | Book (auto ID, slot capacity check), display, sort by ID, check slot availability by date, show latest appointments (stack) |
| Medicine | Add, delete (paged list), view with pagination (sort by name, description, quantity, registration number, expiry date or last updated), search by name, description or registration number |

## Architecture (ECB)

```
src/
├── Main.java
├── ListADT/          ListADT, ListADTInterface, Node
├── Clinic/
│   ├── Boundary/     *Boundary  console UI, talks to Control only
│   ├── Control/      *Controller  business logic
│   └── Entity/       Patient, Staff, Doctor, Appointment, Medicine, Availability
├── StorageHandler/   FileHandler, CSVHandler  (CSV read/write)
└── *.csv             staff, patient, appointment, medicine data
```

## How to run

### Prebuilt jar

Download `DSAAssignment-bundle.zip` from the [v1.0.0 release](https://github.com/tzhenyu/BMCS2063-DSA-Assignment/releases/tag/v1.0.0), unzip it, and run from the unzipped folder (the one containing `DSAAssignment.jar` and `src/`):

```bash
java -jar DSAAssignment.jar
```

Requires **Java 17 or newer**. Check with `java -version`. On Java 8 you will get `UnsupportedClassVersionError`. The code uses `switch` rules, so it cannot be built for Java 8. The program reads `src/*.csv` relative to the working directory, so run it from the unzipped folder.

### From source

Requirements: JDK 17 or newer. The NetBeans project is set to `javac.source=25`, so use JDK 25 there or change the source/target in project properties.

**NetBeans:** File > Open Project > select this folder, then Run (F6). Main class is `Main`.

**Command line:** run from the project root. The controllers read data from the relative path `src/*.csv`, so the working directory must be the project root.

```bash
mkdir -p out && javac -d out $(find src -name "*.java")
```

```bash
java -cp out Main
```

### What `Main` runs

`Main` starts the modules one after another. Each menu runs until you exit it (option `0`), then the next one starts:

1. Staff
2. Patient
3. Appointment

The Medicine module is currently commented out in `Main.java`. Uncomment `medicineBoundary.start()` to run it.

## Data files

Data is loaded from CSV files in `src/` (semicolon-separated) and saved back when a module saves or exits.

| File | Contents |
| :-- | :-- |
| `patient.csv` | Patient records |
| `staff.csv` | Staff and doctor records |
| `appointment.csv` | Appointment bookings |
| `medicine.csv` | Medicine stock |
| `report_<PatientID>.txt` | Generated medical report (output) |
