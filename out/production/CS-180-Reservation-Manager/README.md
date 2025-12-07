# Concert Reservation System
## L30-Team 4
## Team Members
- Saanvi Verma
- Kunj Arora
- Arav Nair
- Shalini Murthula

## Compilation and Running

### This project must run on a local IDE

### To compile all files:
```
javac Account.java
javac Seat.java
javac SeatingChart.java
javac Reservations.java
javac Database.java
javac Client.java
javac Server.java
javac Payment.java
javac PaymentManager.java
javac Concert.java
javac AccountInterface.java
javac SeatInterface.java
javac SeatingChartInterface.java
javac ReservationsInterface.java
javac DatabaseInterface.java
javac ClientInterface.java
javac ServerInterface.java
javac PaymentInterface.java
javac PaymentManagerInterface.java
javac ConcertInterface.java
javac AccountTest.java
javac SeatTest.java
javac SeatingChartTest.java
javac ReservationsTest.java
javac DatabaseTest.java
javac PaymentTest.java
javac PaymentManagerTest.java
javac ClientTest.java
javac ServerTest.java
javac Concert.java
javac seats.txt
javac concert.txt
javac accounts.txt
javac reservations.txt
javac Concert1.txt
javac Concert2.txt
javac Concert3.txt
javac Concert4.txt
javac Concert5.txt
javac Concert6.txt
javac Concert7.txt
javac Concert8.txt
javac Concert9.txt
javac Concert10.txt
javac Concert11.txt
javac Concert12.txt
javac Concert13.txt
javac Concert14.txt
javac Concert15.txt
```

### To run all tests:
```
java DatabaseTest
java AccountTest
java SeatTest
java SeatingChartTest
java ReservationsTest
java PaymentManagerTest
java PaymentTest
java ClientTest
java ServerTest
java ConcertTest
```

## GTA and Submission Information
- When trying to submit on Vocareum, cloning the Github was not working as I was not able to sign in properly so I (Saanvi Verma) uploaded the files instead. One of the TA's told me to add our GTA to our Github repository to ensure that they could see our files.
- Saanvi Verma will submit the video presentation on Brightspace.
- Shalini Murthula will submit the report on Brightspace.

## Testing Information
- Different test cases have their own imports however they all have been written using JUnit 5.
- All test cases were verified locally using JUnit 5, even though Vocareum’s compilation logs show missing symbol errors due to its arrangement.

## Running Information
- To run the program, you must first run the Server and then the Client.

## Submission Information (Phase 3)
- Saanvi Verma submitted Phase 3 on Vocareum by uploading the files, worked on solving checkstyle errors for every class, fixed Phase 2 errors, worked on Client, Account, Concert, Server, the presentation, and the Report.
- Arav Nair worked on ClientTest and the Report.
- Kunj Arora worked on Client and the Report.
- Shalini Murthula worked on the Report, made the script, and the presentation.

## Submission Information (Phase 2)
- Saanvi Verma submitted Phase 2 on Vocareum by uploading the files, worked on solving checkstyle errors for every class, worked on Concert, ConcertInterface, Database, DatabaseInterface, DatabaseTest, Client, ClientInterface, seats.txt, Reservations, Seat, Account, Server, ServerTest, and ServerInterface.
- Arav Nair worked on Payment, PaymentManager, PaymentManagerInterface, PaymentInterface, PaymentTest, PaymentManagerTest, Database, and Reservations.
- Kunj Arora worked on Server, ServerInterface, DatabaseTest, Reservations, ReservationsTest, and Database.
- Shalini Murthula worked on ClientTest, Client, Concert, Server, ServerTest, DatabaseTest, AccountTest, ReservationsTest, ConcertTest, and Database.

## Submission Information (Phase 1)
- Saanvi Verma submitted Phase 1 on Vocareum by uploading the files, worked on solving checkstyle errors for every class, worked on Account, AccountInterface, AccountTest, Database, DatabaseInterface, and DatabaseTest.
- Kunj Arora worked on Seat, SeatInterface, SeatingChart, SeatingChartInterface, and Database.
- Arav Nair worked on Database, Reservations, ReservationsInterface, and ReservationsTest.
- Shalini Murthula worked on AccountTest, DatabaseTest, SeatTest, and SeatingChartTest.


---

## Class Descriptions

