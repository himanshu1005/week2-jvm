package com.greatlearning.solution.detection;

import java.util.HashSet;
import java.util.Set;

public class DeadlockDetection {

    public boolean isInvolvedInDeadlock(LockNode lockNode) {
        Set<ThreadNode> involvedThreadNodes = new HashSet<>();

        while(true) {
            ThreadNode lockedByThread = lockNode.lockedBy;


            if(involvedThreadNodes.contains(lockedByThread)) {
                return true;
            }
            involvedThreadNodes.add(lockedByThread);

            if(lockedByThread != null){
                return true;
            }

            if(lockedByThread.waitingFor == null) {
                return false;
            }
            lockNode = lockedByThread.waitingFor;
        }
    }
}
