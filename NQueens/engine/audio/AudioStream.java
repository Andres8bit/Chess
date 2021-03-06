package audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

public abstract class AudioStream  implements LineListener{

	public static final int LOOP_CONTINOUSLY = -1;
	protected final Lock lock = new ReentrantLock();
	protected final Condition condition = lock.newCondition();
	protected volatile boolean open = false;
	protected volatile boolean started = false;
	
	protected byte[] soundData;
	private List<BlockingAudioListener>listeners = Collections.synchronizedList(new ArrayList<BlockingAudioListener>());
	
	public AudioStream(byte[] soundData) {
		this.soundData = soundData;
	}
	
	public abstract void open();
	public  abstract void close();
	public abstract void start();
	public abstract void loop(int count);
	public abstract void restart();
	public abstract void stop();
	
	public boolean addListener(BlockingAudioListener listener) {
		return listeners.add(listener);
	}
	
	protected void fireTaskFinished() {
		synchronized(listeners) {
			for(BlockingAudioListener listener: listeners) {
				listener.audioFinished();
			}
		}
	}
	@Override
	public void update(LineEvent event) {
		boolean wasStarted = started;
		lock.lock();
		try {
			if(event.getType() == Type.OPEN) {
				open = true;
			}else if(event.getType() == Type.CLOSE) {
				open = false;
			}else if(event.getType() == Type.START) {
				started = true;
			}else if(event.getType() == Type.STOP) {
				started = false;
			}
			
		    this.condition.signalAll();
		}finally{
		    lock.unlock();
		}
		
		if(wasStarted && !started) {
			fireTaskFinished();
		}		
	}
}

