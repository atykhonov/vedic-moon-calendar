import org.satvika.jyotish.Nakshatra;
import org.satvika.jyotish.Rashi;

public class Change {

	private int num;

	private long time;

	private Rashi rashi;

	private Nakshatra nakshatra;

	public Change(int num, long time) {
		this.num = num;
		this.time = time;
	}

	public Change(Nakshatra n, long time) {
		this.nakshatra = n;
		this.time = time;
	}

	public Change(Rashi r, long time) {
		this.rashi = r;
		this.time = time;
	}

	public int getNum() {
		return this.num;
	}

	public long getTime() {
		return this.time;
	}

	public Nakshatra getNakshatra() {
		return this.nakshatra;
	}
}
