# Inner Loop
## Description
Inner Loop is a social networking app for microblogging, enabling users to share updates and engage with a global community. 

## Screenshots
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/manavgambhir/Inner-Loop/assets/97420824/c681b4df-5e47-43ac-b6fa-e4e3e43bfab1" alt="LoginScreen" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/manavgambhir/Inner-Loop/assets/97420824/65ac7af9-f9be-4e3f-8bc1-ac0b9d7e4ff3" alt="RegisterScreen" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/manavgambhir/Inner-Loop/assets/97420824/472222d8-39bf-4791-a6df-ba7109f906d6" alt="HomeScreen" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/manavgambhir/Inner-Loop/assets/97420824/322f010b-ceb3-42f8-8ad9-ad6d131fcb97" alt="SearchScreen" width="250"/>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/manavgambhir/Inner-Loop/assets/97420824/ec68ea4b-59d2-4dd4-8d77-16db5b8a70f8" alt="ProfileScreen" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/manavgambhir/Inner-Loop/assets/97420824/fea2883a-d233-4092-90de-5579cbdecd48" alt="ProfileScreen" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/manavgambhir/Inner-Loop/assets/97420824/d9b55289-b365-4b40-867e-04588a6c460d" alt="ProfileScreen" width="250"/>
    </td>
    <td align="center">
      <img src="https://github.com/manavgambhir/Inner-Loop/assets/97420824/c50c55de-1074-4759-b151-87798d0a72a8" alt="ProfileScreen" width="250"/>
    </td>
  </tr>
</table>

## Features
- Inner Loop leverages Jetpack Compose, the modern UI toolkit for building Android apps, to create dynamic and interactive user interfaces with ease.
- Firebase Authentication providing secure and seamless user authentication through email/password authentication.
- Firebase Realtime Database is used to store and sync user data in real-time, enabling instant updates across devices and ensuring a smooth and responsive user experience.
- Firebase Storage is used for efficient and scalable storage of user-generated content, including post images and profile pictures.
- User can also view other user profiles and follow or unfollow them.
- (UPCOMMING FEATURE) User can like other users post.

# Cloning and Using the Inner Loop App

You can clone the Inner Loop app repository from GitHub and set it up on your local machine for testing and development. Follow these steps to get started:

## Clone the Repository
Open your terminal or command prompt and run the following command to clone the repository:

```bash
git clone https://github.com/YourUsername/Inner-Loop.git
```

Replace YourUsername with your GitHub username.

## Navigate to the Project Directory
Change your current directory to the project folder:
```bash
cd Inner-Loop
```

## Install Dependencies
Run the following command to install all the project dependencies:
```bash
./gradlew build
```

## Set Up Firebase
Create a new Firebase project and Register your app with the correct android package name(Your package name is generally the applicationId in your app-level build.gradle file). </br>
Download the google-services.json file and move it to your module (app-level) root directory.

## Running the App
Use Android Studio to open the project, or you can run the app using the command:
```bash
./gradlew run
```
