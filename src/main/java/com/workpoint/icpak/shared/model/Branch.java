package com.workpoint.icpak.shared.model;

public enum Branch implements Listable {
	COAST("Coast"), NAIROBI("Nairobi"), MTKENYA("Mt. Kenya"), NYANZA("Nyanza"), WESTERN(
			"Western"), NORTHRIFT("North Rift"), SOUTHRIFT("South Rift"), EASTERN(
			"Eastern"), NORTHEASTERN("North Eastern");
	/**
	 * 
	 */
	private String branchName;

	Branch(String branchName) {
		this.setBranchName(branchName);
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return branchName;
	}
}
