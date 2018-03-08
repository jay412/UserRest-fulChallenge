# UserRest-fulChallenge
An Android app that exercises a REST-ful API. All API calls are namespaced by a candidate parameter, which must be included in all requests.

# Functionality
Home Page:
- The home screen shows a list of all users (i.e. the response to a GET to /user). 
- The default candidate parameter(c2366) is filled in the editable text field.
- You can retrieve a different user list by specifying a different Candidate parameter in the text field.
- On the top right corner, there is a REFRESH button that refreshes the page and calls another GET response to get updated API information.
- The ADD button directs you to another page where you can create a new user by providing input in all of the fields.

New User Page:
- Fill the text field under "Name:" to specify the new user's name
- Fill the text field under "Email:" to specify the new user's email
- Fill the text field under "Candidate:" to specify the new user's candidate parameter
- Once all fields are filled, click the "CREATE NEW USER" button to create a new user.
- Tap the back arrow on the top right corner to go back to the home page.

# Assumptions
1) User's device has Wifi enabled --> The home page will display an error message if user tries to refresh with an internet connection
  - Adding a new user will also not work
2) The challenge does not request for any functionality regarding Users making Transfers which will manipulate their balance
  - It also does not request for any delete functionality regarding Users
3) The ability to create a new user is made in another page rather than the home page to account for long lists of users taking up too much space on the screen.

# Screenshots
![Alt text](/default-home-page.png?raw=true "Default Home Page")
![Alt text](/new-home-page.png?raw=true "New Home Page")
![Alt text](/create-new-user.png?raw=true "Create New User")
![Alt text](/show-new-user.png?raw=true "Show New User")
