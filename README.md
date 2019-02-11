## Synopsis

This is a Twilio Voice App that written in Java. The app takes the caller number, convert it from international to national format and uses it to query the caller segment against Oracle DB. 
The result is then played with the TTS (text to speech) to the caller


## Installation

1. Clone the project from GitHub
2. create an .env file with oracle credentials
3. Use gradle to run it


## Used Libraries

Spark Framework - for the web serer
ojdbc8 - oracle library in order to connect to Oracle DB
Libphonenumber - for internation to national number conversion
java-dotenv - for the .env file parsing


## Tests

Describe and show how to run the tests with code examples.

## License

Free for use