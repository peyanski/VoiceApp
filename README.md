## Synopsis

This is a Twilio Voice App that written in Java. The app takes the caller number, convert it from international to national format and uses it to query the caller segment against Oracle DB. 
The result is then played with the TTS (text to speech) to the caller


## Installation

1. Clone the project from GitHub
2. create an .env file with oracle credentials
3. Use gradle to run it


## Used Libraries

Spark Framework - for the local web serer
ojdbc8 - oracle library in order to connect to Oracle DB
Libphonenumber - for international to national number conversion
java-dotenv - for the .env file parsing


## Tests

Several tests that checks for .env file and it's content are included

## License

Free for use