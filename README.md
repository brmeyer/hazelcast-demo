# Hazelcast Demo

## org.threeriverdev.hazelcast.queue.demo

Demonstrates using Hazelcast queues to coordinate a task within a network of nodes.  Each node discovers the cluster
through multicast, then obtains and executes any available task from the distributed queue.  Naively assumes that
the "task" is simply a String that needs printed.  A task will be executed only once, regardless of how many times
it's added to the queue -- executed tasks are added to a distributed set.

To run:

	$ mvn clean install
	$ mvn exec:java -Dexec.mainClass="org.threeriverdev.hazelcast.queue.demo.HazelcastNode"
	[repeat the above to create additional nodes]
	$ mvn exec:java -Dexec.mainClass="org.threeriverdev.hazelcast.queue.demo.HazelcastDemo"