package com.nionetframework.server.util.data;

public class DataBuilder {

	private StringBuilder stringbuilder;

	public DataBuilder() {
		this.stringbuilder = new StringBuilder();
	}

	public DataBuilder add(String dataname) {
		this.stringbuilder.append(dataname);
		return this;
	}

	public DataBuilder wrap(String data) {
		this.stringbuilder.append(DataStandards.DataSeparator.DString.OPEN)
				.append(data).append(DataStandards.DataSeparator.DString.CLOSE);
		return this;
	}

	public String getString() {
		return this.stringbuilder.toString();
	}

	public DataBuilder wrap(int data) {
		this.stringbuilder.append(DataStandards.DataSeparator.DString.OPEN)
				.append(data).append(DataStandards.DataSeparator.DString.CLOSE);
		return this;
	}

	public DataBuilder wrap(boolean data) {
		this.stringbuilder.append(DataStandards.DataSeparator.DString.OPEN)
				.append(data).append(DataStandards.DataSeparator.DString.CLOSE);
		return this;
	}

	public DataBuilder add(boolean dataname) {
		this.stringbuilder.append(dataname);
		return this;
	}

}
