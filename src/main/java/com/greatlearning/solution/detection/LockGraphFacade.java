package com.greatlearning.solution.detection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockGraphFacade {

    private volatile DeadlockDetection deadlockDetection = new DeadlockDetection();

    private volatile Map<Thread, ThreadNode> threadNodes = new HashMap<>();
    private volatile Map<Lock, LockNode>     lockNodes   = new HashMap<>();

    public synchronized boolean tryLock(Lock lock) {
        Thread     currentThread = Thread.currentThread();
        return tryLock(lock, currentThread);
    }

    public synchronized boolean tryLock(Lock lock, Thread currentThread) {
        System.out.println(currentThread.getName() + " tryLock()...");
        LockNode lockNode = getOrCreateLockNode(lock);

        ThreadNode threadNode    = getOrCreateThreadNode(currentThread);

        if(lockNode.lockedBy == threadNode) {
            return true;
        }
        if(lockNode.lockedBy == null) {
            lockNode.lockedBy = threadNode;
            lock.lock();
            return true;
        }

        threadNode.waitingFor = lockNode;

        if(this.deadlockDetection.isInvolvedInDeadlock(lockNode)){
            threadNode.waitingFor = null;
            return false;
        }

        // Lock is locked by another thread, but is not part of a deadlock,
        // so it is safe to wait to lock the Lock at a later time.
        try {
            lock.tryLock(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadNode.waitingFor = null;
        lockNode.lockedBy = threadNode;
        return true;
    }

    public synchronized boolean unlock(Lock lock) {
        ReentrantLock reentrantLock = (ReentrantLock) lock;
        if(reentrantLock.isHeldByCurrentThread()){
            lock.unlock();
            LockNode lockNode = getOrCreateLockNode(lock);
            lockNode.lockedBy = null;
            System.out.println(Thread.currentThread().getName() + " - Unlock successful");
            return true;
        }else{
            System.out.println("Failed to unlock");
            return false;
        }

    }

    private LockNode getOrCreateLockNode(Lock lock) {
        LockNode lockNode = this.lockNodes.get(lock);
        if(lockNode == null) {
            lockNode = new LockNode();
            this.lockNodes.put(lock, lockNode);
        }
        return lockNode;
    }

    private ThreadNode getOrCreateThreadNode(Thread thread) {
        ThreadNode threadNode = this.threadNodes.get(thread);
        if(threadNode == null) {
            threadNode = new ThreadNode();
            threadNode.thread = thread;
            this.threadNodes.put(thread, threadNode);
        }
        return threadNode;
    }

}
