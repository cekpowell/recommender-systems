# Recommender Systems
## COMP3208: Social Computing Techniques
---
## Contents

- **[Introduction](#introduction)**
  * **[Task Description](#task-description)**
  * **[Project Contents](#project-contents)**
- **[Running the Application](#running-the-application)**
- **[Usage](#usage)**
  * **[Performing Image Classification](#performing-image-classification)**

---

## Introduction

### Task Description

- Two datasets were given:
  - A set of predicted and ground truth user-ratings (10,000 entries each).
  - A set of user-ratings (100,000 entries, split into training and testing sets with a 90:10 split).
- The coursework contained three tasks:
  - **Task 1**: Evaluate the performance of the predicted ratings against the ground truth ratings, according to standard evaluation metrics: MAE, MSE, RMSE.
  - **Task 2**: Develop a **Collaborative Filtering** recommender system to produce ratings for the test split of the dataset of user-ratings (either item- or user-based).
    - *An item-based approach was taken, as it yeilded better resultss*.
  - **Task 3**: Develop a **Matrix Factorisation** recommender system to produce ratings for the test split of the dataset of user-ratings.

### Project Contents

#### Data

- Contained in the `data` directory is two directories:
  - `predictions` : The dataset of predicted and ground truth ratings.
  - `dataset` : The dataset of user ratings.

#### Source Code

- **Directories**:
  - `General` : Code used across the whole project.
  - `IBCFRecommender` : The implementation of the item-based collaborative filtering recommender.
  - `MFRecommender` : The implementation of the matrix factorisation recommender.
  - `Tasks` : Individual files/scripts for each of the tasks. These files make use of the project code (e.g., the relevant recommender system) to actually "carry out" the corrresponding task.
  - `Tools` : Non-specific tools used across the whole project.
-  **Files**:
  - `App.java` : Program used to execute the code for each of the project's tasks (i.e., calls the relevant task file).
  - `Test.java` : Program used to test individual project components (not to test the code against a test suite).

---

## Running the Application

- Only the `App.java` class is runnable.
- The classes `Task1.java`, `Task2.java`, `Task3.java` each have a `run()` method that 
- The `App.java` class defines a main method, which runs the code



- In the `App.java` main method, by default, these methods are all called.
- The results of evaluating the classifiers as well as general status messages are outputted to the console.
- The results of classifying the provided testing data with the classifiers are written to text files (`run1.txt`, `run2.txt` and `run3.txt`) as a list of pairs of `<image name> <classification>`.
- Calls to each of these methods can be removed/commetted out in order to evaluate and run individual classifiers.

---
## Usage

### Performing Image Classification

- All three of the implemented classifiers extend the `MyClassifier.java` class, which defines the basic structure and methods of all of the three classifiers.
- One of these methods is `makeGuesses()`:

```java
public ArrayList<Tuple<String, String>> makeGuesses(VFSListDataset<FImage> dataset) { ... }
```

- After a classifier instance has been instantiated with training data, the `makeGuesses()` method can be used to annotate a set of unlabelled images.
- The images are passed in as a `VFSListDataset`, and the method returns an `ArrayList` of `Tuple`s, where each tuple is a `String, String` pair, with the first element being the name of the image, and the second being the annotation of this image.

---

