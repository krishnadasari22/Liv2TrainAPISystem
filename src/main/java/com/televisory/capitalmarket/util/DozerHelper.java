package com.televisory.capitalmarket.util;


import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;

/**
 * Helper class for Dozer Mapper.
 *
 */
public final class DozerHelper {

	/**
	 * Instantiates a new dozer helper.
	 */
	private DozerHelper() {

	}

	/**
	 * This method map source list to another list of type destType.
	 *
	 * @param <T> the generic type
	 * @param <U> the generic type
	 * @param mapper dozer mapper
	 * @param source source list
	 * @param destType destType class
	 * @return dest list
	 */
	public static <T, U> ArrayList<U> map(final Mapper mapper, final List<T> source, final Class<U> destType) {

		final ArrayList<U> dest = new ArrayList<U>();
		for (final T element : source) {
			if (element != null) {
				dest.add(mapper.map(element, destType));
			}
		}
		return dest;
	}
}
