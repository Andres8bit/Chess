package audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BlockingClip extends AudioStream {
	private Clip clip;
	private boolean restart;
	
	public BlockingClip(byte[] soundData) {
		super(soundData);
	}

	@Override
	public void open() {
		this.lock.lock();
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(soundData);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
			clip = AudioSystem.getClip();
			clip.addLineListener(this);
			clip.open(audioInputStream);
			
			while(!open) {
				condition.await();
			}
			System.out.println("Open");
			
		}catch(UnsupportedAudioFileException e) {
			throw new SoundException(e.getMessage(),e);
			
		}catch(LineUnavailableException e) {
			throw new SoundException(e.getMessage(),e);
		}catch(IOException e) {
			throw new SoundException(e.getMessage(),e);
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
			clip.close();
			while(open) {
				condition.await();
			}
			clip = null;
			System.out.println("turned off");
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
			clip.flush();
			clip.setFramePosition(0);
			clip.start();
			
			while(!started) {
				condition.await();
			}
			System.out.println("It's Started");
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
			clip.flush();
			clip.setFramePosition(0);
			clip.loop(count);
			while(!started) {
				condition.await();
			}
			System.out.println("Loop started");
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public void restart() {
		restart = true;
		stop();
		restart = false;
		start();
		
	}

	@Override
	public void stop() {
		lock.lock();
		try {
			clip.stop();
			while(started) {
				condition.await();
			}
			System.out.println("Stopped");
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	@Override
	protected void fireTaskFinished() {
		if(!restart) {
			super.fireTaskFinished();
		}
	}
}
