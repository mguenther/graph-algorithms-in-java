package de.mgu.algorithms.adt.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Test;

import de.mgu.algorithms.adt.list.DoublyLinkedList;

public class DoublyLinkedListTest {
	
	@Test
	public void testAddItems() {
		DummyNode node = new DummyNode();
		DummyNode node2 = new DummyNode();
		
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		
		dllist.insert(node);
		dllist.insert(node2);
		dllist.remove(node2);
	}
	
	@Test
	public void testEmptyList() {
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		assertTrue(dllist.isEmpty());
	}
	
	@Test
	public void testNonEmptyList() {
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		dllist.insert(new DummyNode());
		assertFalse(dllist.isEmpty());
	}
	
	@Test
	public void testHadItemsNowEmpty() {
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		DummyNode node = new DummyNode();
		dllist.insert(node);
		assertFalse(dllist.isEmpty());
		dllist.remove(node);
		assertTrue(dllist.isEmpty());
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testProvokeExceptionWithGreaterIndex() {
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		dllist.get(0);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testProvokeExceptionWithLowerIndex() {
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		dllist.get(-1);
	}
	
	@Test
	public void testGetAtBoundaries() {
		DummyNode n1 = new DummyNode();
		DummyNode n2 = new DummyNode();
		DummyNode n3 = new DummyNode();
		
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		dllist.insert(n1);
		dllist.insert(n2);
		dllist.insert(n3);
		
		DummyNode supposedToBeNode3 = dllist.get(2);
		DummyNode supposedToBeNode1 = dllist.get(0);
		
		assertEquals(n1, supposedToBeNode1);
		assertEquals(n3, supposedToBeNode3);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testRemoveNodeNotInList() {
		DummyNode n1 = new DummyNode();
		DummyNode n2 = new DummyNode();
		
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		dllist.insert(n1);
		dllist.remove(n2);
	}
	
	public void testSizeRemainsCorrectAfterInsertion() {
		DoublyLinkedList<DummyNode> dllist = new DoublyLinkedList<DummyNode>();
		
		assertTrue(dllist.isEmpty());
		assertEquals(0, dllist.size());
		
		for (int i = 0; i < 500; i++) {
			dllist.insert(new DummyNode());
			assertEquals(i+1, dllist.size());
		}
		
		assertFalse(dllist.isEmpty());
	}
}

class DummyNode {
	
}