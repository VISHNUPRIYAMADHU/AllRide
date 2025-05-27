# AllRide
Code challenge for Allride
# Bulk User Import System

This is a full-stack event-driven application that allows uploading a `.csv` file containing user data through a React frontend. The backend, built with Kotlin and Spring Boot, processes the uploaded file asynchronously using coroutines and channels. It parses the CSV data into `User` objects and stores them in memory.

## 📦 Tech Stack
 java version - 17.0.14
 nodejs version - 23.6.0

### 🔧 Backend
- Kotlin
- Spring Boot
- OpenCSV
- Kotlin Coroutines
- Gradle
- Event-driven architecture with channels

### 💻 Frontend (React)
- ReactJS
- fetch
- Bootstrap
- File upload component

### 📦 Project Structure

AllRide/
├── backend/
│   ├── src/
│   │   └── main/
│   │       └── kotlin/
│   │           └── com/
│   │               └── allride/
│   │                   ├── controller/
│   │                   │   └── UploadController.kt
│   │                   ├── event/
│   │                   │   └── FileUploadedEvent.kt
│   │                   ├── model/
│   │                   │   └── User.kt
│   │                   ├── publisher/
│   │                   │   └── EventPublisher.kt
│   │                   ├── subscriber/
│   │                   │   └── UserProcessor.kt
│   │                   ├── configuration/
│   │                   │   └── ChannelConfig.kt
│   │                   └── BulkImportApplication.kt
│   └── build.gradle.kts
│
├── frontend/
│   ├── public/
│   │   └── index.html
│   ├── src/
│   │   ├── components/
│   │   │   └── FileUploader.tsx
│   │   ├── App.tsx
│   │   └── index.tsx
│   └── package.json

## 📂 Backend Overview

### 🔄 Event-Driven Architecture

1. React frontend uploads a CSV file via POST `/api/upload`.
2. Backend saves the file to the `uploads/` folder.
3. A `FileUploadedEvent` is created and published via a Kotlin channel.
4. `UserProcessor` listens to the channel, processes the file asynchronously, and parses valid user records.
5. Parsed users are stored in memory (`MutableList<User>`).

### Architecture Diagram

React Frontend
    │
    ├── POST /api/upload (Multipart CSV)
    │
Backend: Spring Boot (Kotlin)
    │
    ├── Save file → uploads/
    ├── Publish FileUploadedEvent via Channel
    │
UserProcessor
    ├── Listen to Channel
    ├── Parse CSV rows
    ├── Store Users in memory

### Frontend Overview

- Users can upload a .csv file with drag-and-drop or file browser.
- Makes a multipart/form-data POST request to /api/upload.
- Displays success/failure messages based on response.

### 🧪 Sample CSV Format

id,firstName,lastName,email
1,Alice,Johnson,alice.johnson@example.com
2,Bob,Smith,bob.smith@example.com
3,Carol,White,carol.white@example.com

### You can find valid and invalid csv in ./data

### 🚀 Running the Project
 🔹 Clone the Repository
  https://github.com/VISHNUPRIYAMADHU/AllRide.git
  git checkout dev

  🔹 Start Backend
     cd backend
     gradle clean build
     gradle bootRun

  🔹 Start Frontend
     cd frontend
     npm install
     npm start


