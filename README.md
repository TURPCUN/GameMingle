# GameMingle - Board Game Social Network App

With the advancement of technology and people's increasing reliance on online friendships, establishing meaningful in-person connections has become more challenging. Online gaming with virtual friends can't fully replace the joy of sitting down with real friends to play board games over a cup of coffee. However, as our ability to make friends face-to-face diminishes, it's becoming increasingly difficult to meet and spend time with like-minded individuals who share our interests.

The GameMingle App aims to rekindle the experience of gathering with friends as in the past and even foster new friendships. Users can add their favorite board games, both the ones they love and own, through the app. They can then connect with other users interested in playing the same board game and schedule meetings to enjoy their favorite games together over a cup of coffee.

## Project Structure

### Firebase Authentication and Realtime Database

The fundamental APIs used in the development of the GameMingle application belong to the Firebase SDK. Firebase Realtime Database has been employed for storing data and user information. **Firebase Authentication** has been configured for user authentication processes, utilizing the sign-in method with email and password.

**Firebase Storage** has been incorporated later to avoid limiting the application's usage. The URI links for all images uploaded to Firebase Storage have been added to the respective tables in **Firebase Realtime Database**. **Picasso** and **Glide** libraries have been used to display these images in the application. Initially, the Picasso library was chosen, but due to performance issues it introduced, the application migrated to the Glide library. This change was made to ensure better performance and efficiency.

For the design of the application, the **Material 3** library has been utilized. Elements such as bottom navigation view, edit text, and extended floating action button from the Material Library have been incorporated into the application's designs. 

In the implementation, the **MVVM architecture** has been preferred architecturally. Fragments have been created, and operations on the fragments are facilitated by using View Models and **MutableLiveData** to ensure the flow of data. As a result, a strengthened distinction between application components has been established, and a clear boundary between the UI's state and business logic has been ensured.

**ViewBinding** has been utilized to programmatically access UI elements. This enables the secure retrieval and utilization of elements. Additionally, through the use of an appropriate classification and modular structure in the project overall, each fragment performs a specific task independently, ensuring their independent operation.

Adapters have been employed for **RecyclerView** components within the application. This allows for the organized and efficient listing of data. Furthermore, a general **AppViewModel** class has been created to manage shared states throughout the application. Objects such as Firebase Reference are instantiated here. On the other hand, the BaseFragment class within the application encompasses shared behaviors and features. This has reduced code repetition and assisted fragments in focusing on their specific tasks.

## Details

**Firebase integration** has been incorporated to facilitate user authentication and database operations. The Email/Password login method has been activated for authentication purposes. With the implementation of Firebase, the security of users' password information is ensured through encryption. User data is stored in two distinct structures within Firebase. In the authentication segment, only the essential email and password details required for user login are retained (passwords remain confidential). Within the Realtime Database, a table labeled "Users" has been established utilizing the unique user ID.  

