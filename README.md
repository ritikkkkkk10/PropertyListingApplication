# PropertyListingApplication
Property Listing Application
Overview
The Property Listing Application is an Android app designed to help users browse and manage property listings. The app integrates with Firebase for real-time data updates and user authentication. Users can view a list of properties, search for specific properties, and mark their favorite properties for quick access.

Features
Browse Properties: View a list of properties with details including name, location, price, and description.
Search Functionality: Search for properties based on name, location, or price.
Favorites: Mark properties as favorites and view them in a dedicated favorites section.
Real-Time Updates: Get real-time updates on property listings from Firebase.
User Authentication: Sign in and sign out using Firebase Authentication.
Prerequisites
Android Studio: Ensure you have Android Studio installed for development and testing.
Firebase Account: Set up a Firebase project and configure it for your Android app.
Dependencies: Make sure to include the necessary Firebase and AndroidX dependencies in your build.gradle files.
Setup
1. Firebase Setup
   Create a Firebase Project:

Go to the Firebase Console.
Create a new project or use an existing one.
Add Firebase to Your Android App:

Download the google-services.json file from Firebase and place it in the app directory of your project.
Add the Firebase SDK dependencies to your build.gradle files.
2. Project Configuration
   Clone the Repository:

bash
Copy code
git clone <repository-url>
cd <repository-directory>
Open the Project in Android Studio:

Open Android Studio and select "Open an existing project."
Navigate to the project directory and open it.
Sync Gradle:

Click "Sync Now" in the notification bar or use File > Sync Project with Gradle Files.
Build and Run:

Connect an Android device or use an emulator.
Build and run the application from Android Studio.
Usage
Main Features
Home Screen:

The Home screen displays a list of all available properties. You can scroll through the list to view property details.
Search Functionality:

Use the search bar to filter properties based on name, location, or price. As you type, the list updates to show matching properties.
Favorites:

Mark properties as favorites by clicking the heart icon (assuming this feature is integrated in your PropertyAdapter).
Navigate to the Favorites tab to view a list of properties you've marked as favorites. This list is dynamically updated based on the IDs stored in SharedPreferences.
User Authentication:

Sign in using your Firebase Authentication setup. Click the logout button in the toolbar to sign out.
User Interface Components
PropertyListActivity: Displays the list of properties with search functionality.
FavoritesFragment: Shows the list of favorite properties. Fetches favorites from SharedPreferences and displays them.
HomeFragment: Displays the main screen with the list of properties.
ActivityMainBinding: Manages the main activity layout with tabs and view pager.
Troubleshooting
Firebase Authentication Issues:

Ensure that Firebase Authentication is correctly set up and that you have the right configuration in google-services.json.
Empty Favorites List:

Check that your SharedPreferences key matches ("favorites_list") and that the IDs are correctly stored and retrieved.
Search Not Working:

Verify that the search functionality correctly filters properties based on the search query.

