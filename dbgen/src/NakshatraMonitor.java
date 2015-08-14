import org.satvika.jyotish.Nakshatra;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class NakshatraMonitor extends Monitor {

	private Nakshatra nakshatra;

	public NakshatraMonitor(SQLiteConnection db) {
		super(db);
	}

	public Nakshatra getNakshatra() {
		return nakshatra;
	}

	public void setNakshatra(Nakshatra nakshatra) throws SQLiteException {
		if (!nakshatra.equals(this.nakshatra)) {
			this.nakshatra = nakshatra;
			this.log();
		}
	}

	@Override
	protected void log() throws SQLiteException {
		SQLiteStatement st = db
				.prepare("INSERT INTO moon_nakshatra_changes VALUES("
						+ getLogId() + "," + getTime() + ","
						+ getNakshatra().getNumber() + ")");

		st.step();
	}
}