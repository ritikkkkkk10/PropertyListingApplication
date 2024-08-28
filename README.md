Overview
This app allows users to view and manage property listings. Users can log in or register, view property details, add properties to their favorites, and if they are administrators, add new properties to the system. The app also features a search functionality to help users find properties based on their requirements.

Getting Started
Prerequisites
Android Studio: Ensure you have Android Studio installed on your computer.
Firebase Setup: Make sure you have set up Firebase for your project and added the necessary configuration files (google-services.json).
Setup
Clone the Repository:

bash
Copy code
git clone <repository-url>
Open the Project:

Launch Android Studio.
Select "Open an existing project."
Navigate to the directory where you cloned the repository and select it.
Add Required Dependencies:

Open build.gradle files (both project-level and app-level).
Add the required dependencies, such as Firebase and AndroidX libraries, to your build.gradle files.
Sync the project with Gradle files.
Build APK:

Go to Build > Build Bundle(s) / APK(s) > Build APK(s) in Android Studio.
Wait for the build process to complete.
Install the APK:

Once the APK is built, you can install it on an Android emulator or a physical Android device.
Usage
Launch the App:

Open the installed APK on your emulator or Android device.
Login or Register:

The first screen will prompt you to log in or register.
Choose the appropriate option based on whether you are a new user or an existing user.
Select User Type:

After logging in or registering, you will need to select whether you are an admin or a regular user.
Admin: You will have special access to add new properties.
Regular User: You will be able to view and manage properties but cannot add new ones.
Home Screen:

If you are an admin, you will see an option to add properties.
Click on the "Add Property" option and enter the required details to add a new property.
If you are not an admin, you will see an option to view all properties.
Click on "View Properties" to see the list of available properties.
View Property Details:

Click on a property from the list to view its details, including its description, price, location, and images.
You will have the option to add the property to your favorites.
Favorites:

Navigate to the "Favorites" tab in the main screen to view all the properties you have marked as favorites.
You can manage and view your favorite properties in this section.
Search Properties:

In the PropertyList Screen, use the search functionality to filter properties based on your requirements.
Enter search criteria such as property name, location, or price to find properties that match your needs.
Contributing
Contributions are welcome! Please follow the standard Git workflow (fork, create a branch, make changes, create a pull request) to contribute to the project.