/***************************************************************************
 *  Copyright (C) 2011 by H-Store Project                                  *
 *  Brown University                                                       *
 *  Massachusetts Institute of Technology                                  *
 *  Yale University                                                        *
 *                                                                         *
 *  http://hstore.cs.brown.edu/                                            *
 *                                                                         *
 *  Permission is hereby granted, free of charge, to any person obtaining  *
 *  a copy of this software and associated documentation files (the        *
 *  "Software"), to deal in the Software without restriction, including    *
 *  without limitation the rights to use, copy, modify, merge, publish,    *
 *  distribute, sublicense, and/or sell copies of the Software, and to     *
 *  permit persons to whom the Software is furnished to do so, subject to  *
 *  the following conditions:                                              *
 *                                                                         *
 *  The above copyright notice and this permission notice shall be         *
 *  included in all copies or substantial portions of the Software.        *
 *                                                                         *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,        *
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF     *
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. *
 *  IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR      *
 *  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,  *
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR  *
 *  OTHER DEALINGS IN THE SOFTWARE.                                        *
 ***************************************************************************/
package com.oltpbenchmark.util;

import org.junit.Test;

import com.oltpbenchmark.util.json.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

import junit.framework.TestCase;

/**
 * 
 * @author pavlo
 */
public class TestHistogram extends TestCase {

	public static final int NUM_PARTITIONS = 100;
	public static final int NUM_SAMPLES = 100;
	public static final int RANGE = 20;
	public static final double SKEW_FACTOR = 4.0;

	private Histogram<Integer> h = new Histogram<Integer>();
	private Random rand = new Random(1);

	protected void setUp() throws Exception {
		// Cluster a bunch in the center
		int min = RANGE / 3;
		for (int i = 0; i < NUM_SAMPLES; i++) {
			h.put((rand.nextInt(min) + min));
		}
		for (int i = 0; i < NUM_SAMPLES; i++) {
			h.put((rand.nextInt(RANGE)));
		}
	}

