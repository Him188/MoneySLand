package money.utils;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Base range
 *
 * @author Him188 @ MoneySLand Project
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

	//用于 generateChunk, number可能为负数而 min 和 max 永远为正数
	public boolean inRange(int number, boolean checkAbsValue) {
		return (number >= min && number < max) || (number >= max && number < min)
		       || (checkAbsValue && (((number = Math.abs(number)) >= min && number < max) || (number >= max && number < min)));
	}

	public boolean inRangeIncludingFrame(int number, boolean checkAbsValue) {
		return (number >= min && number <= max) || (number >= max && number <= min)
		       || (checkAbsValue && (((number = Math.abs(number)) >= min && number <= max) || (number >= max && number <= min)));
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

	public void forEach(Consumer<? super Integer> action) {
		Objects.requireNonNull(action);
		for (int i = this.getMin(); i <= this.getMax(); i++) {
			action.accept(i);
		}
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