### Database.java
**Purpose:** 
Main database manager that handles all the data storage and retrieval (in text files) for the concert reservation system. Manages four text files: accounts.txt, seats.txt, reservations.txt, concert.txt. Thread safety has been implemented within Database.java. Database is responsible for reading from and writing to files, while SeatingChart manages seats in memory.


**Key Functionality:**
- **Account Management:**
  - 'createAccount()' - Creates new account for the user with a unique ID.
  - 'loginIntoAccount()' - Validates username and password so the user can login.
  - 'deleteAccount()' - Deletes the user's account through their ID, username, and password.
  - 'getAccount()' - Retrieves the user's account.

- **Reservation Management:**
  - 'createReservation()' - Books seats for a concert and generates a unique reservation ID each time.
  - 'cancelReservation()' - Cancels the reservation and frees up seats used for the concert.
  - 'getReservationsByAccount()' - Retrieves all reservations for a user through their ID.
  - 'getReservationByID()' - Finds the user's specific reservation by their reservation ID.

- **Concert Management:**
  - 'createConcert()' - Creates a new concert with a unique ID.
  - 'getAllConcerts()' - Retrieves all the concerts in the file.
  - 'getTime()' - Gets a concert time by unique concert ID.

- **Seat Management (Private Helpers):**
  - 'isSeatAvailable()' - Checks if a seat is available to book.
  - 'updateSeatAvailability()' - Updates seat availability status based on the parameter.

- **Seat Management**
  - 'getSeat()' - Retrieves seat by the seat ID.

**Testing Done:**
- Tested account creation with duplicate username prevention
- Tested login with correct/incorrect credentials
- Tested account deletion
- Tested reservation creation with seat availability checking
- Tested reservation cancellation and seat release
- Tested data persistence across program restarts
- Tested concert creation and ability to get all concerts
- Tested getting concert time

### Relationship to the Other Classes
- Uses 'Account' class to represent and store user data
- Uses 'Seat' class to read/write seat information from/to file
- Uses 'Reservations' class to store booking records
- Works alongside 'SeatingChart' class (Database handles file I/O, SeatingChart handles in-memory operations)
- Reads from and writes to three text files for data persistence
- 'Server' class uses different Database methods depending on the user choice

---

### Client.java
**Purpose:** 
Client class that communicates with a user (through the GUI) and handles all the different commands per menu. Ensures that the user enters an appropriate answer per question and communicates with the Server class for confirmation on actions taken by the user. 

### Fields
- 'boolean run' - To confirm if the Client is running
- 'Socket socket' - Socket
- 'BufferedReader reader' - Reader to communicate with the Server
- 'PrintWriter writer' - Writer to communicate with the Server
- 'Scanner scanner' - Scanner to read in the user's responses
- 'String account' - Current account being used
- 'String host' - Host name (used localhost for testing)
- 'int port' - Port number (used 6767 for testing)

### GUI Panels
- 'mainPanel' - Contains all the panels
- 'loginPanel' - Logs the user in
- 'menuPanel' - Contains menu options
- 'reservationListPanel' - Contains all the reservations that the user has
- 'makeReservationPanel' - Allows the user to make a reservation by selecting the different concerts
- 'reservationPanel' - Allows the user to book seats in the reservation
- 'addConcertPanel' - Adds a concert

### Key Methods
- 'start()' - Starts the menu options for the user, communicates with the 'Server' class, and ensures that the user enters an appropriate answer
- Getters: 'getAccount()', 'getPort()', and 'getHost()'
- Setters: 'setPort()' and 'setHost()'

**Testing Done:**
- Tested Client and Server Connection
- Tested Constructor and setter methods
- Tested the ability to connect and disconnect
- Tested login, account creation, and account deletion
- Tested making a reservation, canceling a reservation, and viewing a reservation
- Tested login failure, account create failure, and account deletion failure
- Tested making a reservation failure
- Tested disconnecting in the middle of the session

### Relationship to the Other Classes
- Communicates with the 'Server' class to confirm user choices

---

### Server.java
**Purpose:** 
Server class communicates with the Client class and uses the Database and PaymentManager to handle all the different commands given by the Client class. Server has been threaded properly.

### Fields
- 'Database database' - Database to call different functions
- 'PaymentManager paymentManager' - PaymentManager to ensure payment per reservation

### Key Methods
- 'handleClient(Socket socket)' - Reads user's commands and utilizes the user's information to call the Database and communicate with the Client class accordingly.

