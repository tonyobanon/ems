package com.kylantis.eaa.core.pdf;

import java.awt.Color;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.regex.Pattern;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.kylantis.eaa.core.forms.SignatureSpec;
import com.kylantis.eaa.core.pdf.InputControl.Size;
import com.kylantis.eaa.core.pdf.TextControl.UnderlineType;
import com.kylantis.eaa.core.pdf.signature.SignatureUtil;

public class PDFWriter implements Closeable {

	private SignatureSpec signatureSpec;

	private final String fileName;
	private final OutputStream out;

	private final PDDocument document;
	private PDPage currentPage;
	private int currentPageIndex = -1;

	private int lastPageIndex_withNoYOffset_ = -1;

	private PDPageContentStream stream;

	public static float FULL_PAGE_WIDTH = 612f;
	public static float FULL_PAGE_HEIGHT = 792f;

	public static final float DEFAULT_BORDER_X = 30f;
	public static final float DEFAULT_BORDER_Y = 40f;

	public static float PAGE_WIDTH = FULL_PAGE_WIDTH - (DEFAULT_BORDER_X * 2);
	public static float PAGE_HEIGHT = FULL_PAGE_HEIGHT - (DEFAULT_BORDER_Y * 2);

	public float CURRENT_Y = DEFAULT_BORDER_Y;

	public static final float DEFAULT_XS_PADDING = 2f;
	public static final float DEFAULT_SMALL_PADDING = 5f;

	static boolean bottomToTop = true;

	/*
	 * 
	 * 
	 * 
	 * //Drawing section separator
	 * 
	 * PDDocument document = new PDDocument(); PDPage page = new PDPage();
	 * document.addPage(page); PDPageContentStream content = new
	 * PDPageContentStream(document, page); PDFont font =
	 * PDType1Font.HELVETICA_BOLD;
	 * 
	 * int cursorX = 70; int cursorY = 500;
	 * 
	 * //draw rectangle content.setNonStrokingColor(200, 200, 200); //gray
	 * background content.fillRect(cursorX, cursorY, 100, 50);
	 * 
	 * //draw text content.setNonStrokingColor(0, 0, 0); //black text
	 * content.beginText(); content.setFont(font, 12);
	 * content.moveTextPositionByAmount(cursorX, cursorY);
	 * content.drawString("Test Data"); content.endText();
	 * 
	 * content.close(); document.save(new File("textOnBackground.pdf"));
	 * document.close();
	 * 
	 */

	public PDFWriter(String fileName) throws IOException {

		File file = new File(fileName);

		if (Files.exists(file.toPath())) {
			file.delete();
			file.createNewFile();
		}

		this.fileName = fileName;
		this.out = new FileOutputStream(file);
		this.document = new PDDocument();
		init();
	}

	private void init() throws IOException {

		this.currentPage = new PDPage();
		this.currentPageIndex = currentPageIndex + 1;
		this.document.addPage(this.currentPage);

		// Start a new content stream which will "hold" the to be created
		// content
		this.stream = new PDPageContentStream(this.document, this.currentPage);

		// System.out.println("PDF Document initialized with Page Index " +
		// currentPageIndex);

	}

	public SignatureSpec getSignatureSpec() {
		return signatureSpec;
	}

	public void setSignatureSpec(SignatureSpec signatureSpec) {
		this.signatureSpec = signatureSpec;
	}

	public void resetY() {
		CURRENT_Y = DEFAULT_BORDER_Y;
	}

	public float incrementY(float value) {
		if (bottomToTop) {
			return FULL_PAGE_HEIGHT - (CURRENT_Y + value);
		} else {
			return CURRENT_Y + value;
		}
	}

	public float decrementY(float value) {
		if (bottomToTop) {
			return FULL_PAGE_HEIGHT - (CURRENT_Y - value);
		} else {
			return CURRENT_Y - value;
		}
	}

	public float nextY(float lineDistance) throws IOException {

		// Check IsPageEnd
		// Assuming the height of the TextContent is 30
		if (isPageEnd(lineDistance)) {
			newPage();
			if (bottomToTop) {
				return FULL_PAGE_HEIGHT - CURRENT_Y;
			} else {
				return CURRENT_Y;
			}
		}

		CURRENT_Y = CURRENT_Y + lineDistance;
		if (bottomToTop) {
			return FULL_PAGE_HEIGHT - CURRENT_Y;
		} else {
			return CURRENT_Y;
		}
	}

