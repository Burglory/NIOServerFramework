package com.burglory.generic.multiplayer.server.util.data;

import java.util.Arrays;

public class DataString {

	private char[] data;
	/** Inclusive */
	private int low_bound;
	/** Exclusive */
	private int up_bound;
	private int length;

	public DataString(char[] data) {
		this.data = data;
		deselect();
	}

	/** Wrapper Object for using data from datastrings. */
	public DataString(String data) {
		this.data = data.toCharArray();
		deselect();
	}

	public void setData(char[] data) {
		this.data = data;
		deselect();
	}

	public void setData(String data) {
		this.data = data.toCharArray();
		deselect();
	}

	public char[] getOriginalData() {
		return data;
	}

	public char[] getData() {
		return Arrays.copyOfRange(data, low_bound, up_bound);
	}

	public String getString() {
		return String.copyValueOf(data, low_bound, length);
	}

	public String getOriginalStringData() {
		return new String(data);
	}

	public DataString copy() {
		return new DataString(Arrays.copyOfRange(data, low_bound, up_bound));
	}

	public void deselect() {
		this.low_bound = 0;
		this.up_bound = this.data.length - 1;
		this.length = this.data.length;
	}

	/** Gets the data associated to the dataname without selecting. */
	public String get(String dataname) {
		int startingpoint = find(data, low_bound, length,
				(dataname + DataStandards.DataSeparator.DString.OPEN)
						.toCharArray());
		if (startingpoint == -1) {
			throw new RuntimeException(
					"DataString_V2's Char Array does not contain: " + dataname);
			// return new DataString_V2("");
		}
		int bracketcount = 0;

		int begin = 0;
		int end = 0;

		for (int i = startingpoint; i < data.length; i++) {

			if (data[i] == DataStandards.DataSeparator.DChar.OPEN) {
				if (bracketcount == 0) {
					begin = i + 1;
				}
				bracketcount++;
			}
			if (data[i] == DataStandards.DataSeparator.DChar.CLOSE) {

				bracketcount--;
				if (bracketcount == 0) {
					end = i;
					break;
				}
			}

		}
		if (end == 0) {
			throw new RuntimeException(
					"Unexpected end of data (no closing bracket) for \""
							+ dataname + "\"");
			// return new DataString_V2("");
		}
		return String.copyValueOf(data, begin, end - begin);
	}

	/**
	 * Gets the data associated to the dataname. Returns a this DataString (for
	 * chaining purposes). Method name should become select() after removing old
	 * DataString.
	 */
	public DataString select(String dataname) {

		// TODO: Hoe te handelen als de spelersnaam twee keer voorkomt?

		int startingpoint = find(data, low_bound, length,
				(dataname + DataStandards.DataSeparator.DString.OPEN)
						.toCharArray());
		if (startingpoint == -1) {
			throw new RuntimeException(
					"DataString_V2's Char Array does not contain: " + dataname);
			// return new DataString_V2("");
		}
		int bracketcount = 0;

		int begin = 0;
		int end = 0;

		for (int i = startingpoint; i < data.length; i++) {

			if (data[i] == DataStandards.DataSeparator.DChar.OPEN) {
				if (bracketcount == 0) {
					begin = i + 1;
				}
				bracketcount++;
			}
			if (data[i] == DataStandards.DataSeparator.DChar.CLOSE) {

				bracketcount--;
				if (bracketcount == 0) {
					end = i;
					break;
				}
			}

		}
		if (end == 0) {
			throw new RuntimeException(
					"Unexpected end of data (no closing bracket)");
			// return new DataString_V2("");
		}
		this.low_bound = begin;
		this.up_bound = end;
		this.length = end - begin;
		return this;
	}

	private static int find(char[] source, int low_bound, int length,
			char[] target) {

		if (0 >= length) {
			return (target.length == 0 ? length : -1);
		}
		if (target.length == 0) {
			return 0;
		}

		char first = target[0];
		int max = low_bound + (length - target.length);

		for (int i = low_bound + 0; i <= max; i++) {
			/* Look for first character. */
			if (source[i] != first) {
				while (++i <= max && source[i] != first)
					;
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				int end = j + target.length - 1;
				for (int k = 0 + 1; j < end && source[j] == target[k]; j++, k++)
					;

				if (j == end) {
					/* Found whole string. */
					return i - low_bound;
				}
			}
		}
		return -1;

	}

	// private static int indexOf(char[] source, int sourceOffset, int
	// sourceCount,
	// char[] target, int targetOffset, int targetCount,
	// int fromIndex) {
	// if (fromIndex >= sourceCount) {
	// return (targetCount == 0 ? sourceCount : -1);
	// }
	// if (fromIndex < 0) {
	// fromIndex = 0;
	// }
	// if (targetCount == 0) {
	// return fromIndex;
	// }
	//
	// char first = target[targetOffset];
	// int max = sourceOffset + (sourceCount - targetCount);
	//
	// for (int i = sourceOffset + fromIndex; i <= max; i++) {
	// /* Look for first character. */
	// if (source[i] != first) {
	// while (++i <= max && source[i] != first);
	// }
	//
	// /* Found first character, now look at the rest of v2 */
	// if (i <= max) {
	// int j = i + 1;
	// int end = j + targetCount - 1;
	// for (int k = targetOffset + 1; j < end && source[j] ==
	// target[k]; j++, k++);
	//
	// if (j == end) {
	// /* Found whole string. */
	// return i - sourceOffset;
	// }
	// }
	// }
	// return -1;
	// }

}
