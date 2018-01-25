package com.kylantis.eaa.core.pdf;


public class TableConfig {

	private boolean showBorders;
	private boolean scaleWidth;

	private float startX;
	private float stopX;
	
	private float width;

	private int percentageWidth;

	public TableConfig() {
		startX = PDFWriter.DEFAULT_BORDER_X;
		percentageWidth = 100;
		computeWidth();
	}

	public TableConfig(int percentageWidth){
		startX = PDFWriter.DEFAULT_BORDER_X;
		this.percentageWidth = percentageWidth;
		computeWidth();
	}

	public boolean isShowBorders() {
		return showBorders;
	}

	public TableConfig withShowBorders(boolean showBorders) {
		this.showBorders = showBorders;
		return this;
	}

	public boolean isScaleWidth() {
		return scaleWidth;
	}

	public TableConfig withScaleWidth(boolean scaleWidth) {
		this.scaleWidth = scaleWidth;
		return this;
	}

	public int getPercentage() {
		return percentageWidth;
	}

	public TableConfig withPercentageWidth(int percentageWidth) {
		this.percentageWidth = percentageWidth;
		computeWidth();
		return this;
	}

	public float getStartX() {
		return startX;
	}

	public TableConfig withStartX() {
		this.startX = PDFWriter.DEFAULT_BORDER_X;
		computeWidth();
		return this;
	}

	public TableConfig withStartX(float startX) {
		this.startX = startX;
		return this;
	}

	public float getStopX() {
		return stopX;
	}

	public int getPercentageWidth() {
		return percentageWidth;
	}

	private void computeWidth(){
		
		float pageWidthStop = ((float)PDFWriter.FULL_PAGE_WIDTH - PDFWriter.DEFAULT_BORDER_X);
		float pageWidthStart = (float) PDFWriter.DEFAULT_BORDER_X;
		
		float pageWidth = pageWidthStop - pageWidthStart;				 
		float width =  (float) ((percentageWidth * 0.01) * pageWidth);
		
		this.width = width;
		this.stopX = this.startX + width;		
	}
	
	public float getWidth() {
		return width;
	}

}