	public float nextY() throws IOException {

		// Check IsPageEnd
		if (isPageEnd(LineDistance.MEDIUM)) {
			newPage();
			if (bottomToTop) {
				return FULL_PAGE_HEIGHT - CURRENT_Y;
			} else {
				return CURRENT_Y;
			}
		}

		CURRENT_Y = CURRENT_Y + LineDistance.MEDIUM;
		if (bottomToTop) {
			return FULL_PAGE_HEIGHT - CURRENT_Y;
		} else {
			return CURRENT_Y;
		}
	}

	public float currentY() throws IOException {
		if (bottomToTop) {
			return FULL_PAGE_HEIGHT - CURRENT_Y;
		} else {
			return CURRENT_Y;
		}
	}

	public float footerY() throws IOException {
		if (bottomToTop) {
			return DEFAULT_BORDER_Y - 10f;
		} else {
			return FULL_PAGE_HEIGHT - (DEFAULT_BORDER_Y - 10f);
		}
	}

	public boolean isPageEnd(float lineDistance) {
		// Assuming the height of the TextContent is LineDistance.MEDIUM
		return FULL_PAGE_HEIGHT < CURRENT_Y + DEFAULT_BORDER_Y + lineDistance;
	}

	public boolean isPageEnd(float lineDistance, float elementHeight) {
		// Assuming the height of the TextContent is LineDistance.MEDIUM
		return FULL_PAGE_HEIGHT < CURRENT_Y + DEFAULT_BORDER_Y + lineDistance + elementHeight;
	}

	public void newPage() throws IOException {

		this.stream.close();

		this.currentPageIndex = currentPageIndex + 1;

		// Check if this page index already exists
		try {

			this.currentPage = document.getPage(currentPageIndex);
			// Execution here, means that the page exists, do not overwrite
			this.stream = new PDPageContentStream(document, this.currentPage, AppendMode.APPEND, false, false);
			// System.out.println("Page with Index " + currentPageIndex + "
			// already exists");

		} catch (IndexOutOfBoundsException e) {

			this.currentPage = new PDPage();
			document.addPage(this.currentPage);

			this.stream = new PDPageContentStream(document, this.currentPage);
			// System.out.println("Page with Index " + currentPageIndex + " was
			// created");
		}

		// Reset Y
		CURRENT_Y = DEFAULT_BORDER_Y;

	}

	public void writeText(XCoordinate container, SizeSpec size, TextControl text) throws IOException {

		Float fontSize = text.getFontSize() > 0.0f && text.getFontSize() < size.getTextFontSize() ? text.getFontSize()
				: size.getTextFontSize();

		stream.setFont(getFont(text.getFont()), fontSize);

		stream.beginText();
		// incrementY((float) (size.getCellHeight() + 0.2))
		stream.newLineAtOffset(container.getStart(), currentY());

		stream.showText(text.getText());
		/*
		 * float containerWidth = (container.getStop() - container.getStart()) -
		 * DEFAULT_XS_PADDING;
		 * 
		 * int pixelsInLine = 0;
		 * 
		 * char[] characters = text.getText().toCharArray();
		 * 
		 * for (int i = 0; i < characters.length; i++) { Character character =
		 * characters[i]; stream.showText(character.toString());
		 * 
		 * pixelsInLine += size.getPixelWidthPerCharacter();
		 * 
		 * if (pixelsInLine > containerWidth && i + 2 < characters.length) {
		 * 
		 * // nextY(..) stream.endText(); stream.beginText();
		 * stream.newLineAtOffset(container.getStart(), nextY((float)
		 * (size.getCellHeight() + 0.2))); pixelsInLine = 0; } }
		 */
		stream.endText();

		if (text.getUnderline().equals(UnderlineType.SCALE)) {

			// The line should be few pixels before/after Text (X gradient)
			stream.moveTo(container.getStart() - DEFAULT_SMALL_PADDING, nextY(LineDistance.MINI));

			stream.lineTo(container.getStop() + DEFAULT_SMALL_PADDING, currentY());
			stream.stroke();

		}

		if (text.getUnderline().equals(UnderlineType.FULL)) {

			// The line should be few pixels before/after Text (X gradient)
			stream.moveTo(DEFAULT_BORDER_X, nextY(LineDistance.MINI));

			stream.lineTo(FULL_PAGE_WIDTH - DEFAULT_BORDER_X, currentY());
			stream.stroke();

		}
	}

