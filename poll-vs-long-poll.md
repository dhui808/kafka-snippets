### Poll vs Long Poll
	Regular poll: the client gets an empty response immediately if the server does not have the data.
	
 	Long poll: the client does not gets a response immediately if the server does not have the data. 
 	The server holds the request and waits for information to become available, after which a complete response is 
	finally sent to the client.

	From the client's perspective, it's basically the same. It sends a request, and at some point it gets a response. 
 	With long-polling it may take a little longer.

	If the connection times out, the clients of both regular poll and long poll need to handle it and poll again.

 	Long poll saves the time for re-estabilshing connection in regular poll scheme and reduces the ammount that needs
  	to be sent back to the client.
  
