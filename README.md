# Recommender Systems
## COMP3208: Social Computing Techniques
---
## Contents

- **[Introduction](#introduction)**
  * **[Task Description](#task-description)**
  * **[Project Contents](#project-contents)**
- **[Running the Application](#running-the-application)**

---

## Introduction

### Task Description

- Two datasets were given:
  - A set of predicted and ground truth user-ratings (10,000 entries each).
  - A set of user-ratings (100,000 entries, split into training and testing sets with a 90:10 split).
- Three tasks were set:
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

> All code is extensivley documented. In particular, the algorithms used for the two recommender system implementations are explained in detail.

- **Directories**:
  - `General` : Code used across the whole project.
  - `IBCFRecommender` : The implementation of the item-based collaborative filtering recommender.
  - `MFRecommender` : The implementation of the matrix factorisation recommender.
  - `Tasks` : Individual files/scripts for each of the tasks. These files make use of the project code (e.g., the relevant recommender system) to actually "carry out" the corrresponding task.
  - `Tools` : Non-specific tools used across the whole project.
-  **Files**:
    - `App.java` : Program used to execute the code for each of the project's tasks (i.e., calls the relevant task file).
    - `Test.java` : Program used to test the performance of the recommender systems developed for the courswork.

---

## Running the Application

- Only `App.java` and `Test.java` are runnable.
- `App.java` can be used to carry out the tasks.
- `Test.java` can be used to evaluate the performance of the recommender systems using the dataset of user-ratings.

---
