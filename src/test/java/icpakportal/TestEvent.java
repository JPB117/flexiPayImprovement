package icpakportal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Test;

public class TestEvent {

	@Test
	public void comment() {
		String string = "January 2, 2010";
		DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		// Date date = format.parse(string);
		// System.err.println(date.getTime());
	}
}
