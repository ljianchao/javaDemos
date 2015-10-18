package cn.jc.Utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.junit.Test;

public class QueueTest {

	public static void printQueue(Queue<Integer> queue) {
		while (queue.peek() != null) {
			Integer currentInt = queue.remove();
			System.out.println(currentInt + "");
			
			if (currentInt == 1) {
				queue.offer(currentInt + 1);
			}
		}
		System.out.println();
	}
	
	@Test
	public void testLinkedList() {
		Queue<Integer> queue = new LinkedList<Integer>();
		Random random = new Random(47);
		for (int i = 0; i < 10; i++) {
			queue.offer(random.nextInt(i + 10));
		}
		
		printQueue(queue);
	}
}
