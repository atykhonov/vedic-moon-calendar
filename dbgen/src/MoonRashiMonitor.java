import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class MoonRashiMonitor extends RashiMonitor {

	public MoonRashiMonitor(SQLiteConnection db) {
		super(db);
	}

	@Override
	protected void log() throws SQLiteException {
		SQLiteStatement st = db
				.prepare("INSERT INTO moon_rashi_changes VALUES(" + getLogId()
						+ "," + getTime() + "," + getRashi().getNumber() + ")");

		st.step();
	}
}
