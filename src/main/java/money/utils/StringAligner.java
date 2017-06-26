package money.utils;

import java.util.Objects;

/**
 * 字符串排列器, 用于价格显示时右对齐价格. <br>
 * 当 "1000" 和 "200" 进行排列后, 结果为 "1000" 和 " 200" <br>
 * 当 "100" 和 "2000" 进行排列后, 结果为 " 100" 和 "2000" <br>
 *
 * @author Him188 @ MoneySLand Project
 */
public final class StringAligner {
	private String string;
	private String another;

	public StringAligner(Object object, Object another) {
		this(object.toString(), another.toString());
	}

	public StringAligner(String string, String another) {
		this.string = string;
		this.another = another;

		if (this.string.length() == this.another.length()) {
			return;
		}

		switch (Objects.compare(this.string.length(), this.another.length(), Integer::compareTo)) {
			case 0:  //equals
				return;
			case 1:  //string is longer
				this.another = emptyString(this.string.length() - this.another.length()) + this.another;
				return;
			case -1:  //another is longer
				this.string = emptyString(this.another.length() - this.string.length()) + this.string;
				return;
		}
	}

	public String string() {
		return string;
	}

	public String another() {
		return another;
	}

	private static String emptyString(int length) {
		StringBuilder stringBuilder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			stringBuilder.append(' ');
		}
		return stringBuilder.toString();
	}
}