	public void writeInputControl(XCoordinate container, SizeSpec size, InputControl control) throws IOException {

		float containerWidth = (container.getStop() - container.getStart()) - DEFAULT_XS_PADDING;
		float computedWidth = control.getComputedWidth() < containerWidth ? control.getComputedWidth() : containerWidth;

		switch (control.getBorderType()) {
		case BOTTOM_ONLY:

			stream.moveTo(container.getStart(), incrementY((float) (size.getCellHeight() * 0.2)));
			stream.lineTo((container.getStart() + computedWidth), incrementY((float) (size.getCellHeight() * 0.2)));
			stream.stroke();
			break;

		case FULL:

			float height = (float) (control.getWidth().equals(Size.SMALL) ? (size.getCellHeight() / 2)
					: (size.getCellHeight() / 1.1));

			stream.addRect(

					container.getStart(),

					incrementY((float) (control.getWidth().equals(Size.SMALL) ? (height * 0.25) : (height * 0.35))),

					computedWidth,

					control.getWidth().equals(Size.SMALL) ? computedWidth : height);

			stream.stroke();
			break;
		}
	}

	public void writeImage(XCoordinate container, SizeSpec size, Image image) throws IOException {

		float containerWidth = (container.getStop() - container.getStart()) - DEFAULT_XS_PADDING;

		float height = SizeSpec.fromPixel(image.getHeight());
		float computedHeight = height < size.getCellHeight() ? height : size.getCellHeight();

		PDImageXObject img = PDImageXObject.createFromFile(image
				.withDimension(SizeSpec.toPixel(computedHeight), SizeSpec.toPixel(containerWidth)).getAbsolutePath(),
				document);

		// Check IsPageEnd
		if (isPageEnd(LineDistance.MEDIUM, size.getCellHeight())) {
			newPage();
		}

		stream.drawImage(img, container.getStart(), currentY());
	}

	protected PDColor getColor(COSName itemName) {
		COSBase c = this.currentPage.getCOSObject().getItem(itemName);
		if (c instanceof COSArray) {
			PDColorSpace colorSpace = null;
			switch (((COSArray) c).size()) {
			case 1:
				colorSpace = PDDeviceGray.INSTANCE;
				break;
			case 3:
				colorSpace = PDDeviceRGB.INSTANCE;
				break;
			// case 4:
			// colorSpace = PDDeviceCMYK.INSTANCE;
			// break; TODO
			default:
				break;
			}
			return new PDColor((COSArray) c, colorSpace);
		}
		return null;
	}

	public void appendStroke(float distance) throws IOException {

		if (isPageEnd(LineDistance.MEDIUM, 5f)) {
			newPage();
		}

		// The line should be few pixels(5) before/after Text (X gradient)
		stream.moveTo(DEFAULT_BORDER_X - 5f, nextY(distance));

		stream.lineTo(FULL_PAGE_WIDTH - DEFAULT_BORDER_X, currentY());
		stream.stroke();
	}

	public void drawRect(Color color, float width, float height, boolean fill) throws IOException {
		drawRect(color, width, height, fill, true, true);
	}

	public void drawRect(Color color, float width, float height, boolean fill, boolean paddingBefore,
			boolean paddingAfter) throws IOException {
		drawRect(color, DEFAULT_BORDER_X, currentY(), width, height, fill, paddingBefore, paddingAfter);
	}

	public void drawRect(Color color, float x, float y, float width, float height, boolean fill, boolean paddingBefore,
			boolean paddingAfter) throws IOException {

		if (paddingBefore) {
			nextY(DEFAULT_SMALL_PADDING);
		}

		stream.addRect(x, y, width, height);
		stream.setNonStrokingColor(color);
		if (fill) {
			stream.fill();
		} else {
			stream.stroke();
		}
		stream.setNonStrokingColor(Color.BLACK);

		if (paddingAfter) {
			nextY(height);
			nextY(DEFAULT_SMALL_PADDING * 2);
		}
	}

