package audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SoundEvent implements Runnable {
	public static final String SHUT_DOWN = "shutDown";
	protected AudioStream audio;
	protected BlockingQueue<String>queue;
	private Thread consumer;
	
	
	public SoundEvent (AudioStream audio) {
		this.audio = audio;
	}
	
	public void initialize() {
		audio.addListener(getListener());
		queue = new LinkedBlockingQueue<String>();
		consumer = new Thread(this);
		consumer.start();
	}
	
	public void put(String event) {
		try {
			queue.put(event);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	public void shutDown() {
		Thread temp = consumer;
		try {
			queue.put(SHUT_DOWN);
			temp.join(10000L);
			System.out.println("Event shutdown");
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(Thread.currentThread() == consumer) {
			try {
				processEvent(queue.take());
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	protected void processEvent(String event) throws InterruptedException{
		
	}
	
	protected void onAudioFinished() {}

	private BlockingAudioListener getListener() {
		return new BlockingAudioListener() {
			@Override
			public void audioFinished() {
				onAudioFinished();
			}
		};
	}


	
	
}
