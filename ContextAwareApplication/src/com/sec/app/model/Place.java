package com.sec.app.model;

import java.util.ArrayList;
import java.util.List;

import com.sec.app.model.EnumValues.BrightNessLevel;
import com.sec.app.model.EnumValues.NoiseLevel;

public class Place {
	
	NoiseLevel mNoiseLevel;
	BrightNessLevel mBrightnessLevel;
	int mNoiseValue;
	int mBrightnessValue;
	String mPlaceName;
	List<Neighbour> mNeighbours = new ArrayList<Neighbour>();

	/**
	 * 
	 * @return
	 */
	public NoiseLevel getmNoiseLevel() {
		return mNoiseLevel;
	}
	/**
	 * 
	 * @param mNoiseLevel
	 */
	public void setmNoiseLevel(NoiseLevel mNoiseLevel) { 
		this.mNoiseLevel = mNoiseLevel;
	}
	/**
	 * 
	 * @return
	 */
	public String getmPlaceName() {
		return mPlaceName;
	}
	/**
	 * 
	 * @param mPlaceName
	 */
	public void setmPlaceName(String mPlaceName) {
		this.mPlaceName = mPlaceName;
	}
	/**
	 * 
	 * @return
	 */
	public List<Neighbour> getmNeighbours() {
		return mNeighbours;
	}
	/*
	 * 
	 */
	public void setmNeighbours(List<Neighbour> mNeighbours) {
		this.mNeighbours = mNeighbours;
	}
	/**
	 * @return the mBrightnessLevel
	 */
	public BrightNessLevel getmBrightnessLevel() {
		return mBrightnessLevel;
	}
	/**
	 * @param mBrightnessLevel the mBrightnessLevel to set
	 */
	public void setmBrightnessLevel(BrightNessLevel mBrightnessLevel) {
		this.mBrightnessLevel = mBrightnessLevel;
	}
	/**
	 * @return the mNoiseValue
	 */
	public int getmNoiseValue() {
		return mNoiseValue;
	}
	/**
	 * @param mNoiseValue the mNoiseValue to set
	 */
	public void setmNoiseValue(int mNoiseValue) {
		this.mNoiseValue = mNoiseValue;
	}
	/**
	 * @return the mBrightnessValue
	 */
	public int getmBrightnessValue() {
		return mBrightnessValue;
	}
	/**
	 * @param mBrightnessValue the mBrightnessValue to set
	 */
	public void setmBrightnessValue(int mBrightnessValue) {
		this.mBrightnessValue = mBrightnessValue;
	}
	
}