	public void appendTable(Table table, boolean noOffsetY) throws IOException {

		float YOffset = CURRENT_Y;

		if (lastPageIndex_withNoYOffset_ == -1 && noOffsetY) {
			// This is the first page in the group
			// Set the current Page
			System.out.println("Setting the table start Page Index to " + currentPageIndex);
			lastPageIndex_withNoYOffset_ = currentPageIndex;
		}

		for (Row o : table) {

			if (o.getPadding() != null) {
				// Add padding
				nextY(o.getPadding().getCellHeight() / 2);
			}

			SizeSpec sizeSpec = o.getFontSpec();

			// Check IsPageEnd
			if (isPageEnd(sizeSpec.getCellHeight())) {
				newPage();
			}

			// Add cell height
			nextY(sizeSpec.getCellHeight() / 2);

			float currentXStart = table.getConfig().getStartX();

			for (Column column : o) {

				float currentXEnd = ((float) currentXStart + column.getWidth());

				if (column.value() instanceof InputControl) {

					InputControl input = (InputControl) column.value();
					writeInputControl(new XCoordinate(currentXStart, currentXEnd),
							o.getComponentSizeSpec() != null ? o.getComponentSizeSpec() : sizeSpec, input);

				} else if (column.value() instanceof TextControl) {

					TextControl text = (TextControl) column.value();
					writeText(new XCoordinate(currentXStart, currentXEnd), sizeSpec, text);

				} else if (column.value() instanceof Image) {

					Image image = (Image) column.value();
					writeImage(new XCoordinate(currentXStart, currentXEnd), sizeSpec, image);
				}

				// Increment by averageWidthX
				currentXStart = currentXEnd;
			}

			// Add cell height
			nextY(sizeSpec.getCellHeight() / 2);

			if (o.getPadding() != null) {
				// Add padding
				nextY(o.getPadding().getCellHeight() / 2);
			}
		}

		/*
		 * if (lastPageIndex_withNoYOffset_ != -1 &&
		 * lastPageIndex_withNoYOffset_ != currentPageIndex) { // restore the
		 * page stream this.currentPage = document.getPage(currentPageIndex);
		 * this.stream.close(); this.stream = new PDPageContentStream(document,
		 * currentPage); }
		 */

		if (noOffsetY) {
			// This indicates that that table belongs to a group whose members
			// must start at the same Y axis.
			// Set Y back to YOffset
			CURRENT_Y = YOffset;

			// We may need to get the exact page where the previous table(s)
			// started from

			if (lastPageIndex_withNoYOffset_ != currentPageIndex) {

				// Scroll back to the first page of the table
				System.out.println("Scrolling back to Page Index " + lastPageIndex_withNoYOffset_);
				this.stream.close();
				this.currentPage = document.getPage(lastPageIndex_withNoYOffset_);
				this.currentPageIndex = lastPageIndex_withNoYOffset_;
				this.stream = new PDPageContentStream(document, this.currentPage, AppendMode.APPEND, false, false);
			}

		} else {
			// Commit group table append operation
			lastPageIndex_withNoYOffset_ = -1;
		}

	}

	private PDFont getFont(String font) throws IOException {
		switch (font) {
		case "1":
			return PDType1Font.COURIER;
		case "2":
			return PDType1Font.COURIER_BOLD;
		case "3":
			return PDType1Font.COURIER_BOLD_OBLIQUE;
		case "4":
			return PDType1Font.COURIER_OBLIQUE;
		case "5":
			return PDType1Font.HELVETICA;
		case "6":
			return PDType1Font.HELVETICA_BOLD;
		case "7":
			return PDType1Font.HELVETICA_BOLD_OBLIQUE;
		case "8":
			return PDType1Font.HELVETICA_OBLIQUE;
		case "9":
			return PDType1Font.SYMBOL;
		case "10":
			return PDType1Font.TIMES_BOLD;
		case "11":
			return PDType1Font.TIMES_BOLD_ITALIC;
		case "12":
			return PDType1Font.TIMES_ITALIC;
		case "13":
			return PDType1Font.TIMES_ROMAN;
		case "14":
			return PDType1Font.ZAPF_DINGBATS;

		default:
			return PDType0Font.load(document, getClass().getClassLoader().getResourceAsStream(font));
		}
	}

	@Override
	public void close() throws IOException {

		stream.close();

		document.save(out);
		document.close();
		out.close();

		if (signatureSpec != null) {
			SignatureUtil.signDocument(fileName, fileName.replaceFirst(Pattern.quote(".pdf"), "_signed.pdf"),
					signatureSpec);
		}

	}

	public static class LineDistance {
		public static float MINI = 10;
		public static float SMALL = 20;
		public static float MEDIUM = 30;
		public static float LARGE = 40;
	}

	static {
		// We need to disable LittleCMS in favor of the old KCMS(Kodak Color
		// Management System)
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
	}

}
