package com.workpoint.icpak.shared.model;

public enum CertificateAwarded implements Listable {
	KCPE(0, "KCPE"), KCSE(1, "KCSE"), DIPLOMA(2, "DIPLOMA"), HIGHERDIPLOMA(3,
			"Higher Diploma"), PGDIPLOMA(4, "PG Diploma"), DEGREE(5, "Degree"), EACE(
			6, "EACE"), KCE(7, "KCE"), KACE(8, "KACE"), MASTERS(9, "Masters");

	private Integer code;
	private String displayName;

	CertificateAwarded(Integer code, String displayName) {
		this.setDisplayName(displayName);
		this.setCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getName() {
		return name();
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
