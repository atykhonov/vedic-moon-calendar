import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class TithiMonitor extends Monitor {

	private int tithi;

	public TithiMonitor(SQLiteConnection db) {
		super(db);
	}

	public void setTithi(int tithi) throws SQLiteException {
		if (tithi != this.tithi) {
			this.tithi = tithi;
			this.log();
		}
	}

	public int getTithi() {
		return this.tithi;
	}

	@Override
	protected void log() throws SQLiteException {
		SQLiteStatement st = db
				.prepare("INSERT INTO moon_tithi_changes VALUES(" + getLogId()
						+ "," + getTime() + "," + getTithi() + ")");

		st.step();
	}
}
