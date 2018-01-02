package com.kylantis.eaa.core.pdf.invoices;

import com.kylantis.eaa.core.pdf.SizeSpec;

public class MainClass {

	public static void main(String[] args) {
		InvoiceFactory.toPDF(new SizeSpec(3), new SizeSpec(5), getSampleOrder());
	}

	private static Order getSampleOrder() {

		// Create sample order

		Order sampleOrder = new Order();

		sampleOrder.setOrderId("4347893734");

		sampleOrder.setCompanyLogo(MainClass.class.getClassLoader().getResource("com/kylantis/eaa/pdf/resources/logo2.png"));

		sampleOrder.setCompanyName("Retail XYZ Company");

		sampleOrder.setCompanyEmail("help@retail-xyz-company.co.uk");
		
		sampleOrder.setCompanyState("Washington D.C.");

		sampleOrder.setCompanyCity("City ABC");
		
		sampleOrder.setCompanyAddress("123, Wilson Avenue");

		sampleOrder.setCompanyFax("123489");

		sampleOrder.setCompanyPhone("+1-325-343-343");

		sampleOrder.setCompanyZIP("129673");

		sampleOrder.setCustomerId("34321");
		
		sampleOrder.setCustomerName("Vivian Fowler");

		sampleOrder.setCustomerState("Illinios");
		
		sampleOrder.setCustomerCity("City XYZ");

		sampleOrder.setCustomerAddress("123, Douglas Close");

		sampleOrder.setCustomerPhone("+1-325-343-343");

		sampleOrder.setCustomerZIP("129673");

		sampleOrder.addProduct(new OrderItem("Baby Diapers", "Medium Sized Diapers for toddlers", 457.34));

		sampleOrder.addProduct(new OrderItem("Black Gucci Shirt", "XXL-Sized Gucci T-Shirt with bow tie", 1054.23));

		sampleOrder.addProduct(new OrderItem("Black Shoes", "Nice black shoes of size 45", 674.00));

		sampleOrder.setSubtotal(950.00);
		sampleOrder.setTaxable(345.00);
		sampleOrder.setTaxRate(6.250);
		sampleOrder.setTaxDue(21.56);
		sampleOrder.setOther(0);

		sampleOrder.setTotal(971.56);

		sampleOrder.addComment("Total payments due in 30 days");

		sampleOrder.addComment("Please include the invoice number on your check");

		sampleOrder.setDateCreated("12/06/2017");

		return sampleOrder;
	}

}
