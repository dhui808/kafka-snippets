### Publish/Subscribe Pattern vs Message Queue Pattern
![Pub-Sub](images/pub-sub.png)  

![Queue](images/queue.png)  

	If your system requires broadcasting messages to multiple recipients or you're building an event-driven architecture, 
	pub/sub might be the better choice. 
 
 	If you need to ensure that each message is processed by exactly one consumer, or you require FIFO ordering, then 
  	message queues are likely the way to go.

	Kafka is more of a publish/subscribe product, though it is still possible to use it as a queue.

 	Kafka does not use a queue underneath, but a log, an order sequence of elements.
	Lack of per‑message lifecycle management. When Kafka removes messages, it always happens in batches, and it's completely 
 	detached from consumers activity.
  
	Kafka allows automatic creation of topics
