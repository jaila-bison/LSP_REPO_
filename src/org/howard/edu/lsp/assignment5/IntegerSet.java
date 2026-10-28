package org.howard.edu.lsp.assignment5;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Represents a mathematical set of integers. A set never contains duplicate
 * values. Elements are kept internally in ascending order, so
 * {@link #toString()} always lists them from smallest to largest.
 *
 * <p>All operations that return an {@code IntegerSet} produce a new,
 * independent object and never modify either input set.</p>
 */
public class IntegerSet {

    /** Internal storage; a sorted set guarantees uniqueness and ordering. */
    private final TreeSet<Integer> set;

    /**
     * Constructs a new, empty IntegerSet.
     */
    public IntegerSet() {
        set = new TreeSet<Integer>();
    }

    /**
     * Private constructor that copies the given elements into a new set.
     *
     * @param elements the elements to copy
     */
    private IntegerSet(TreeSet<Integer> elements) {
        set = new TreeSet<Integer>(elements);
    }

    /**
     * Removes all elements from this set.
     */
    public void clear() {
        set.clear();
    }

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of unique elements
     */
    public int length() {
        return set.size();
    }

    /**
     * Returns true if this set and {@code b} contain exactly the same
     * elements, regardless of the order in which they were added.
     *
     * @param b the set to compare with
     * @return true if both sets contain the same elements; false otherwise
     *         (including when {@code b} is null)
     */
    public boolean equals(IntegerSet b) {
        if (b == null) {
            return false;
        }
        if (this == b) {
            return true;
        }
        return set.equals(b.set);
    }

    /**
     * Returns true if this set contains the given value.
     *
     * @param value the value to look for
     * @return true if the value is present; false otherwise
     */
    public boolean contains(int value) {
        return set.contains(value);
    }

    /**
     * Returns the largest element in this set.
     *
     * @return the largest integer in the set
     * @throws IllegalStateException if the set is empty
     */
    public int largest() {
        if (set.isEmpty()) {
            throw new IllegalStateException("Cannot get largest element of an empty set");
        }
        return set.last();
    }

    /**
     * Returns the smallest element in this set.
     *
     * @return the smallest integer in the set
     * @throws IllegalStateException if the set is empty
     */
    public int smallest() {
        if (set.isEmpty()) {
            throw new IllegalStateException("Cannot get smallest element of an empty set");
        }
        return set.first();
    }

    /**
     * Adds an item to this set. Duplicate values are ignored.
     *
     * @param item the value to add
     */
    public void add(int item) {
        set.add(item);
    }

    /**
     * Removes an item from this set. Has no effect if the item is absent.
     *
     * @param item the value to remove
     */
    public void remove(int item) {
        set.remove(item);
    }

    /**
     * Returns a new set containing every element that is in this set,
     * in {@code b}, or in both.
     *
     * @param b the other set
     * @return a new set representing the union
     */
    public IntegerSet union(IntegerSet b) {
        IntegerSet result = new IntegerSet(set);
        result.set.addAll(b.set);
        return result;
    }

    /**
     * Returns a new set containing only the elements shared by this set
     * and {@code b}.
     *
     * @param b the other set
     * @return a new set representing the intersection
     */
    public IntegerSet intersect(IntegerSet b) {
        IntegerSet result = new IntegerSet(set);
        result.set.retainAll(b.set);
        return result;
    }

    /**
     * Returns a new set containing the elements in this set that are not
     * in {@code b} (this - b).
     *
     * @param b the set whose elements are excluded
     * @return a new set representing the difference
     */
    public IntegerSet diff(IntegerSet b) {
        IntegerSet result = new IntegerSet(set);
        result.set.removeAll(b.set);
        return result;
    }

    /**
     * Returns a new set containing the elements in {@code b} that are not
     * in this set (b - this). This is the assignment-specific definition
     * of complement.
     *
     * @param b the set whose elements are considered
     * @return a new set representing the complement
     */
    public IntegerSet complement(IntegerSet b) {
        IntegerSet result = new IntegerSet(b.set);
        result.set.removeAll(set);
        return result;
    }

    /**
     * Returns true if every element of this set is also in {@code b}.
     * An empty set is a subset of every set.
     *
     * @param b the potential superset
     * @return true if this set is a subset of {@code b}; false otherwise
     */
    public boolean isSubsetOf(IntegerSet b) {
        return b.set.containsAll(set);
    }

    /**
     * Returns true if this set and {@code b} share no elements.
     * An empty set is disjoint from every set.
     *
     * @param b the other set
     * @return true if the sets have no elements in common; false otherwise
     */
    public boolean isDisjoint(IntegerSet b) {
        for (Integer value : set) {
            if (b.set.contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a new set containing the elements that occur in exactly one
     * of this set and {@code b}.
     *
     * @param b the other set
     * @return a new set representing the symmetric difference
     */
    public IntegerSet symmetricDiff(IntegerSet b) {
        IntegerSet result = new IntegerSet(set);
        for (Integer value : b.set) {
            if (!result.set.add(value)) {
                result.set.remove(value);
            }
        }
        return result;
    }

    /**
     * Returns true if this set contains no elements.
     *
     * @return true if the set is empty; false otherwise
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * Returns the elements in ascending order, separated by a comma and a
     * single space, inside square brackets, for example {@code [-3, 0, 8, 15]}.
     * An empty set returns {@code []}.
     *
     * @return the string representation of this set
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Iterator<Integer> it = set.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
