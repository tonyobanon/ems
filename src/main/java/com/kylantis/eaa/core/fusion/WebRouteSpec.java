package com.kylantis.eaa.core.fusion;

import java.util.List;

public class WebRouteSpec {

	private List<Integer> min;
	private List<Integer> max;
	
	public List<Integer> getMin() {
		return min;
	}

	public WebRouteSpec setMin(List<Integer> min) {
		this.min = min;
		return this;
	}

	public List<Integer> getMax() {
		return max;
	}

	public WebRouteSpec setMax(List<Integer> max) {
		this.max = max;
		return this;
	}

}