	/**
	 * testMinMaxCount
	 */
	public void testMinMaxCount() throws Exception {
		Histogram<Integer> h = new Histogram<Integer>();
		int expected = 10;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < expected; j++)
				h.put(i);
		} // FOR
		long min_count = h.getMinCount();
		assertEquals(expected, min_count);
		long max_count = h.getMaxCount();
		assertEquals(expected, max_count);

		for (int i = 9; i >= 0; i--) {
			if (i == 5)
				continue;
			for (int j = 0; j < expected; j++)
				h.put(i);
		} // FOR
		// System.err.println(h);
		min_count = h.getMinCount();
		assertEquals(expected, min_count);
		max_count = h.getMaxCount();
		assertEquals(expected * 2, max_count);
	}

	/**
	 * testMinCountValues
	 */
	public void testMinCountValues() throws Exception {
		Histogram<Long> h = new Histogram<Long>();
		long expected = -1981;
		h.put(expected);
		for (int i = 0; i < 1000; i++) {
			h.put((long) 99999);
		} // FOR
		Collection<Long> min_values = h.getMinCountValues();
		assertNotNull(min_values);
		assertEquals(1, min_values.size());

		Long min_value = CollectionUtil.first(min_values);
		assertNotNull(min_value);
		assertEquals(expected, min_value.longValue());

		// Test whether we can get both in a set
		long expected2 = -99999;
		h.put(expected2);

		min_values = h.getMinCountValues();
		assertNotNull(min_values);
		assertEquals(2, min_values.size());
		assert (min_values.contains(expected));
		assert (min_values.contains(expected2));
	}

	/**
	 * testMaxCountValues
	 */
	public void testMaxCountValues() throws Exception {
		int expected = -1981;
		int count = 1000;
		for (int i = 0; i < count; i++) {
			h.put(expected);
		} // FOR
		Collection<Integer> max_values = h.getMaxCountValues();
		assertNotNull(max_values);
		assertEquals(1, max_values.size());

		Integer max_value = CollectionUtil.first(max_values);
		assertNotNull(max_value);
		assertEquals(expected, max_value.intValue());

		// Test whether we can get both in a set
		int expected2 = -99999;
		for (int i = 0; i < count; i++) {
			h.put(expected2);
		} // FOR

		max_values = h.getMaxCountValues();
		assertNotNull(max_values);
		assertEquals(2, max_values.size());
		assert (max_values.contains(expected));
		assert (max_values.contains(expected2));
	}

	/**
	 * testClearValues
	 */
	@Test
	public void testClearValues() throws Exception {
		Set<Object> keys = new HashSet<Object>(this.h.values());

		// Make sure that the keys are all still there
		h.setKeepZeroEntries(true);
		h.clearValues();
		assertEquals(keys.size(), h.getValueCount());
		assertEquals(keys.size(), h.values().size());
		assertEquals(0, h.getSampleCount());
		for (Object o : keys) {
			Integer k = (Integer) o;
			assertEquals(0, h.get(k).intValue());
		} // FOR

		// Now make sure they get wiped out
		h.setKeepZeroEntries(false);
		h.clearValues();
		assertEquals(0, h.getValueCount());
		assertEquals(0, h.values().size());
		assertEquals(0, h.getSampleCount());
		for (Object o : keys) {
			Integer k = (Integer) o;
			assertNull(h.get(k));
		} // FOR
	}

	/**
	 * testZeroEntries
	 */
	@Test
	public void testZeroEntries() {
		Set<Integer> attempted = new HashSet<Integer>();

		// First try to add a bunch of zero entries and make sure that they
		// aren't included
		// in the list of values stored in the histogram
		h.setKeepZeroEntries(false);
		assertFalse(h.isZeroEntriesEnabled());
		for (int i = 0; i < NUM_SAMPLES; i++) {
			int key = 0;
			do {
				key = rand.nextInt();
			} while (h.contains(key) || attempted.contains(key));
			h.put(key, 0);
			attempted.add(key);
		} // FOR
		for (Integer key : attempted) {
			assertFalse(h.contains(key));
			assertNull(h.get(key));
		} // FOR

		// Now enable zero entries and make sure that our entries make it in
		// there
		h.setKeepZeroEntries(true);
		assert (h.isZeroEntriesEnabled());
		for (Integer key : attempted) {
			h.put(key, 0);
			assert (h.contains(key));
			assertEquals(0, h.get(key).longValue());
		} // FOR

		// Disable zero entries again and make sure that our entries from the
		// last step are removed
		h.setKeepZeroEntries(false);
		assertFalse(h.isZeroEntriesEnabled());
		for (Integer key : attempted) {
			assertFalse(h.contains(key));
			assertNull(h.get(key));
		} // FOR
	}

	/**
	 * testPutValues
	 */
	@Test
	public void testPutValues() {
		Histogram<Integer> hist = new Histogram<Integer>();
		hist.put(49);
		hist.put(50);

		List<Integer> partitions = new ArrayList<Integer>();
		for (int i = 0; i < NUM_PARTITIONS; i++)
			partitions.add(i);

		hist.putAll(partitions);
		assertEquals(partitions.size(), hist.getValueCount());
		assertTrue(hist.values().containsAll(partitions));

		for (int i : partitions) {
			assertNotNull(hist.get(i));
			int cnt = hist.get(i).intValue();
			int expected = (i == 49 || i == 50 ? 2 : 1);
			assertEquals(expected, cnt);
		} // FOR
	}

	/**
	 * testToJSONString
	 */
	public void testToJSONString() throws Exception {
		String json = h.toJSONString();
		assertNotNull(json);
		for (Histogram.Members element : Histogram.Members.values()) {
			if (element == Histogram.Members.KEEP_ZERO_ENTRIES)
				continue;
			assertTrue(json.indexOf(element.name()) != -1);
		} // FOR
	}

	/**
	 * testFromJSON
	 */
	public void testFromJSON() throws Exception {
		String json = h.toJSONString();
		assertNotNull(json);
		JSONObject jsonObject = new JSONObject(json);
		System.err.println(jsonObject.toString(1));

		Histogram<Integer> copy = new Histogram<Integer>();
		copy.fromJSON(jsonObject);
		assertEquals(h.getValueCount(), copy.getValueCount());
		for (Histogram.Members element : Histogram.Members.values()) {
			if (element == Histogram.Members.VALUE_TYPE)
				continue;
			String field_name = element.toString().toLowerCase();
			Field field = Histogram.class.getDeclaredField(field_name);
			assertNotNull(field);

			Object orig_value = field.get(h);
			Object copy_value = field.get(copy);

			if (element == Histogram.Members.HISTOGRAM) {
				for (Integer value : h.values()) {
					assertNotNull(value);
					assertEquals(h.get(value), copy.get(value));
				} // FOR
			} else {
				assertEquals(orig_value.toString(), copy_value.toString());
			}
		} // FOR
	}
}
