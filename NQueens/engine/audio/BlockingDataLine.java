package audio;

public class BlockingDataLine extends AudioStream {
	private AudioDataLine stream;
	 
	public BlockingDataLine(byte[] soundData) {
		super(soundData);
	}
	
	@Override
	public void restart() {
		stream.reset();
	}

	@Override
	public void open() {
		lock.lock();
		try {
			stream = new AudioDataLine(soundData);
			stream.initlialize();
			stream.addLineListener(this);
			stream.open();
			while(!open) {
				condition.await();
			}
			System.out.println("open");
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public void close() {
		lock.lock();
		try {
			stream.close();
			while(open) {
				condition.await();
			}
			System.out.println("closed");
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public void start() {
		lock.lock();
		try {
			stream.start();
			while(!started) {
				condition.await();
			}
			System.out.println("started");
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
	}

	@Override
	public void loop(int count) {
		lock.lock();
		try {
			stream.loop(count);
			while(!started) {
				condition.await();
			}
			System.out.println("Started looping");
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public void stop() {
		lock.lock();
		try {
			stream.stop();
			while(started) {
				condition.await();
			}
			System.out.println("stopped");
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
}
