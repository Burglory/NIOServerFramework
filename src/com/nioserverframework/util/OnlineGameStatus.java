package com.nioserverframework.util;

public enum OnlineGameStatus {
	Created(0), Started(1), Finished(-1), Interrupted(-2), Paused(2);

	private int progress;

	private OnlineGameStatus(int progress) {
		this.progress = progress;
	}

	public int getProgress() {
		return this.progress;
	}

}