**Testing Done:**
- Tested Client and Server Connection
- Tested Constructor
- Tested the ability to connect and disconnect
- Tested login, account creation, and account deletion
- Tested making a reservation, canceling a reservation, and viewing a reservation
- Tested disconnecting in the middle of the session

### Relationship to the Other Classes
- Communicates with the 'Client' class to confirm user choices
- Uses the 'PaymentManager' class to confirm the payment for every reservation made
- Uses the 'Database' class to get needed information for each user choice

---

## Concert.java

### Description
Represents every concert that users can go to. Each concert has a unique ID generated upon creation and a unique seating class associated with every concert to validate avaliable seating.

### Fields
- 'String name' - Concert's name
- 'String date' - Date of the concert
- 'String time' - Time of the concert
- 'int ID' - Unique for concert (auto-generated upon creation)

### Key Methods
- 'writingInFile()' - Converts concert to comma-separated string for file storage
- Constructor 'Concert(String line)' - Makes a concert object from file string
- Getters: 'getName()', 'getDate()', 'getTime()', and 'getID()'

### Testing Done
**Unit Tests:**
- Concert creation with all valid fields
- File string conversion: `concert.toString()` produces correct comma-separated format
- File reconstruction: `new Concert(line)` correctly parses all fields from string
- All getters return correct values after creation
- Concert reconstruction preserves all data

### File Format
Format: 'name,date,time,ID'

Example:
```
Taylor Swift,15/12/2016,6:30PM,1
Eminen,03/12/2025,8:25PM,2
```

### Relationship to Other Classes
- Used by 'Database' for concert management and persistence
- Implements 'ConcertInterface'
- Used in 'Server' and 'Client' to add concerts and identify what seat was booked per concert
- Stored in concert.txt

---

## ConcertInterface.java

### Description
Defines the format that the Concert class.

### Required Methods
- 'String getName()'
- 'String getDate()'
- 'String getTime()'
- 'int getID()'
- 'void setName(String)'
- 'void setDate(String)'
- 'void setTime(String)'
- 'void setID(int)'
- 'void writingInFile()'
- 'String toString()'

### Testing Done
- Verified Concert class correctly implements ConcertInterface
- Verified all method signatures match interface declarations
- Verified return types are correct

---

## Payment.java

### Description
Represents payment for a reservation including reservation ID and amount. Each payment has a unique reservation ID and amount associated with every reservation to validate the reservation.

### Fields
- 'int reservationID' - Reservation ID
- 'double amount' - Price amount of reservation

### Key Methods
- Getters: 'getReservationID()' and 'getAmount()'

### Testing Done
**Unit Tests:**
- Payment creation with all valid fields
- All getters return correct values after creation
- Negative or 0 values for amount
- Ensuring Reservation IDs are stored correctly

### Relationship to Other Classes
- Used by 'Server' fand 'Database' to confirm reservation booking
- Implements 'PaymentInterface'

---

## PaymentInterface.java

### Description
Defines the format that the Payment class.

### Required Methods
- 'int getReservationID()'
- 'double getAmount()'

### Testing Done
- Verified Payment class correctly implements PaymentInterface
- Verified all method signatures match interface declarations
- Verified return types are correct

---

## PaymentManager.java

### Description
Represents the processing and refunding of payments for reservations. Contains a list of the different payments.

### Fields
- 'List<Payment> payments' - List of Payments

### Key Methods
- 'processPayment()' - Processes the payment and adds to the list
- 'refundPayment()' - Processes the refund and removes the payment from the list
- 'getPaymentAmount()' - Retrieves the payment amount by the reservation ID

### Testing Done
**Unit Tests:**
- Retrieving payment amount by reservation ID
- Attempting to refund a payment
- Attempting to make an invalid payment
- Attempting to make a payment
- Retrieving all payments

### Relationship to Other Classes
- Used by 'Server' to validate payment
- Implements 'PaymentManagerInterface'

---

## PaymentManagerInterface.java

### Description
Defines the format that the PaymentManager class.

### Required Methods
- 'boolean processPayment(int, double)'
- 'boolean refundPayment(int)'
- 'double getPaymentAmount(int)'

### Testing Done
- Verified PaymentManager class correctly implements PaymentManagerInterface
- Verified all method signatures match interface declarations
- Verified return types are correct

---

## Account.java

### Description
Represents a customer account with personal information and login credentials. Each account has a unique ID generated upon creation and a unique username validated during account creation.

