package com.ce.ems.base.classes;

public class ClientResources {

	private static final String RB_TRANSLATE_CSS_CLASSNAME = "ce-translate";

	public static class WebResource {

		private final String uri;
		private final String name;

		private WebResource(String uri, String name) {
			this.uri = uri;
			this.name = name;
		}

		public static WebResource get(String uri, String name) {
			return new WebResource(uri, name);
		}

		@Override
		@ClientAware
		public String toString() {
			return "<a href='" + uri + "'>" + name + "</a>";
		}
	}

	public static class ClientRBRef {

		private String value;
		private String[] values;

		private ClientRBRef(Object value) {
			this.value = value.toString();
		}

		private ClientRBRef(String... values) {
			this.values = values;
		}

		public static ClientRBRef get(Object value) {
			return new ClientRBRef(value);
		}

		public static ClientRBRef forAll(String... values) {
			return new ClientRBRef(values);
		}

		private static String getTag(String value) {
			return "<span class='" + ClientResources.RB_TRANSLATE_CSS_CLASSNAME + "'>" + value + "</span>";
		}

		@Override
		@ClientAware
		public String toString() {

			if (value != null) {

				return getTag(this.value);

			}
			if (values != null) {

				StringBuilder sb = new StringBuilder();

				for (String e : values) {
					sb.append(getTag(e.toString()));
					sb.append(ClientResources.HtmlCharacterEnties.SPACE);
				}

				return sb.toString();
			}

			return null;
		}
	}

	public static enum HtmlCharacterEnties {

		SPACE("&#32;"), NON_BREAKING_SPACE("&nbsp;"), COMMA("&#44;"), LESS_THAN("&lt;"), GREATER_THAN(
				"&gt;"), AMPERSAND("&amp;"), DOUBLE_QUOTATION("&quot"), SINGLE_QUOTATION("&apos");

		private final String value;

		private HtmlCharacterEnties(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}
}
