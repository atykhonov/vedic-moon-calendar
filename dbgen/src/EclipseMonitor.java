import org.satvika.jyotish.Graha;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class EclipseMonitor extends Monitor {

	private Graha graha;

	public EclipseMonitor(SQLiteConnection db) {
		super(db);
	}

	public void setGraha(Graha graha) {
		this.graha = graha;
	}

	public Graha getGraha() {
		return this.graha;
	}

	public void setTime(long time) throws SQLiteException {

		super.setTime(time);

		this.log();
	}

	@Override
	protected void log() throws SQLiteException {

		SQLiteStatement st = db.prepare("INSERT INTO eclipses VALUES("
				+ getLogId() + "," + getTime() + "," + getGraha().getNumber()
				+ ")");

		st.step();
	}
}