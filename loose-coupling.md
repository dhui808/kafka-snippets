When tightly coupled, both servera dn clien applications are required to be available at the same time.  
With loose coupling, it is okay if the receiving server is down when the sending server sendss the message,  
and it is okay if the sending server is down or unable to respond when the receiving server reads the message.  
Loose coupling provides true messaging independence. Reducing the requirements for simultaneous availability  
reduces complexity and can improve overall availability.

Loose coupling makes sense only if Kafka is introduced between the client and the server:  
![client-kafka-server](images/kafka-1.png)  

It does not make sense if Kafka is used to decouple two microservices:  
![client-ms-kafka-server](images/kafka-2.png)  
