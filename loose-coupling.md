When tightly coupled, both servera dn clien applications are required to be available at the same time.  
With loose coupling, it is okay if the receiving server is down when the sending server sendss the message,  
and it is okay if the sending server is down or unable to respond when the receiving server reads the message.  
Loose coupling provides true messaging independence. Reducing the requirements for simultaneous availability  
reduces complexity and can improve overall availability.
