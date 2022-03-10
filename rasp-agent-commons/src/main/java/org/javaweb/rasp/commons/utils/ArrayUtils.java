package org.javaweb.rasp.commons.utils;

import java.util.Collection;

import static org.apache.commons.lang3.ArrayUtils.contains;

public class ArrayUtils {

	public static boolean isEmpty(final Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean arrayContains(final int[] array, final int search) {
		return array != null && array.length > 0 && contains(array, search);
	}

	public static boolean arrayContains(final String[] array, final String stringToFind) {
		return array != null && array.length > 0 && contains(array, stringToFind);
	}

	public static void reverse(final byte[] array) {
		org.apache.commons.lang3.ArrayUtils.reverse(array);
	}

	public static String toString(final Object array) {
		return org.apache.commons.lang3.ArrayUtils.toString(array);
	}

}
