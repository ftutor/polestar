package com.dianping.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

/**
 * Created by fangyingming on 16/12/28.
 */
public class Master implements Watcher{

    private static boolean isLeader;
    private String serverId = Long.toString(new Random().nextLong());
    private ZooKeeper zk;
    private String hostPort;

    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    boolean checkMaster(){
        while(true) try {

            Stat stat = new Stat();
            byte data[] = zk.getData("/master", false, stat);
            isLeader = new String(data).equals(serverId);
            return true;
        } catch (KeeperException.NoNodeException e) {
            return false;
        } catch (KeeperException.ConnectionLossException e){

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    private void runForMaster(){
        while (true){
            try {
                zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader=true;
                break;
            }catch (KeeperException.NodeExistsException e){
                isLeader=false;
                break;
            }catch (KeeperException.ConnectionLossException e){

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
            if(checkMaster()) break;
        }
    }

    void startZk() {
        try {
            zk = new ZooKeeper(hostPort, 15000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);

    }

    private  AsyncCallback.StringCallback masterCreateCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String s, Object o, String s1) {
            switch (KeeperException.Code.get(i)){
                case CONNECTIONLOSS:
                    checkMaster_asy();
                    return;
                case OK:
                    isLeader=true;
                    break;
                default:
                    isLeader=false;
            }
            System.out.println("I am "+(isLeader?"":"not ") +" the leader");
        }
    };

    private AsyncCallback.DataCallback masterCheckCallBack = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
            switch (KeeperException.Code.get(i)){
                case CONNECTIONLOSS:
                    checkMaster_asy();
                    return;
                case NONODE:
                     runForMaster_asy(1);
                    return;
            }
        }
    };
    private void checkMaster_asy() {
        zk.getData("/master", false, masterCheckCallBack, null);
    }


    private void runForMaster_asy(int a) {

        try {
            zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, masterCreateCallback, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void stopZK() throws InterruptedException {
        zk.close();
    }
    public static void main(String[] args) throws InterruptedException {
        Master m = new Master("127.0.0.1:2181");
        m.startZk();
        //m.runForMaster();
        m.runForMaster_asy(1);
        if(isLeader){
            System.out.println("I am the leader");
            Thread.sleep(60000);
        }else {
            System.out.println("someone else is the leader");
        }


        m.stopZK();
    }
}
