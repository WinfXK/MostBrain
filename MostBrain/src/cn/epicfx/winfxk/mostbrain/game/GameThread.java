package cn.epicfx.winfxk.mostbrain.game;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.tool.Update;

/**
 * @author Winfxk
 */
public class GameThread extends Thread {
	private Activate ac;
	private volatile int Key;
	private volatile int UpdateTime = 0;

	public GameThread(Activate activate, int Key) {
		this.ac = activate;
		this.Key = Key;
	}

	public void reloadUpdateTime() {
		UpdateTime = ac.getConfig().getInt("更新间隔");
	}

	@Override
	public void run() {
		switch (Key) {
		case 0:
			Update update = new Update(ac.getMostBrain());
			while (true)
				if (ac.getConfig().getBoolean("自动更新"))
					try {
						sleep(1000);
						if (UpdateTime-- <= 0) {
							update.start();
							UpdateTime = ac.getConfig().getInt("更新间隔");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		}
	}
}