### Fields
- 'String firstName' - User's first name
- 'String lastName' - User's last name
- 'int age' - User's age
- 'String userName' - Unique username for login (validated in Database)
- 'String password' - User's password
- 'String email' - User's email address
- 'String phoneNumber' - User's phone number
- 'String accountID' - Unique account identifier (auto-generated)

### Key Methods
- 'createID()' - Generates unique account ID based on current timestamp or counter
- 'writingInFile()' - Converts account to comma-separated string for file storage
- Constructor 'Account(String line)' - Makes an account object from file string
- Getters: 'getFirstName()', 'getLastName()', 'getAge()', 'getUsername()', 'getPassword()', 'getEmail()', 'getPhoneNumber()', 'getID()'

### Testing Done
**Unit Tests:**
- Account creation with all valid fields
- File string conversion: `account.toString()` produces correct comma-separated format
- File reconstruction: `new Account(line)` correctly parses all fields from string
- ID generation creates unique IDs matching user's information
- All getters return correct values after creation
- Account reconstruction preserves all data

### File Format
Format: 'firstName,lastName,age,userName,password,email,phoneNumber,ID'

Example:
```
John,Doe,25,johndoe,password123,john@email.com,555-1234,1
Jane,Smith,30,janesmith,pass456,jane@email.com,555-5678,2
```

### Relationship to Other Classes
- Used by 'Database' for account management and persistence
- Implements 'AccountInterface'
- Used in 'Reservations' to identify who made a booking
- Stored in accounts.txt

---

## AccountInterface.java

### Description
Defines the format that the Account class.

### Required Methods
- 'String getFirstName()'
- 'String getLastName()'
- 'int getAge()'
- 'String getUsername()'
- 'String getPassword()'
- 'String getEmail()'
- 'String getPhoneNumber()'
- 'String getID()'
- 'String toString()'

### Testing Done
- Verified Account class correctly implements AccountInterface
- Verified all method signatures match interface declarations
- Verified return types are correct

---

## Seat.java

### Description
Represents a single seat in the concert venue with pricing, location, and availability information. Seats can be serialized for file storage and reconstructed from strings in the file.

### Fields
- 'String seatID' - Unique identifier for the seat (e.g., "SEAT001")
- 'String row' - Row identifier (e.g., "A", "B", "VIP")
- 'int number' - Seat number within the row
- 'double price' - Cost of the seat
- 'boolean isAvailable' - Decided whether the seat can be booked (true = available, false = booked)

### Key Methods
- 'writingInFile()' - Converts seat to file format string (seatID,row,isAvailable,number,price)
- 'setAvailable(boolean)' - Updates availability status
- 'setPrice(double)' - Updates seat price
- Constructor 'Seat(String seatID, String row, int number, double price)' - Creates new seat
- Constructor 'Seat(String line)' - Makes a seat from a string in file
- Getters: 'getSeatID()', 'getRow()', 'getNumber()', 'getPrice()', 'isAvailable()'

### Testing Done
**Unit Tests:**
- Seat creation with all fields properly initialized
- Availability changing (true → false → true)
- Price updates and retrieval
- File string conversion using `writingInFile()`
- Seat reconstruction from file string
- Verification that new seats default to available (isAvailable = true)

### File Format
Format: 'seatID,row,isAvailable,number,price'

Example:
```
SEAT001,A,true,1,50.0
SEAT002,A,false,2,50.0
```

### Relationship to Other Classes
- Used by 'Database' for reading/writing seat data to file
- Used by 'SeatingChart' for in-memory seat management
- Referenced in 'Reservations' by seatID
- Implements 'SeatInterface' and 'Serializable'
- Stored in seats.txt

---

## SeatInterface.java

### Description
Defines the format for Seat class.

### Required Methods
- 'String getSeatID()'
- 'String getRow()'
- 'int getNumber()'
- 'double getPrice()'
- 'boolean isAvailable()'
- 'void setAvailable(boolean)'
- 'void setPrice(double)'

### Testing Done
- Verified Seat class implements all interface methods
- Verified method return types and parameters match declarations

---

## SeatingChart.java

### Description
Manages a collection of seats in memory. Provides a faster access to seat information and handles seat reservation/cancellation operations. Works alongside the Database class.

### Fields
- 'List<Seat> seats' - ArrayList storing all seats in the seating chart

