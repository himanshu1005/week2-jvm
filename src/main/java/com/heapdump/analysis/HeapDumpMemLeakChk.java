package com.heapdump.analysis;

import com.heapdump.solution.ObjectForLeak;

import java.util.ArrayList;
import java.util.List;

public class HeapDumpMemLeakChk {

	public static void main(String[] args) {

		System.out.println("Inside main");
		List<com.heapdump.solution.ObjectForLeak> leak = new ArrayList<>();

		for (int i = 0; i < 1000000; i++) {
			leak.add(new ObjectForLeak());
		}

		System.out.println("Post Object creation to check Memory Leak");
	}

}