The diagram of the Sign-In process in the GameMingle application is shown in Figure.
![Sign-in Process](https://github.com/TURPCUN/GameMingle/assets/26707748/6af410ae-2f5c-4f5a-9000-31eb661b25d6)

**The Glide library** is used to set the user's profile picture and all the images in the application. Below, in Figure, the code for importing the profileImageUrl from the user object into the imgProfile view using the Glide library is shown.
![Update User's Profile Picture](https://github.com/TURPCUN/GameMingle/assets/26707748/96c8eac0-d3c9-45f9-b98f-5cea034515eb)

**Search and Request a Game:** Users can view all games in the application's database alphabetically. They can also use the Search View to search for a specific game by its name. If users cannot find the desired game, they can click the Floating Action Button, colored yellow in the bottom right corner and belonging to the Material 3 library, to request the game from the Admin user by providing the game's name and a few details. Figure at the below shares screenshots of the Game Search process. Clicking on any game in the search screen redirects users to a detailed page with descriptions of the game. Users can add the game to their favorites or libraries from this page as well.

![Search and Request a Game](https://github.com/TURPCUN/GameMingle/assets/26707748/cb266be6-179a-48e4-a30a-fb4a465c348b)

**Home and Favorite Games Screen:**
There are two different areas where users can access their favorite games. One of them is the MyGamesFavouritesFragment, located on the Home screen, which shows only four of the user's favorite games. The purpose of this fragment is to provide a preview to the user on the home screen.
If the user clicks the "See All" button, they are directed to the AllFavouriteGamesFragment, where they can access all their favorite games. Screenshots from the Home screen to the Favorite Games removing stage and implementation of the ItemTouchHelper can be seen in the next two figures.
![Home screen and Favorite Games screen](https://github.com/TURPCUN/GameMingle/assets/26707748/e24238e9-2458-4754-b5b1-6c1dda22628d)

**ItemTouchHelper implementation:**
Apart from these features, the fragment incorporates the Swipe Left gesture functionality using ItemTouchHelper. When the user swipes the item to the left, it indicates their intention to remove the game from their favorites. A dialog prompt appears asking the user if they want to remove the game from their favorites. If the user confirms by selecting 'Yes,' the game is removed from the user's favorites and, if applicable, from their library, and the relevant fragment is updated. 

![ItemTouchHelper implementation](https://github.com/TURPCUN/GameMingle/assets/26707748/e90285e8-b337-4829-ae7e-aac3cb2fb187)

**Create Event:**
The Create Event screen allows the user to set up a new gathering with the board games they own. Here, the user enters details such as the event name, location, and date. The board games they own are listed under the “Select a board game” option. Users can also upload an image related with the event and game. In Figure, screenshots illustrating the process of selecting an image for an event are provided.

![Create Event](https://github.com/TURPCUN/GameMingle/assets/26707748/4986048a-4d02-49a4-840b-d9c3e3cbaa20)

The Datepicker and TimePicker components from the Material library are used for date and time selection. Constraints have been added to the DatePicker to prevent the user from selecting a past date. After the user selects a date and clicks OK, the TimePicker opens. In Figure 5.8, screenshots of the DatePicker and TimePicker are displayed.

![Date and Time Picker](https://github.com/TURPCUN/GameMingle/assets/26707748/b966dcbb-ebc6-49c4-adcf-58c50692631c)

**Event Participation:**
The Events screen includes a section where the user can view and participate in upcoming events, as well as view events they have created. The user can make their selection between 'Upcoming' and 'My Events' tabs to view the details.

![Event Participation](https://github.com/TURPCUN/GameMingle/assets/26707748/2d3fee54-4116-43d0-b46b-2c1237d1faae)

For a user to participate in an event, the owner of the event needs to approve the participation request. Therefore, until the event owner approves the participation request, the user will see "Waiting for approval" as their status in the bottom-right corner, and they will not have access to the chat room related to the event. Relevant screenshots are provided in Figure.

![Event Approval waiting](https://github.com/TURPCUN/GameMingle/assets/26707748/ac11570a-93c6-4a0a-b2ff-bbc5ecbbd648)

**Event Approval and Notifications:**
When a user sends a registration request to an event, it requires approval from the event organizer. The event organizer can accept or reject incoming requests from the respective event page. Once the event organizer approves a user's participation request, a notification is sent to inform the users that their participation requests for the event have been accepted. Figure below displays a screenshot of the dashboard where the event organizer can manage users' participation requests. 

![Event Approval](https://github.com/TURPCUN/GameMingle/assets/26707748/f8eebac8-a7d7-4837-9c8a-c159aed9847a)

**Event Chat and Reporting:**
Having a chat room for each event provides users with an opportunity to discuss details and engage with each other about the event. Only approved users can access this chat room. To ensure the security of the chat room, a reporting feature has been added. Here, users can report a message and the sender by swiping left if they encounter any inappropriate content, and these reports are then reviewed through the Admin Dashboard.
In Figure below, a screenshot of the Chat screen and the message reporting feature is presented. Users cannot report their own messages, but they can report messages from other users.

![Event Chat](https://github.com/TURPCUN/GameMingle/assets/26707748/b9f0917b-18d2-4026-ba64-5632ebbf2de0)

# FUTURE WORK
In future developments of the application, a friend list feature could be added to user profiles, ensuring that the friendships formed through the app become more enduring. Additionally, by adding a sharing feature for events, it would be possible for created events to reach a broader audience. Firebase Push Notification functionality is planned to be incorporated for the chat feature of the application. The current in-app notification feature reflects notifications within the app. Real-time notifications could encourage users to be more active in the application. Furthermore, to expand the application's scope, a tutorial page about games could also be added, providing users with the opportunity to learn how to play a game they are interested in. 

# INSTALLATION GUIDE
The GameMingle Application is designed for devices running the Android operating system. It is recommended to be used on devices with a minimum SDK of 24, as the application has been developed with Android Studio Flamingo | 2022.2.1 Patch 2 version. Therefore, it is advisable to use the same version during the execution of the application in Android Studio to avoid potential errors arising from version differences.

Firebase Authentication, Firebase Realtime Database, and Firebase Storage SDKs utilize configurations in the google-services.json file. The google-services.json file contains configuration settings specific to the Firebase project, including private information related to the project. This file includes API keys, service credentials, and other configuration settings necessary for the application to interact with Firebase services.
Due to the sensitivity of this information, it is not recommended to openly share these details. Therefore, in the GameMingle repository on Github, this section has been added to the gitignore file to prevent it from being pushed. To ensure the success of automation processes defined for code build on Github, encrypted organizational secrets have been added in the organizational secrets section on Github.

For the project to run Firebase services on Android Studio, configuration through Firebase is necessary, and the generated google-services.json file must be added to the project.

Clone the project using the following command:

```bash
git clone https://github.com/kullaniciadi/proje.git
```

## Contact

If you have any questions or feedback regarding my project, please feel free to reach out to me:

- Email: [neslihantrpc@gmail.com](mailto:neslihantrpc@gmail.com)

## License

This project is licensed under the MIT License.

When using the project's code, please don't forget to give attribution.





