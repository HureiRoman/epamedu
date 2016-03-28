package edu.epam.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class AjaxComandExecutor {
	private List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	private String url;

	private AjaxComandExecutor() {
	}

	private AjaxComandExecutor(
			AjaxComandExecutorBuilder ajaxComandExecutorBuilder) {
		this.parameters = ajaxComandExecutorBuilder.parameters;
		this.url = ajaxComandExecutorBuilder.url;
	}

	public String execute() {
		return HttpConnection.getInstance().post(this.url,parameters);
		
	}

	public static AjaxComandExecutorBuilder newBuilder() {
		return new AjaxComandExecutorBuilder();
	}

	public static class AjaxComandExecutorBuilder {
		private List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		private String url;

		public AjaxComandExecutorBuilder addParameter(String name, String value) {
			parameters.add(new BasicNameValuePair(name, value));
			return this;
		}

		public AjaxComandExecutorBuilder setUrl(String url) {
			this.url = url;
			return this;
		}

		public String build() {
			return new AjaxComandExecutor(this).execute();
		}
	}

}
