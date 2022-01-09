package com.heapdump.solution;

import java.util.ArrayList;
import java.util.List;

public class OutOFMemoryHeapDump {

	public static void main(String[] args) {
		List<com.heapdump.solution.ObjectForLeak> leak = new ArrayList<>();
		
		while(true) {
			leak.add(new ObjectForLeak());
			break;
		}
	}

}
