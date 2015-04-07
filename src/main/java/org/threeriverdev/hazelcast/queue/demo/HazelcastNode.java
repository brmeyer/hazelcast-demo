package org.threeriverdev.hazelcast.queue.demo;

import com.hazelcast.config.Config;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates using Hazelcast queues to coordinate a task within a network of nodes.  Each node discovers the cluster
 * through multicast, then obtains and executes any available task from the distributed queue.  Naively assumes that
 * the "task" is simply a String that needs printed.  A task will be executed only once, regardless of how many times
 * it's added to the queue -- executed tasks are added to a distributed set.
 *
 * @author Brett Meyer.
 */
public class HazelcastNode {

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

        IQueue<String> queue = hazelcastInstance.getQueue("DemoQueue");
        Set<String> set = hazelcastInstance.getSet("DemoSet");
        try {
            // Note that this is extremely naive.  We assume that this node will simply sit and wait until
            // 1.) it receives a task or 2.) it's killed.
            String task = queue.poll(999, TimeUnit.SECONDS);
            if (!set.contains(task)) {
                System.out.println(task);
                set.add(task);
            }
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }
}
