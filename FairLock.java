package org.source.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tw on 2017/10/25.
 * 公平锁，按线程进来的顺序，依次放到等待链表中，每次获得锁的都是链表中的第一个，也就是最先进来的线程
 */
public class FairLock {
    private boolean isLocked = false;
    private Thread lockingThread = null;
    private List<QueueObject> waitingThreads =
            new ArrayList<QueueObject>();

    public void lock() throws InterruptedException{
        QueueObject queueObject = new QueueObject();
        boolean isLockedForThisThread = true;
        synchronized (this) {
            waitingThreads.add(queueObject);
        }

        while(isLockedForThisThread) {
            synchronized (this) {
                isLockedForThisThread =
                        isLocked || waitingThreads.get(0) != queueObject;
                if(!isLockedForThisThread) {
                    isLocked = true;
                    waitingThreads.remove(queueObject);
                    lockingThread = Thread.currentThread();
                    return;
                }
            }
            try{
                queueObject.doWait();
            }catch(InterruptedException e) {
                synchronized (this) {
                    waitingThreads.remove(queueObject);
                }
                throw e;
            }
        }
    }

    public synchronized void unlock() {
        if(this.lockingThread != Thread.currentThread()) {
            throw new IllegalMonitorStateException("Calling thread has not locked this lock");
        }
        isLocked = false;
        lockingThread = null;
        if(waitingThreads.size() > 0) {
            waitingThreads.get(0).doNotify();
        }
    }


    public static class QueueObject{
        private boolean isNotified = false;

        public synchronized void doWait() throws InterruptedException {
            while(!isNotified)
                this.wait();

            this.isNotified = false;
        }

        public synchronized void doNotify() {
            this.isNotified = true;
            this.notify();
        }

        public boolean equals(Object o) {
            return this == o;
        }
    }
}
