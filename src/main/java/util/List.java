package util;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**This class provides various methods to manipulate and access the elements in the list.
 * @author Jasmine Justin
 * @author Jimena Reyes
 * @param <E> is the general list type
 */
public class List <E> implements Iterable<E> {

    /**general array type of objects*/
    private E[] objects;
    /**size of array at a given time*/
    private int size;
    /**initial capacity of general array*/
    private static final int DEFAULT_CAPACITY = 4;

    /**
     * Constructs an empty list with an initial capacity of 4.
     */
    public List() {
        this.objects = (E[]) new Object[4];
        this.size = 0;
    }

    /**
     * Finds the index of the specified element in the list.
     * @param e the element to search for
     * @return the index of the element if found, otherwise -1
     */
    private int find (E e) {
        for(int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Doubles the capacity of the list when it is full.
     */
    private void grow(){
        E[] newObjects = (E[]) new Object[objects.length *2];
        System.arraycopy(objects, 0, newObjects, 0 , objects.length);
        objects = newObjects;
    }

    /**
     * Checks if the list contains the specified element.
     * @param e the element to check for
     * @return true if the element is in the list, otherwise false
     */
    public boolean contains(E e){
        return find(e) != -1;
    }

    /**
     * Adds a new element to the end of the list.
     * @param e the element to add
     */
    public void add(E e) {
        if(size == objects.length) {
            grow();
        }
        objects[size++] = e;
    }

    /**
     * Removes the first occurrence of the specified element from the list.
     * @param e the element to remove
     */
    public void remove(E e) {
        int index = find(e);
        if (index == -1){
            return;
        }
        for (int i = index; i < size; i++) {
            objects[i] = objects[i + 1];
        }
        objects[--size] = null;
    }

    /**
     * Checks if the list is empty.
     * @return true if the list is empty, otherwise false
     */
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * Returns the number of elements in the list.
     * @return the size of the list
     */
    public int size(){
        return size;
    }

    /**
     * Retrieves the element at the specified index.
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws NoSuchElementException if the index is out of bounds
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new NoSuchElementException("Invalid index: " + index);
        }
        return objects[index];
    }

    /**
     * Replaces the element at the specified index with the specified element.
     * @param index the index of the element to replace
     * @param e the new element to set
     * @throws NoSuchElementException if the index is out of bounds
     */
    public void set(int index, E e) {
        if(index <0 || index >= size) {
            throw new NoSuchElementException("Invalid index: " + index);
        }
        objects[index] = e;
    }

    /**
     * Returns the index of the specified element, or -1 if not found.
     * @param e the element to search for
     * @return the index of the element if found, otherwise -1
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * An iterator for the util.List class.
     * @param <E> is the general iterable
     */
    private class ListIterator<E> implements Iterator<E> {
        /**keeps track of position within iterable*/
        private int currentIndex = 0;
        public boolean hasNext() {
            return currentIndex < size;
        }

        /**
         * Returns the next element in the iteration.
         * @return the next element
         * @throws NoSuchElementException if there are no more elements
         */
        public E next(){
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (E) objects[currentIndex++];
        }
    }

    /**
     * Returns an iterator over the elements in this list.
     *
     * @return an iterator for the list
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator<>();
    }
}
