package com.arraygen.ngs.readqc;

public class NucleotideBase {
	private long Acount;
	private long Tcount;
	private long Gcount;
	private long Ccount;
	private long Ncount;

	public NucleotideBase() {
		Acount = 0;
		Tcount = 0;
		Gcount = 0;
		Ccount = 0;
		Ncount = 0;
	}


	public long getAcount() {
		return Acount;
	}

	public void incrementAcount() {
		Acount += 1;
	}

	public long getTcount() {
		return Tcount;
	}

	public void incrementTcount() {
		Tcount += 1;
	}

	public long getGcount() {
		return Gcount;
	}

	public void incrementGcount() {
		Gcount += 1;
	}

	public long getCcount() {
		return Ccount;
	}

	public void incrementCcount() {
		Ccount += 1;
	}

	public long getNcount() {
		return Ncount;
	}

	public void incrementNcount() {
		Ncount += 1;
	}


}
