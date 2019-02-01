package com.scs.splitscreenfpsengine.components;

public interface IExpiringEffect extends IEntity {

	float getTimeRemaining();
	
	void setTimeRemaining(float t);
	
	void effectFinished();
}