### Key Methods
- 'addSeat(Seat seat)' – Adds a seat to the seating chart
- 'getSeat(String seatID)' – Retrieves a specific seat by ID from memory
- 'reserveSeat(String seatID)' – Marks a seat as unavailable (books it)
- 'cancelSeat(String seatID)' – Marks a seat as available (frees it)
- 'getAllSeats()' – Returns all seats in the chart
- 'getAvailableSeats()' – Returns only seats that are currently available

### Testing Done
**Unit Tests:**
- Retrieving seats by ID
- Reserving available seats
- Attempting to reserve an already-reserved seat
- Canceling reserved seats
- Attempting to cancel already-available seats
- Getting all seats
- Filtering available seats correctly under mixed or all-reserved conditions

### Relationship to Other Classes
- Uses 'Seat' objects to store seat information in memory
- Implements 'SeatingChartInterface'
- Works alongside 'Database'
- Will be used by Server in Phase 2 for fast seat lookups

---

## SeatingChartInterface.java

### Description
Defines the format for SeatingChart class.

### Required Methods
- 'void addSeat(Seat seat)'
- 'Seat getSeat(String seatID)'
- 'boolean reserveSeat(String seatID)'
- 'boolean cancelSeat(String seatID)'
- 'List<Seat> getAllSeats()'
- 'List<Seat> getAvailableSeats()'

### Testing Done
- Verified SeatingChart implements all interface methods
- Verified method signatures match declarations

---

## Reservations.java

### Description
Represents a booking made by a user for specific seats at a concert show. Links an account to seats for a specific event date and time. Auto-generates unique reservation IDs.

### Fields
- 'int reservationID' - Unique booking identifier (auto-generated)
- 'String userID' - ID of user who made reservation
- 'String showID' - ID of the concert/show being booked
- 'List<String> seatIDs' - List of booked seat IDs
- 'String date' - Concert date
- 'String time' - Concert time
- 'double totalPrice' - Total cost of all seats in reservation
- 'int numSeats' - Total number of seats in reservation

### Key Methods
- 'toString()' - Converts reservation to file format string
- Constructor 'Reservations(Account account, String showID, List<String> seatIDs, String date, String time, double totalPrice)' - Creates new reservation with auto-generated ID
- Constructor 'Reservations(String line)' - Makes a reservation from a string in the file
- Getters: 'getReservationID()', 'getUserID()', 'getShowID()', 'getSeatIDs()', 'getDate()', 'getTime()', 'getTotalPrice()', 'getNumSeats()'

### Testing Done
**Unit Tests:**
- Reservation creation with single seat
- Reservation creation with multiple seats
- Auto-generation of unique reservation IDs
- File string conversion with proper formatting
- Reconstruction from file string preserves all data
- List of seat IDs properly stored and retrieved
- Date and time formats validated

### File Format
Format: 'reservationID,userID,showID,seatID1|seatID2|seatID3,date,time,totalPrice'

Example:
```
1,1,SHOW001,SEAT001|SEAT002,2024-12-15,19:00,100.0
2,2,SHOW002,SEAT003,2024-12-20,20:00,75.0
```

Note: Multiple seat IDs are separated by |.

### Relationship to Other Classes
- Uses 'Account' object to get userID (accountID) for new reservations
- References 'Seat' objects by storing their seat IDs
- Used by 'Database' for reservation management and to write/read files
- Implements 'ReservationsInterface'
- Stored in reservations.txt

---

## ReservationsInterface.java

### Description
Defines the format for Reservations class.

### Required Methods
- 'int getReservationID()'
- 'String getUserID()'
- 'String getShowID()'
- 'List<String> getSeatIDs()'
- 'int getNumSeats()'
- 'String getDate()'
- 'String getTime()'
- 'double getTotalPrice()'
- 'void setUserID(String userID)'
- 'void setShowID(String showID)'
- 'void setSeatIDs(List<String> seatIDs)'
- 'void setDate(String date)'
- 'void setTime(String time)'
- 'void setTotalPrice(double totalPrice)'

### Testing Done
- Verified Reservations class implements all interface methods
- Verified return types match declarations

---

## Data Files

### accounts.txt
Stores all user account information, one account per line in comma-separated format.

**Format:** 'firstName,lastName,age,userName,password,email,phoneNumber,accountID'

