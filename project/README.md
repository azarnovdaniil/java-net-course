<h2>Remote repository</h2>
This is a small project of console input client-server remote repository, that allows:
* file storage on server side;
* remote repository file list view;
* file uploading/downloading with no file length restrictions;
* length-based check of file transmission;
* file name occupied check with rewrite confirmation;
* file managing (rename, delete) in remote repository;
* multiple user connection with separate repositories;
* settings configure with separate config file;
* settings configure by console input on client side;

<h4>The project uses the following stacks:</h4>
* Java Core
* Maven 
* Netty (client-server connection, own encoding)
* Concurrent (thread management)
* SQL (authorisation using MySql data base)
* Log4j
