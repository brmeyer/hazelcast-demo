package org.threeriverdev.hazelcast.queue.demo;

import com.hazelcast.config.Config;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Demonstrates using Hazelcast queues to coordinate a task within a network of nodes.  Each node discovers the cluster
 * through multicast, then obtains and executes any available task from the distributed queue.  Naively assumes that
 * the "task" is simply a String that needs printed.
 *
 * Note that, theoretically, this could simply use hazelcast-client.  However, it appears the client does not currently
 * support multicast discovery.  To keep things simple, without having to know addresses ahead of time, I'm creating
 * another node.
 *
 * @author Brett Meyer.
 */
public class HazelcastDemo {

    public static void main(String[] args) {
        Config hazelcastConfig = new Config();

        // In case we're starting this multiple times on the same machine...
        hazelcastConfig.getNetworkConfig().setPortAutoIncrement(true);

        // Enable and configure multicast.
        MulticastConfig multicastConfig = hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig();
        multicastConfig.setEnabled(true);
        multicastConfig.setMulticastGroup("224.2.2.3");
        multicastConfig.setMulticastPort(54327);

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(hazelcastConfig);

        hazelcastInstance.getQueue("DemoQueue").add("We are started!");
    }
}
