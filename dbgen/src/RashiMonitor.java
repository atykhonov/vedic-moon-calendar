import org.satvika.jyotish.Rashi;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;

public abstract class RashiMonitor extends Monitor {

	private Rashi rashi;

	public RashiMonitor(SQLiteConnection db) {
		super(db);
	}

	public Rashi getRashi() {
		return rashi;
	}

	public void setRashi(Rashi rashi) throws SQLiteException {
		if (!rashi.equals(this.rashi)) {
			this.rashi = rashi;
			this.log();
		}
	}
}