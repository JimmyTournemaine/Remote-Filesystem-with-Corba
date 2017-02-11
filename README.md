# Remote Filesystem with CORBA

## Requirements  
  
  - JDK 1.6
  - JacORB
  - Apache Ant
  
  *Remark*. These applications needs to be correctly set as well as their environment variables.
  
## Installation

The application sources are available on GitHub and compilable with Ant.
  
	$ git clone https://http://github.com/JimmyTournemaine/Remote-Filesystem-with-Corba
	$ ant compile
  
## Project composition
  
The project is composed by 4 programs :

  - **files.Serveur** The server-side program.
  - **files.console.Console** A client which simulate a console and offers files-manipulating commands. 
  - **files.ui.directory.ClientUI** A client which offers the same features as the console client but has a graphic user interface.
  - **admin.ShutdownClient** A client with *administrator privileges*. Its role is to shutdown the server remotely and properly.
  
## Test case
  
  Test case are implemented in **files.test.Test** and test all the application features including exceptions cases.
  These tests are runnable using `ant run-test`.
  
  *Remark*. To launch tests, set the `junit` property in **build.xml** with the correct path to your **junit.jar**.
  
## Usage
  
	$ jaco files.Serveur &
	$ jaco files.ui.directory.ClientUI # or files.console.Console
	$ jaco admin.ShutdownClient # or fg then ctrl+C
  
To simplify the usage of the application, we wrote two scripts to easily run each client :

- **./run_console.sh** To run the console client.
- **./run_ui.sh** To run the GUI client.
 
*Warning*. These scripts clean the workspace before launch programs, meaning that files previously handled will be deleted.

