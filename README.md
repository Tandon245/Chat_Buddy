# Chat Buddy

Chat Buddy is a real-time chat application built for Android. This app allows users to connect, chat, and interact with each other using Firebase for backend services. 

## Features

- **User Authentication**: Secure login and registration using Firebase Authentication.
- **Real-time Messaging**: Send and receive messages instantly.
- **Chat Rooms**: Separate chat rooms for each user connection.
- **Send Button Activation**: Automatically enables the send button when there's text in the input field.
- **Keyboard Handling**: Adjusts the chat view to keep the latest message visible when the keyboard appears.

## Screenshots


## Getting Started

### Prerequisites

1. Android Studio installed.
2. A Firebase account.

### Setup Firebase

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new Firebase project.
3. Add an Android app to your project with the package name `com.example.chatbuddy`.
4. Download the `google-services.json` file and place it in the `app/` directory of your project.
5. Enable **Firebase Authentication** (Email/Password) in the Firebase Console.
6. Enable **Firebase Realtime Database** and set its rules to public for testing:
   ```json
   {
     "rules": {
       ".read": "auth != null",
       ".write": "auth != null"
     }
   }


### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Tandon245/Chat_Buddy.git
