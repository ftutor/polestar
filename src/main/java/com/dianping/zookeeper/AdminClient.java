package com.dianping.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by fangyingming on 16/12/28.
 */
public class AdminClient implements Watcher{

    private static final Logger LOG = Logger.getLogger(AdminClient.class);
    private ZooKeeper zk;
    private String hostPort;
    private String serverId = Integer.toHexString(new Random().nextInt());

    public AdminClient(String hostPort) {
        this.hostPort = hostPort;
    }

    public void startZk() throws IOException {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        LOG.info(watchedEvent.toString()+", "+hostPort);
    }


    private void listState() throws KeeperException, InterruptedException {
        try{
            Stat stat =new Stat();
            byte masterData[] = zk.getData("/master", false, stat);
            Date startDate = new Date(stat.getCtime());
            System.out.println("Master:"+new String(masterData)+" since"+startDate);
        }catch (KeeperException.NoNodeException e){
            System.out.println("No master");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }


        System.out.println("workes: ");
        for(String w:zk.getChildren("/worker",false)) {

            byte data[] = zk.getData("/worker/" + w, false, null);
            String state = new String(data);
            System.out.println("\t"+w+":"+state);

        }

        System.out.println("Tasks: ");
        for(String t:zk.getChildren("/tasks",false)){
            System.out.println("\t"+t);
        }
    }

    public static void main(String[] args) throws Exception {
        AdminClient w = new AdminClient("127.0.0.1:2181");
        w.startZk();
        w.listState();
        Thread.sleep(30000);
    }
}
