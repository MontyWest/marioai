package com.montywest.marioai.task;

import ch.idsia.benchmark.tasks.SystemOfValues;

public class SystemOfValuesAdapter extends SystemOfValues {

	public SystemOfValuesAdapter(MWEvaluationMultipliers values) {
		this.distance = values.distance();
	    this.win = values.win();
	    this.mode = values.mode();
	    this.coins = values.coins();
	    this.flowerFire = values.flowerFire();
	    this.mushroom = values.mushroom();
	    this.kills = values.kills();
	    this.killedByFire = values.killedByFire();
	    this.killedByShell = values.killedByShell();
	    this.killedByStomp = values.killedByStomp();
	    this.timeLeft = values.timeLeft();
	    this.hiddenBlock = values.hiddenBlock();
	    this.greenMushroom = values.greenMushroom();
	    this.stomp = values.stomp();
	}
	
}
