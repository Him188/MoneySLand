package money.utils;

import java.util.Objects;

/**
 * Base range
 *
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class Range {
	public final int min;
	public final int max;

	public Range(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public boolean inRange(int number) {
		return (number >= min && number < max) || (Math.abs(number) >= min && Math.abs(number) < max);
	}

	public int getLength() {
		return max - min;
	}

	public int getRealLength() {
		return max - min + 1;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof Range && ((Range) obj).getMin() == this.getMin() && ((Range) obj).getMax() == this.getMax());
	}

	@Override
	public String toString() {
		return min + "/" + max;
	}

	public static Range fromString(String string) {
		Objects.requireNonNull(string);

		String[] strings = string.split("/");
		if (strings.length != 2) {
			return null;
		}

		try {
			return new Range(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