**Example:**
```
Emma,Thompson,28,emmaT28,Pass!word123,emma.thompson@email.com,5551237890,TOMM-890
Liam,Nguyen,35,liamN35,securePass!,liam.nguyen@gmail.com,4085552233,NUIA-2233
Sofia,Ramirez,17,sofiR17,blueSky2025,sofia.ramirez@yahoo.com,9175559988,RMOI-9988
```

### seats.txt
Stores all seat information for the venue, one seat per line. Managed by Database for file operations and SeatingChart for in-memory operations.

**Format:** 'seatID,row,isAvailable,number,price'

**Example:**
```
SEAT001,A,true,1,50.0
SEAT002,A,false,2,50.0
SEAT003,B,true,1,75.0
SEAT004,VIP,true,1,150.0
```

### reservations.txt
Stores all booking information, one reservation per line. Multiple seat IDs are |-separated.

**Format:** 'reservationID,accountID,showID,seatIDs(|-separated),date,time,totalPrice'

**Example:**
```
1,1,SHOW001,SEAT001|SEAT002,2024-12-15,19:00,100.0
2,2,SHOW002,SEAT003,2024-12-20,20:00,75.0
3,1,SHOW001,SEAT004,2024-12-25,18:00,150.0
```

### concert.txt
Stores all concert information, one concert per line.

**Format:** 'name,date,time,ID'

**Example:**
```
Taylor Swift,12/12/2025,19:00,5
Kendrick Lamar,26/03/2015,20:00,6
Billie,13/06/2030,18:00,7
```

---

## Overview

### Class Responsibilities

**Database Class:**
- Handles all file I/O operations (reading/writing)
- Persists data to text files
- Manages accounts, reservations, seat, concert file operations
- Provides methods for data retrieval and storage

**SeatingChart Class:**
- Manages seats for fast access
- Handles seat reservations and cancellations
- Provides quick lookups for available seats
- Works alongside Database

**PaymentManager Class:**
- Manages payments for fast access
- Works alongside Server

**Client Class:**
- Uses GUI to talk with the user
- Provides information to the server for every operation
- Works alongside Server

**Server Class:**
- Provides information to the client based on the operation
- Implements Runnable allowing for multiple threads
- Works alongside Database

**Account, Seat, Reservations, Concert, Payment Classes:**
- Data models representing entities
- Handle their own serialization (toString)
- Provide getters/setters for fields

**Interface Classes:**
- Define contracts for implementations
- Ensure consistent method signatures
- Allow for future flexibility

---

## Testing Strategy

### Test Files
- 'DatabaseTest.java' - Tests all database file operations
- 'AccountTest.java' - Tests account creation and methods
- 'SeatTest.java' - Tests seat functionality
- 'ReservationsTest.java' - Tests reservation operations
- 'SeatingChartTest.java' - Tests seat management
- 'PaymentTest.java' - Tests payment functionality
- 'PaymentManagerTest.java' - Tests payment management
- 'ClientTest.java' - Tests client operations and functionality (Contains GUI Tests)
- 'ServerTest.java' - Tests server opterations
- 'ConcertTest.java' - Tests concert creation and methods

### Test Coverage
- **Account operations:** Create, login, delete, retrieve
- **Seat operations:** Availability checking and updating
- **Reservation operations:** Create, cancel, retrieve by user, retrieve by ID
- **Concert operations:** Create, retrieve all concerts, retrieve ID
- **Client operations:** Communicate with user, connect with Server, performs proper tasks per command from GUI
- **Server operations:** Connect with Client, performs proper tasks per command, utlizes Database
- **Edge cases:** Duplicate usernames, non-existent accounts, unavailable seats (will be handled in Phase 2).
- **Data persistence:** Verify data survives program restarts

### Running All Tests
```
java org.junit.runner.JUnitCore DatabaseTest
java org.junit.runner.JUnitCore AccountTest
java org.junit.runner.JUnitCore SeatTest
java org.junit.runner.JUnitCore ReservationsTest
java org.junit.runner.JUnitCore ConcertTest
java org.junit.runner.JUnitCore ServerTest
java org.junit.runner.JUnitCore ClientTest
java org.junit.runner.JUnitCore PaymentTest
java org.junit.runner.JUnitCore PaymentManagerTest
java org.junit.runner.JUnitCore SeatingChartTest
```

---

## Notes
- All data is stored in plain text files.
- Database uses file I/O for all operations to access data.
- All classes have to have an interface.
- Client is communicates with the user through the GUI.
