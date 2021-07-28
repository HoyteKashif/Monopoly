package monopoly;

public class PlayerQueue {

	public Node currentPlayer = null;

	private Node head = null;
	private Node tail = null;

	public static class Node {

		Player value;
		Node nextNode;

		Node(Player value) {
			this.value = value;
		}
	}

	// change the current player to the next available player
	public void advance() {

		if (currentPlayer == null) {
			currentPlayer = head;
		} else {
			currentPlayer = currentPlayer.nextNode;
		}

	}

	public void addNode(Player value) {

		Node newNode = new Node(value);

		if (head == null) {
			head = newNode;
		} else {
			tail.nextNode = newNode;
		}

		tail = newNode;
		tail.nextNode = head;
	}

	public Node getNode(Player searchValue) {
		Node currentNode = head;

		if (head == null) {
			return null;
		} else {
			do {
				if (currentNode.value == searchValue) {
					return currentNode;
				}

				currentNode = currentNode.nextNode;
			} while (currentNode != head);
			return null;
		}
	}

	public boolean containsNode(Player searchValue) {
		Node currentNode = head;

		if (head == null) {
			return false;
		} else {
			do {
				if (currentNode.value == searchValue) {
					return true;
				}

				currentNode = currentNode.nextNode;
			} while (currentNode != head);
			return false;
		}
	}

	public void deleteNode(Player valueToDelete) {
		Node currentNode = head;

		// the list is empty
		if (head == null) {
			return;
		}

		do {
			Node nextNode = currentNode.nextNode;
			if (nextNode.value == valueToDelete) {
				// the list has only one single element
				if (tail == head) {
					head = null;
					tail = null;
				} else {
					currentNode.nextNode = nextNode.nextNode;

					// we're deleting the head
					if (head == currentNode) {
						head = head.nextNode;
					}

					// we're deleting the tail
					if (tail == nextNode) {
						tail = currentNode;
					}
				}
				break;
			}
			currentNode = nextNode;
		} while (currentNode != head);
	}

	public void traverseList() {
		Node currentNode = head;

		if (head != null) {
			do {
				System.out.println(currentNode.value);
				currentNode = currentNode.nextNode;
			} while (currentNode != head);
		}
	}

	public Player findOwner(int location) {
		Node currentNode = head;

		if (head != null) {
			do {
				Player p = currentNode.value;
				if (p.deeds[location] != null) {
					return p;
				}
			} while (currentNode != head);
		}

		return null;
	}
}
