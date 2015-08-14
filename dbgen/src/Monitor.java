import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;

public abstract class Monitor {

	private long time;

	private int logId = 0;

	protected SQLiteConnection db;

	public Monitor(SQLiteConnection db) {
		this.db = db;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) throws SQLiteException {
		this.time = time;
	}

	protected abstract void log() throws SQLiteException;

	protected int getLogId() {
		logId++;
		return logId;
	}
}