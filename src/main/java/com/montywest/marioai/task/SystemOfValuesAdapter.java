package com.montywest.marioai.task;

import ch.idsia.benchmark.tasks.SystemOfValues;

public class SystemOfValuesAdapter extends SystemOfValues {

	public SystemOfValuesAdapter(
				int distance,
				int win,
				int mode,
				int coins,
				int flowerFire,
				int mushroom,
				int kills,
				int killedByFire,
				int killedByShell,
				int killedByStomp,
				int timeLeft,
				int hiddenBlock,
				int greenMushroom,
				int stomp
			) {
		this.distance = distance;
		this.win = win;
		this.mode = mode;
		this.coins = coins;
		this.flowerFire = flowerFire;
		this.mushroom = mushroom;
		this.kills = kills;
		this.killedByFire = killedByFire;
		this.killedByShell = killedByShell;
		this.killedByStomp = killedByStomp;
		this.timeLeft = timeLeft;
		this.hiddenBlock = hiddenBlock;
		this.greenMushroom = greenMushroom;
		this.stomp = stomp;
	}
	
}
