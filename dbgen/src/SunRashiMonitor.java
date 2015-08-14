import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class SunRashiMonitor extends RashiMonitor {

	public SunRashiMonitor(SQLiteConnection db) {
		super(db);
	}

	@Override
	protected void log() throws SQLiteException {
		SQLiteStatement st = db.prepare("INSERT INTO sun_rashi_changes VALUES("
				+ getLogId() + "," + getTime() + "," + getRashi().getNumber()
				+ ")");

		st.step();
	}
}