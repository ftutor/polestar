package com.dianping.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Random;

/**
 * Created by fangyingming on 16/12/28.
 */
public class Worker implements Watcher{

    private static final Logger LOG = Logger.getLogger(Worker.class);
    private ZooKeeper zk;
    private String hostPort;
    private String serverId = Integer.toHexString(new Random().nextInt());

    public Worker(String hostPort) {
        this.hostPort = hostPort;
    }

    public void startZk() throws IOException {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        LOG.info(watchedEvent.toString()+", "+hostPort);
    }

    public void register() {
        zk.create("/worker/worker-" + serverId, "Idle".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createWorkerCallback,null);
    }

    AsyncCallback.StringCallback createWorkerCallback= new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String s, Object o, String s1) {
            switch (KeeperException.Code.get(i)){
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    LOG.info("regist sucessfully:" + serverId);
                    break;
                default:
                    LOG.error("something went wrong");

            }
        }
    };


    public static void main(String[] args) throws Exception {
        Worker w = new Worker("127.0.0.1:2181");
        w.startZk();
        w.register();
        Thread.sleep(30000);
    }
}
