package de.mgu.algorithms.adt.pq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.mgu.algorithms.adt.pq.BinaryHeap;
import de.mgu.algorithms.adt.pq.Entry;
import de.mgu.algorithms.adt.pq.IntegerComparator;
import de.mgu.algorithms.adt.pq.PriorityQueue;
import de.mgu.algorithms.adt.pq.UnderflowException;

public class BinaryHeapTest {
	
	private PriorityQueue<Integer, DummyNode> pq;
	
	@Before
	public void beforeEachTest() {
		pq = BinaryHeap.createPQ(new IntegerComparator());
	}
	
	@Test
	public void testCorrectOrderRandomInsert() {
		for (int i = 0; i < 50; i++) {
			assertTrue(verifyRandomInsertTestcase());
		}
	}
	
	@Test
	public void testCorrectOrderInverseInsert() {
		List<Integer> listOfNumbers = new LinkedList<Integer>();
		for (int i = 1000; i > 0; i--) {
			DummyNode n = new DummyNode();
			n.setDist(i);
			listOfNumbers.add(i);
			pq.insert(i, n);
		}
		
		assertTrue(verifyCorrectOrder(listOfNumbers, pq));
	}
	
	@Test
	public void testCorrectOrderDuplicateEntries() {
		List<Integer> listOfNumbers = new LinkedList<Integer>();
		for (int i = 1000; i > 0; i--) {
			int number = 1;
			if (i % 2 == 0)
				number = 1;
			else
				number = 2;
			DummyNode n = new DummyNode();
			listOfNumbers.add(number);
			n.setDist(number);
			pq.insert(number, n);
		}
		
		assertTrue(verifyCorrectOrder(listOfNumbers, pq));
	}
	
	@Test
	public void testMassDeletion() {
		List<Integer> listOfNumbers = new LinkedList<Integer>();
		for (int i = 0; i < 1000; i++) {
			listOfNumbers.add(i);
			pq.insert(i, new DummyNode());
		}
		
		while (!pq.isEmpty()) {
			Entry<Integer, DummyNode> entry = pq.deleteMin();
			assertTrue(listOfNumbers.contains(entry.getKey()));
		}
	}
	
	@Test
	public void testDeletionOfSingleEntry() {
		pq.insert(3, new DummyNode());
		pq.insert(2, new DummyNode());
		pq.insert(5, new DummyNode());
		pq.insert(1, new DummyNode());
		pq.insert(8, new DummyNode());
		
		Entry<Integer, DummyNode> entry = pq.deleteMin(); /* removes the entry with key = 1 */
		
		assertNull(pq.find(1, entry.getValue()));
	}
	
	@Test
	public void testDecreaseKeyOnSingleEntry() {
		pq.insert(3, new DummyNode());
		pq.insert(2, new DummyNode());
		pq.insert(5, new DummyNode());
		Entry<Integer, DummyNode> probe = pq.insert(2, new DummyNode());
		pq.insert(8, new DummyNode());
		
		pq.decreaseKey(probe, 1);
		
		assertNull(pq.find(2, probe.getValue()));
		
		Entry<Integer, DummyNode> minNode = pq.deleteMin();
		assertEquals(new Integer(1), minNode.getKey());
		assertEquals(probe.getKey(), minNode.getKey());
		assertNull(pq.find(1, probe.getValue()));
	}
	
	@Test
	public void testDecreaseKeyMaintainsOrder() {
		List<Integer> listOfNumbers = new LinkedList<Integer>();
		for (int i = 0; i < 98; i += 2) {
			listOfNumbers.add(i);
			DummyNode node = new DummyNode();
			node.setDist(i);
			pq.insert(i, node);
		}
		DummyNode numberNode = new DummyNode();
		numberNode.setDist(51);
		Entry<Integer, DummyNode> numberPQ = pq.insert(51, numberNode);
		pq.decreaseKey(numberPQ, 47);
		numberNode.setDist(47);
		
		listOfNumbers.remove(new Integer(51));
		listOfNumbers.add(47);
		
		assertTrue(verifyCorrectOrder(listOfNumbers, pq));
	}
	
	@Test(expected = UnderflowException.class)
	public void testFindUnderflow() {
		pq.find(null, null);
	}
	
	@Test(expected = UnderflowException.class)
	public void testDeleteMinUnderflow() {
		pq.deleteMin();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDecreaseKeyWrongParameter() {
		Entry<Integer, DummyNode> entry = pq.insert(11, new DummyNode());
		pq.decreaseKey(entry, 13);
	}
	
	private boolean verifyRandomInsertTestcase() {
		List<Integer> listOfNumbers = new LinkedList<Integer>();
		for (int i = 0; i < 500; i++) {
			int number = (int)(Math.random()*100);
			listOfNumbers.add(number);
			DummyNode n = new DummyNode();
			n.setDist(number);
			pq.insert(number, n);
		}
		
		return verifyCorrectOrder(listOfNumbers, pq);
	}
	
	private boolean verifyCorrectOrder(List<Integer> listOfNumbers,
									   PriorityQueue<Integer, DummyNode> pq) {
		Collections.sort(listOfNumbers);
		
		boolean correct = true;
		
		for (Integer i : listOfNumbers) {
			Entry<Integer, DummyNode> entry = pq.deleteMin();
			
			if (entry.getKey().intValue() != i.intValue() ||
				entry.getValue().getDist() != i.intValue()) {
				correct = false;
				break;
			}
		}
		
		return correct;
	}
}

class DummyNode {
	private int i;
	public void setDist(int i) { this.i = i; }
	public int getDist() { return this.i; }
}