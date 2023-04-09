# A Discord Chatbot Resume Scanner 
A Discord chatbot that allows users to verify how well their resumes will perform on online resume scanners, also known as Applicant Tracking Systems (ATS).

The chatbot is built using Java 20 and utilizes the JDA (Java Discord API) library to communicate with Discord. 

## Setup and Installation
To use the chatbot, you will need to have Java 20 and Maven installed on your system.

1. Clone the repository to your local machine using the command:
  `git clone https://github.com/lhunter3/ResuScan.git`

2. Navigate to the cloned directory and install the required packages by running:

3. Find `TOKEN` in the `ConnectBot.java` and replace with token from your Discord Developer Portal.

4. Open `run.bat` on a machine that has  a `Java 17` installation. The bot should now be running and ready to use on you're personal server.

## Usage
To use the bot, invite it to your Discord server and type !help to see a list of available commands. The main command to check your resume is !scan, which accepts a resume file attachment. The bot will then analyze the resume and provide a report indicating how well it will perform on an ATS.

##Contributing
Contributions to this project are welcome. If you would like to contribute, please fork the repository and submit a pull request with your changes. Please ensure that your code adheres to the Google Java Style Guide and includes tests where applicable.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
