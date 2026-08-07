package com.jamiedev.bygone.common.entity;

public interface BlockPhasingEntity {

	boolean isInsideBlock();
	void setInsideBlock(boolean value);

	boolean isPhasing();
	void setPhasing(boolean value);

	boolean canStartPhasing();

	void onStartPhasing();
	void onStopPhasing();

	void tickPhasing();
	int getPhasingTime();
	int getMaxPhasingTicks();

}
